package com.elwaha.rawag.repo

import android.net.Uri
import android.text.BoringLayout
import com.elwaha.rawag.data.models.ApiResponse
import com.elwaha.rawag.data.models.CommentModel
import com.elwaha.rawag.data.models.FavouritesWithAds
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.data.models.requests.*
import com.elwaha.rawag.data.storage.local.PreferencesHelper
import com.elwaha.rawag.data.storage.remote.RetrofitApiService
import com.elwaha.rawag.utilies.*

class UserRepo(
    private val retrofitApiService: RetrofitApiService,
    private val preferencesHelper: PreferencesHelper
) {

    suspend fun login(loginRequest: LoginRequest): DataResource<UserModel> {
        return safeApiCall(
            call = {
                val response = retrofitApiService.loginAsync(loginRequest).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun profile(profileRequest: ProfileRequest): DataResource<UserModel> {
        return safeApiCall(
            call = {
                val response: ApiResponse<UserModel>
                response = if (preferencesHelper.isLoggedIn) {
                    val userString = Injector.getPreferenceHelper().user
                    val user = ObjectConverter().getUser(userString!!)

                    retrofitApiService.profileAuthAsync(
                        if (user.token.contains(Constants.AUTHORIZATION_START))
                            user.token
                        else
                            "${Constants.AUTHORIZATION_START} ${user.token}", profileRequest
                    ).await()
                } else {
                    retrofitApiService.profileAsync(profileRequest).await()
                }

                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun register(registerRequest: RegisterRequest, uri: Uri): DataResource<UserModel> {
        return safeApiCall(
            call = {
                val response = retrofitApiService.registerAsync(
                    registerRequest.name.toMultiPart(),
                    registerRequest.email.toMultiPart(),
                    registerRequest.phone.toMultiPart(),
                    registerRequest.password.toMultiPart(),
                    registerRequest.address.toMultiPart(),
                    registerRequest.lang.toMultiPart(),
                    registerRequest.lat.toMultiPart(),
                    registerRequest.about.toMultiPart(),
                    registerRequest.sub_category_id.toMultiPart(),
                    registerRequest.accepted.toMultiPart(),
                    uri.toMultiPart(Injector.getApplicationContext(), "avatar")
                ).await()

                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun editProfile(
        editProfileRequest: EditProfileRequest,
        uri: Uri?
    ): DataResource<UserModel> {
        return safeApiCall(
            call = {

                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)
                val response: ApiResponse<UserModel>
                if (uri == null) {
                    response = retrofitApiService.editProfileAsync(
                        if (user.token.contains(Constants.AUTHORIZATION_START))
                            user.token
                        else
                            "${Constants.AUTHORIZATION_START} ${user.token}",
                        editProfileRequest
                    ).await()

                } else {
                    response = retrofitApiService.editProfileWithImageAsync(
                        if (user.token.contains(Constants.AUTHORIZATION_START))
                            user.token
                        else
                            "${Constants.AUTHORIZATION_START} ${user.token}",
                        editProfileRequest.name.toMultiPart(),
                        editProfileRequest.email.toMultiPart(),
                        editProfileRequest.phone.toMultiPart(),
                        editProfileRequest.address.toMultiPart(),
                        editProfileRequest.lang.toMultiPart(),
                        editProfileRequest.lat.toMultiPart(),
                        editProfileRequest.about.toMultiPart(),
                        editProfileRequest.sub_category_id.toMultiPart(),
                        uri.toMultiPart(Injector.getApplicationContext(), "avatar")
                    ).await()
                }

                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)

            }
        )
    }

    suspend fun updatePassword(updatePasswordRequest: UpdatePasswordRequest): DataResource<String> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                val response = retrofitApiService.updatePasswordAsync(
                    if (user.token.contains(Constants.AUTHORIZATION_START))
                        user.token
                    else
                        "${Constants.AUTHORIZATION_START} ${user.token}",
                    updatePasswordRequest
                ).await()
                if (response.status)
                    DataResource.Success(response.msg)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun sendFirebaseToken(token:String): DataResource<String> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                val response = retrofitApiService.sendFirebaseTokenAdAsync(
                    SendFirebaseTokenRequest(user.id.toString(),token)
                ).await()

                if (response.status)
                    DataResource.Success(response.msg)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun notificationStatus(): DataResource<Boolean> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                val response = retrofitApiService.notificationStatusAsync(
                    NotificationStateRequest(user.id.toString())
                ).await()

                if (response.status){
                    if (response.data==1){
                        DataResource.Success(true)
                    }else{
                        DataResource.Success(false)
                    }
                }
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun editSocial(editSocialRequest: EditSocialRequest): DataResource<UserModel> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                val response = retrofitApiService.editSocialAsync(
                    if (user.token.contains(Constants.AUTHORIZATION_START))
                        user.token
                    else
                        "${Constants.AUTHORIZATION_START} ${user.token}", editSocialRequest
                ).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun addContact(addContactRequest: AddContactRequest): DataResource<String> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                val response = retrofitApiService.addContactAsync(
                    if (user.token.contains(Constants.AUTHORIZATION_START))
                        user.token
                    else
                        "${Constants.AUTHORIZATION_START} ${user.token}", addContactRequest
                ).await()
                if (response.status)
                    DataResource.Success(response.msg)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun addComment(addCommentRequest: AddCommentRequest): DataResource<CommentModel> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                val response = retrofitApiService.addCommentAsync(
                    if (user.token.contains(Constants.AUTHORIZATION_START))
                        user.token
                    else
                        "${Constants.AUTHORIZATION_START} ${user.token}", addCommentRequest
                ).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun addReport(addReportRequest: AddReportRequest): DataResource<Boolean> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                val response = retrofitApiService.addReportAsync(
                    if (user.token.contains(Constants.AUTHORIZATION_START))
                        user.token
                    else
                        "${Constants.AUTHORIZATION_START} ${user.token}", addReportRequest
                ).await()
                if (response.status)
                    DataResource.Success(response.status)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun addLike(addLikeRequest: AddLikeRequest): DataResource<Int> {
        return safeApiCall(
            call = {
                val userString = Injector.getPreferenceHelper().user
                val user = ObjectConverter().getUser(userString!!)

                val response = retrofitApiService.addLikeAuthAsync(
                    if (user.token.contains(Constants.AUTHORIZATION_START))
                        user.token
                    else
                        "${Constants.AUTHORIZATION_START} ${user.token}",
                    addLikeRequest
                ).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun allComments(userId: String): DataResource<List<CommentModel>> {
        return safeApiCall(
            call = {
                val response =
                    retrofitApiService.allCommentsAsync(AllCommentsRequest(userId)).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }

    suspend fun deleteComments(commentId: String): DataResource<String> {
        return safeApiCall(
            call = {
                val response =
                    retrofitApiService.deleteCommentAsync(DeleteCommentRequest(commentId)).await()
                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)
            }
        )
    }


    suspend fun myFavouritesWithAds(): DataResource<FavouritesWithAds> {
        return safeApiCall(
            call = {
                val response: ApiResponse<FavouritesWithAds>

                response = if (preferencesHelper.isLoggedIn) {
                    val userString = preferencesHelper.user
                    val user = ObjectConverter().getUser(userString!!)

                    retrofitApiService.myFavouriteAuthAsync(
                        if (user.token.contains(Constants.AUTHORIZATION_START))
                            user.token
                        else
                            "${Constants.AUTHORIZATION_START} ${user.token}"
                    ).await()

                } else {
                    retrofitApiService.myFavouriteAsync().await()
                }

                if (response.status)
                    DataResource.Success(response.data)
                else
                    DataResource.Error(response.msg)


            }
        )
    }
}