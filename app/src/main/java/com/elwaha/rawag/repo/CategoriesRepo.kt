package com.elwaha.rawag.repo

import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.data.models.HomeWithAds
import com.elwaha.rawag.data.models.requests.SubCategoriesRequest
import com.elwaha.rawag.data.storage.remote.RetrofitApiService
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.safeApiCall

class CategoriesRepo(private val retrofitApiService: RetrofitApiService) {

    suspend fun getCategories(): DataResource<List<CategoryModel>> {
        return safeApiCall(
            call = {
                val response = retrofitApiService.categoriesAsync().await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg!!)
            },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    suspend fun getCategoriesWithAds(): DataResource<HomeWithAds> {
        return safeApiCall(
            call = {
                val response = retrofitApiService.homeAsync().await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg!!)
            },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    suspend fun getSubCategories(categoryId: String): DataResource<List<CategoryModel>> {
        return safeApiCall(
            call = {
                val response = retrofitApiService.subCategoriesAsync(SubCategoriesRequest(categoryId)).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }
}