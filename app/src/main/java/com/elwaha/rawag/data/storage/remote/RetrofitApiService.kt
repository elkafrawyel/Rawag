package com.elwaha.rawag.data.storage.remote

import com.elwaha.rawag.data.models.CategoriesResponse
import com.elwaha.rawag.data.models.HomeCategoriesWithAds
import com.elwaha.rawag.data.models.HomeResponseWithAds
import com.elwaha.rawag.data.models.UserResponse
import com.elwaha.rawag.data.models.requests.LoginRequest
import com.elwaha.rawag.data.models.requests.SubCategoriesRequest
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RetrofitApiService {

    @POST("login")
    fun loginAsync(@Body loginRequest: LoginRequest): Deferred<UserResponse>

    @GET("categories")
    fun categoriesAsync(): Deferred<CategoriesResponse>

    @POST("subcategories")
    fun subCategoriesAsync(
        @Body subCategoriesRequest: SubCategoriesRequest
    ):Deferred<CategoriesResponse>

    @GET("home")
    fun homeAsync(): Deferred<HomeResponseWithAds>

    @Multipart
    @POST("register")
    fun registerAsync(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("address") address: RequestBody,
        @Part("lang") lang: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("sub_category_id") sub_category_id: RequestBody,
        @Part("accepted") accepted: RequestBody,
        @Part avatar: MultipartBody.Part
    ): Deferred<UserResponse>
}