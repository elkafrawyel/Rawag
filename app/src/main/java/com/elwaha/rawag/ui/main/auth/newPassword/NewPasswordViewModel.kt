package com.elwaha.rawag.ui.main.auth.newPassword

import com.elwaha.rawag.data.models.requests.UpdatePasswordRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class NewPasswordViewModel : AppViewModel() {

    fun changePassword(updatePasswordRequest: UpdatePasswordRequest){
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }

            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getUserRepo().updatePassword(updatePasswordRequest)) {
                    is DataResource.Success -> {
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
}
