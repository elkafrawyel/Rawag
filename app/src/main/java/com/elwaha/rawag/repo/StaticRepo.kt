package com.elwaha.rawag.repo

import com.elwaha.rawag.data.models.*
import com.elwaha.rawag.data.models.requests.CitiesRequest
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

    suspend fun getAllBaqas(): DataResource<List<BaqaModel>> {
        return safeApiCall(
            call = {
                val response =
                    retrofitApiService.allBaqasAsync().await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun getCountries(): DataResource<List<CountryModel>> {
        return safeApiCall(
            call = {
                val response =
                    retrofitApiService.countriesAsync().await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun getCities(countryId: String): DataResource<List<CityModel>> {
        return safeApiCall(
            call = {
                val response =
                    retrofitApiService.citiesAsync(CitiesRequest(countryId)).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }


}