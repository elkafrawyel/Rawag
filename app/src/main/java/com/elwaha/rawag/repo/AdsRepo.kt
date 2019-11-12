package com.elwaha.rawag.repo

import android.net.Uri
import androidx.core.net.toUri
import com.elwaha.rawag.data.models.AdImages
import com.elwaha.rawag.data.models.AdModel
import com.elwaha.rawag.data.models.requests.AddAdRequest
import com.elwaha.rawag.data.models.requests.ProfileRequest
import com.elwaha.rawag.data.storage.local.PreferencesHelper
import com.elwaha.rawag.data.storage.remote.RetrofitApiService
import com.elwaha.rawag.utilies.*
import okhttp3.MultipartBody

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

    suspend fun uploadAds(addAdRequest: AddAdRequest, adImages: AdImages): DataResource<AdModel> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                //upload an array of images
                val images = adImages.images.map {
                    it.toUri().toMultiPart(Injector.getApplicationContext(),"images[]")
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
            }
        )
    }
}