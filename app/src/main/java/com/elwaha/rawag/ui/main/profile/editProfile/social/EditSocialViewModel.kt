package com.elwaha.rawag.ui.main.profile.editProfile.social

import com.elwaha.rawag.data.models.requests.EditSocialRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.*
import kotlinx.coroutines.launch

class EditSocialViewModel : AppViewModel() {
    fun edit(editSocialRequest: EditSocialRequest) {
        checkNetworkEvent {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }

            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getUserRepo().editSocial(editSocialRequest)) {
                    is DataResource.Success -> {
                        val user = result.data
                        val userString = ObjectConverter().saveUser(user)
                        Injector.getPreferenceHelper().user = userString
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
