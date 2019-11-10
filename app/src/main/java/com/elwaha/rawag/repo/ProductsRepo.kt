package com.elwaha.rawag.repo

import com.elwaha.rawag.data.models.AdModel
import com.elwaha.rawag.data.models.requests.ProfileRequest
import com.elwaha.rawag.data.storage.local.PreferencesHelper
import com.elwaha.rawag.data.storage.remote.RetrofitApiService
import com.elwaha.rawag.utilies.Constants
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.ObjectConverter
import com.elwaha.rawag.utilies.safeApiCall

class ProductsRepo(
    private val retrofitApiService: RetrofitApiService,
    private val preferencesHelper: PreferencesHelper
) {
    suspend fun myAds(user_id:String): DataResource<List<AdModel>> {
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
}