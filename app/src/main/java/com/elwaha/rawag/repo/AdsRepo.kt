package com.elwaha.rawag.repo

import androidx.core.net.toUri
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.*
import com.elwaha.rawag.data.models.requests.*
import com.elwaha.rawag.data.storage.local.PreferencesHelper
import com.elwaha.rawag.data.storage.remote.RetrofitApiService
import com.elwaha.rawag.utilies.*

class AdsRepo(
    private val retrofitApiService: RetrofitApiService,
    private val preferencesHelper: PreferencesHelper
) {
    suspend fun myAds(user_id: String): DataResource<List<AdModel>> {
        return safeApiCall(
            call = {
                val response =
                    retrofitApiService.myAdsAsync(
                        ProfileRequest(user_id)
                    ).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun search(searchRequest: SearchRequest): DataResource<List<UserModel>> {
        return safeApiCall(
            call = {
                val response: ApiResponse<List<UserModel>>
                response = if (preferencesHelper.isLoggedIn) {
                    val userString = Injector.getPreferenceHelper().user
                    val user = ObjectConverter().getUser(userString!!)
                    retrofitApiService.searchAuthAsync(
                        if (user.token.contains(Constants.AUTHORIZATION_START))
                            user.token
                        else
                            "${Constants.AUTHORIZATION_START} ${user.token}", searchRequest
                    ).await()
                } else {
                    retrofitApiService.searchAsync(searchRequest).await()
                }

                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun editInfo(editAdRequest: EditAdRequest): DataResource<String> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)
                val response = retrofitApiService.editAdAsync(
                    if (user.token.contains(Constants.AUTHORIZATION_START))
                        user.token
                    else
                        "${Constants.AUTHORIZATION_START} ${user.token}", editAdRequest
                ).await()

                if (response.status)
                    DataResource.Success(response.msg)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun delete(deleteAdRequest: DeleteAdRequest): DataResource<Boolean> {
        return safeApiCall(
            call = {
                val response = retrofitApiService.deleteAdAsync(deleteAdRequest).await()
                if (response.status)
                    DataResource.Success(response.status)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun deleteAdImage(deleteAdImageRequest: DeleteAdImageRequest): DataResource<Boolean> {
        return safeApiCall(
            call = {
                val response = retrofitApiService.deleteAdImageAsync(deleteAdImageRequest).await()
                if (response.status)
                    DataResource.Success(response.status)
                else
                    DataResource.Error(response.msg)
            }
        )
    }


    suspend fun uploadAds(addAdRequest: AddAdRequest, adImages: AdImages): DataResource<AdModel> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                //upload an array of images
                val images = adImages.images.map {
                    it.toUri().toMultiPart(Injector.getApplicationContext(), "images[]")
                }

                val response =
                    retrofitApiService.addAdAsync(
                        if (user.token.contains(Constants.AUTHORIZATION_START))
                            user.token
                        else
                            "${Constants.AUTHORIZATION_START} ${user.token}",
                        addAdRequest.sub_category_id.toMultiPart(),
                        addAdRequest.city_id.toMultiPart(),
                        addAdRequest.price.toMultiPart(),
                        addAdRequest.payment_type.toMultiPart(),
                        addAdRequest.baqa_id.toMultiPart(),
                        addAdRequest.days.toMultiPart(),
                        images
                    ).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            },
            errorMessage = Injector.getApplicationContext().getString(R.string.errorUploadAd)
        )
    }

    suspend fun addImagesToAd(adId:String, adImages: AdImages): DataResource<AdModel> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                //upload an array of images
                val images = adImages.images.map {
                    it.toUri().toMultiPart(Injector.getApplicationContext(), "images[]")
                }

                val response =
                    retrofitApiService.addImagesToAdAsync(
                        adId.toMultiPart(),
                        images
                    ).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            },
            errorMessage = Injector.getApplicationContext().getString(R.string.errorUploadAd)
        )
    }
}