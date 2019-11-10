package com.elwaha.rawag.repo

import android.net.Uri
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.ApiResponse
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.data.models.requests.*
import com.elwaha.rawag.data.storage.local.PreferencesHelper
import com.elwaha.rawag.data.storage.remote.RetrofitApiService
import com.elwaha.rawag.utilies.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
                val response = retrofitApiService.profileAsync(profileRequest).await()
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

    suspend fun updatePassword(updatePasswordRequest: UpdatePasswordRequest): DataResource<Any> {
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
}