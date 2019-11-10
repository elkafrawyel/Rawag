package com.elwaha.rawag.repo

import com.elwaha.rawag.data.models.AboutModel
import com.elwaha.rawag.data.models.ProblemModel
import com.elwaha.rawag.data.models.TermsModel
import com.elwaha.rawag.data.storage.remote.RetrofitApiService
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.safeApiCall

class StaticRepo(private val retrofitApiService: RetrofitApiService) {
    suspend fun getProblems(): DataResource<List<ProblemModel>> {
        return safeApiCall(
            call = {
                val response =
                    retrofitApiService.problemsAsync().await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun getAbout(): DataResource<AboutModel> {
        return safeApiCall(
            call = {
                val response =
                    retrofitApiService.aboutAsync().await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun getTerms(): DataResource<TermsModel> {
        return safeApiCall(
            call = {
                val response =
                    retrofitApiService.rulesAsync().await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }
}