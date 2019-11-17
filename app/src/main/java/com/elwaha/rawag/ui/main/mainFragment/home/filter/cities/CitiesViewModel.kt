package com.elwaha.rawag.ui.main.mainFragment.home.filter.cities

import com.elwaha.rawag.data.models.CityModel
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class CitiesViewModel : AppViewModel() {
    var cityId: String? = null
    var countryId: String? = null
    var citiesList = ArrayList<CityModel>()

    fun getCities() {
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }

            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getStaticRepo().getCities(countryId!!)) {
                    is DataResource.Success -> {
                        citiesList.clear()
                        citiesList.addAll(result.data)
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
