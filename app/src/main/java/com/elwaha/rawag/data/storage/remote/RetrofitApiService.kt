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

    @GET("countries")
    fun countriesAsync(): Deferred<ApiResponse<List<CountryModel>>>

    @POST("cities")
    fun citiesAsync(@Body citiesRequest: CitiesRequest): Deferred<ApiResponse<List<CityModel>>>

    @GET("allBaqas")
    fun allBaqasAsync(): Deferred<ApiResponse<List<BaqaModel>>>

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

    @POST("userComments")
    fun allCommentsAsync(
        @Body allCommentsRequest: AllCommentsRequest
    ): Deferred<ApiResponse<List<CommentModel>>>

    //================================ With Auth =========================================

    @POST("myAds")
    fun myAdsAsync(
        @Body profileRequest: ProfileRequest
    ): Deferred<ApiResponse<List<AdModel>>>

    @POST("addComment")
    fun addCommentAsync(
        @Header("Authorization") token: String,
        @Body addCommentRequest: AddCommentRequest
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

    @POST("addContact")
    fun addContactAsync(
        @Header("Authorization") token: String,
        @Body addContactRequest: AddContactRequest
    ): Deferred<ApiResponseNoData>

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

    @POST("addAd")
    @Multipart
    fun addAdAsync(
        @Header("Authorization") token: String,
        @Part("sub_category_id") sub_category_id: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("price") price: RequestBody,
        @Part("payment_type") payment_type: RequestBody,
        @Part("baqa_id") baqa_id: RequestBody,
        @Part("days") days: RequestBody,
        @Part image: List<MultipartBody.Part>
    ): Deferred<ApiResponse<AdModel>>

    @POST("addReport")
    fun addReportAsync(
        @Header("Authorization") token: String,
        @Body addReportRequest: AddReportRequest
    ): Deferred<ApiResponseNoData>

    @POST("subcategoryHomeUsers")
    fun subCategoryUsersAsync(
        @Body usersRequest: UsersRequest
    ): Deferred<ApiResponse<List<UserModel>>>

    @POST("subcategoryUsers")
    fun subCategoryUsersAuthAsync(
        @Header("Authorization") token: String,
        @Body usersRequest: UsersRequest
    ): Deferred<ApiResponse<List<UserModel>>>

    @POST("addLike")
    fun addLikeAuthAsync(
        @Header("Authorization") token: String,
        @Body addLikeRequest: AddLikeRequest
    ): Deferred<ApiResponse<Int>>

    @GET("myFavouriteAuth")
    fun myFavouriteAuthAsync(
        @Header("Authorization") token: String
    ): Deferred<ApiResponse<FavouritesWithAds>>

    @GET("myFavourite")
    fun myFavouriteAsync(): Deferred<ApiResponse<FavouritesWithAds>>

}