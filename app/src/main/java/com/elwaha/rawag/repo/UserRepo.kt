package com.elwaha.rawag.repo

import android.net.Uri
import com.elwaha.rawag.R
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.data.models.requests.LoginRequest
import com.elwaha.rawag.data.models.requests.RegisterRequest
import com.elwaha.rawag.data.storage.remote.RetrofitApiService
import com.elwaha.rawag.utilies.*

class UserRepo(private val retrofitApiService: RetrofitApiService) {

    suspend fun login(loginRequest: LoginRequest): DataResource<UserModel> {
        return safeApiCall(
            call = {
                val response = retrofitApiService.loginAsync(loginRequest).await()
                if (response.status)
                    DataResource.Success(response.userModel!!)
                else
                    DataResource.Error(response.msg!!)
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
                    registerRequest.sub_category_id.toMultiPart(),
                    registerRequest.accepted.toMultiPart(),
                    uri.toMultiPart(Injector.getApplicationContext(), "avatar")
                ).await()

                if (response.status)
                    DataResource.Success(response.userModel!!)
                else
                    DataResource.Error(response.msg!!)
            }
        )
    }
}