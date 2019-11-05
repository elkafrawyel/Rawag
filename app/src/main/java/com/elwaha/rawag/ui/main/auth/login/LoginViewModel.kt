package com.elwaha.rawag.ui.main.auth.login

import androidx.lifecycle.viewModelScope
import com.elwaha.rawag.data.models.requests.LoginRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.*
import kotlinx.coroutines.launch

class LoginViewModel : AppViewModel() {

    fun login(email: String, password: String) {
        checkNetworkEvent {
            runOnMainThread {
                _uiStateEvent.value = Event(ViewState.Loading)
            }

            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getUserRepo().login(LoginRequest(email, password))) {
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
