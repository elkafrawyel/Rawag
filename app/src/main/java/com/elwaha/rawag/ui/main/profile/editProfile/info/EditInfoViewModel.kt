package com.elwaha.rawag.ui.main.profile.editProfile.info

import android.net.Uri
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.data.models.requests.EditProfileRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.*
import kotlinx.coroutines.launch

class EditInfoViewModel : AppViewModel() {
    var selectedCategory: CategoryModel? = null
    var selectedSubCategory: CategoryModel? = null
    var avatarUri: Uri? = null
    var lat: String? = null
    var lang: String? = null
    var address: String? = null

    fun editProfile(editProfileRequest: EditProfileRequest, uri: Uri?) {
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }

            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getUserRepo().editProfile(editProfileRequest, uri)) {
                    is DataResource.Success -> {
                        val user = result.data
                        val userString = ObjectConverter().saveUser(user)
                        Injector.getPreferenceHelper().user = userString
                        Injector.getPreferenceHelper().isLoggedIn = true
                        runOnMainThread { _uiState.value = ViewState.Success }
                    }
                    is DataResource.Error -> {
                        runOnMainThread {
                            _uiState.value = ViewState.Error(result.errorMessage)
                        }
                    }
                }
            }
        }
    }

    fun cancel() {
        if(job?.isActive!!){
            job!!.cancel()
        }
    }
}
