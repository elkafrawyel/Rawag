package com.elwaha.rawag.data.storage.remote

import com.elwaha.rawag.data.models.*
import com.elwaha.rawag.data.models.requests.*
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RetrofitApiService {

    @POST("login")
    fun loginAsync(@Body loginRequest: LoginRequest): Deferred<ApiResponse<UserModel>>

    @POST("profile")
    fun profileAsync(@Body profileRequest: ProfileRequest): Deferred<ApiResponse<UserModel>>

    @GET("categories")
    fun categoriesAsync(): Deferred<ApiResponse<List<CategoryModel>>>

    @GET("aboutus")
    fun aboutAsync(): Deferred<ApiResponse<AboutModel>>

    @GET("rules")
    fun rulesAsync(): Deferred<ApiResponse<TermsModel>>

    @POST("subcategories")
    fun subCategoriesAsync(
        @Body subCategoriesRequest: SubCategoriesRequest
    ): Deferred<ApiResponse<List<CategoryModel>>>

    @GET("home")
    fun homeAsync(): Deferred<ApiResponse<HomeWithAds>>

    @GET("problems")
    fun problemsAsync(): Deferred<ApiResponse<List<ProblemModel>>>

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
        @Part("about") about: RequestBody,
        @Part("sub_category_id") sub_category_id: RequestBody,
        @Part("accepted") accepted: RequestBody,
        @Part avatar: MultipartBody.Part
    ): Deferred<ApiResponse<UserModel>>

    //================================ With Auth =========================================

    @POST("myAds")
    fun myAdsAsync(
        @Body profileRequest: ProfileRequest
    ): Deferred<ApiResponse<List<AdModel>>>

    @POST("addComment")
    fun addCommentAsync(
        @Header("Authorization") token: String
    ): Deferred<ApiResponse<CommentModel>>

    @POST("changePassword")
    fun updatePasswordAsync(
        @Header("Authorization") token: String,
        @Body UpdatePasswordRequest: UpdatePasswordRequest
    ): Deferred<ApiResponseNoData>

    @POST("editSocail")
    fun editSocialAsync(
        @Header("Authorization") token: String,
        @Body editSocialRequest: EditSocialRequest
    ): Deferred<ApiResponse<UserModel>>


    @POST("editProfile")
    fun editProfileAsync(
        @Header("Authorization") token: String,
        @Body editProfileRequest: EditProfileRequest
    ): Deferred<ApiResponse<UserModel>>

    @POST("editProfile")
    @Multipart
    fun editProfileWithImageAsync(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("address") address: RequestBody,
        @Part("lang") lang: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("about") about: RequestBody,
        @Part("sub_category_id") sub_category_id: RequestBody,
        @Part avatar: MultipartBody.Part
    ): Deferred<ApiResponse<UserModel>>
}