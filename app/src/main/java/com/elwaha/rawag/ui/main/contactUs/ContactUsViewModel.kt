package com.elwaha.rawag.ui.main.contactUs

import com.elwaha.rawag.data.models.requests.AddContactRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class ContactUsViewModel : AppViewModel() {
    fun contactUs(addContactRequest: AddContactRequest){
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }
            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getUserRepo().addContact(addContactRequest)) {
                    is DataResource.Success -> {
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
