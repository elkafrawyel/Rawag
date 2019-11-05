package com.elwaha.rawag.ui.main.auth.register

import android.net.Uri
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.data.models.requests.RegisterRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.*
import kotlinx.coroutines.launch

class RegisterViewModel : AppViewModel() {
    var selectedCategory: CategoryModel? = null
    var selectedSubCategory: CategoryModel? = null
    var avatarUri: Uri? = null
    var lat: String? = null
    var lang: String? = null
    var address: String? = null

    fun register(registerRequest: RegisterRequest, uri: Uri) {
        checkNetworkEvent {
            runOnMainThread {
                _uiStateEvent.value = Event(ViewState.Loading)
            }
            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getUserRepo().register(registerRequest, uri)) {
                    is DataResource.Success -> {
                        val user = result.data
                        val userString = ObjectConverter().saveUser(user)
                        Injector.getPreferenceHelper().user = userString
                        Injector.getPreferenceHelper().isLoggedIn = true
                        runOnMainThread { _uiStateEvent.value = Event(ViewState.Success) }
                    }
                    is DataResource.Error -> {
                        runOnMainThread {
                            _uiStateEvent.value = Event(ViewState.Error(result.errorMessage))
                        }
                    }
                }

            }
        }
    }
}
