package com.elwaha.rawag.ui.main.mainFragment.home.filter.countries

import com.elwaha.rawag.data.models.CountryModel
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class CountriesViewModel : AppViewModel() {
    var countriesList = ArrayList<CountryModel>()

    init {
        getCountries()
    }
    fun getCountries(){
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }

            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getStaticRepo().getCountries()) {
                    is DataResource.Success -> {
                        countriesList.clear()
                        countriesList.addAll(result.data)
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
