package com.elwaha.rawag.ui.main.choosePackage

import com.elwaha.rawag.data.models.BaqaModel
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class ChoosePackageViewModel : AppViewModel() {


    var allBaqas = ArrayList<BaqaModel>()

    init {
        getAllBaqas()
    }
    fun getAllBaqas() {
        checkNetworkEvent {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }

            scope.launch(dispatcherProvider.io) {
                when(val result = Injector.getStaticRepo().getAllBaqas()){
                    is DataResource.Success -> {
                        allBaqas.clear()
                        allBaqas.addAll(result.data)
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
