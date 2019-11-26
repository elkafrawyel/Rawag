package com.elwaha.rawag.ui.main.settings

import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class SettingsViewModel : AppViewModel() {

    var status = false
    fun setNotificationState() {
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }

            scope.launch(dispatcherProvider.io) {
                when(val result = Injector.getUserRepo().notificationStatus()){
                    is DataResource.Success -> {
                        status = result.data
                        runOnMainThread {
                            _uiState.value = ViewState.Success
                        }
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
