package com.elwaha.rawag.ui.main.aboutUs

import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class AboutUsViewModel : AppViewModel() {
    var about = ""

    init {
        getAbout()
    }

    private fun getAbout() {
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Success
            }
            scope.launch(dispatcherProvider.io) {
                when (val result =
                    Injector.getStaticRepo().getAbout()) {
                    is DataResource.Success -> {
                        about = result.data.aboutus
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
