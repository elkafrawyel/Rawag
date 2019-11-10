package com.elwaha.rawag.ui.main.termsAndCondition

import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class TermsViewModel : AppViewModel() {
    var terms = ""

    init {
        getTerms()
    }

    private fun getTerms() {
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Success
            }
            scope.launch(dispatcherProvider.io) {
                when (val result =
                    Injector.getStaticRepo().getTerms()) {
                    is DataResource.Success -> {
                        terms = result.data.rules
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
