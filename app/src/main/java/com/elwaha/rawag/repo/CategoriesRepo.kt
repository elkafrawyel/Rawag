package com.elwaha.rawag.repo

import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.ApiResponse
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.data.models.HomeWithAds
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.data.models.requests.SubCategoriesRequest
import com.elwaha.rawag.data.models.requests.UsersRequest
import com.elwaha.rawag.data.storage.local.PreferencesHelper
import com.elwaha.rawag.data.storage.remote.RetrofitApiService
import com.elwaha.rawag.utilies.*

class CategoriesRepo(
    private val retrofitApiService: RetrofitApiService,
    private val preferencesHelper: PreferencesHelper
) {

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
                val response =
                    retrofitApiService.subCategoriesAsync(SubCategoriesRequest(categoryId)).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            },
            errorMessage = Injector.getApplicationContext().getString(R.string.generalError)
        )
    }

    suspend fun getUsers(usersRequest: UsersRequest): DataResource<List<UserModel>> {
        return safeApiCall(
            call = {

                val response: ApiResponse<List<UserModel>>

                response = if (preferencesHelper.isLoggedIn) {
                    val userString = preferencesHelper.user
                    val user = ObjectConverter().getUser(userString!!)

                    retrofitApiService.subCategoryUsersAuthAsync(
                        if (user.token.contains(Constants.AUTHORIZATION_START))
                            user.token
                        else
                            "${Constants.AUTHORIZATION_START} ${user.token}",
                        usersRequest
                    ).await()

                } else {
                    retrofitApiService.subCategoryUsersAsync(usersRequest).await()
                }

                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }
}