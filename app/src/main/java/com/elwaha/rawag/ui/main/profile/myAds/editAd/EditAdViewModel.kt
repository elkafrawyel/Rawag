package com.elwaha.rawag.ui.main.profile.myAds.editAd

import com.elwaha.rawag.data.models.AdModel
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.data.models.CityModel
import com.elwaha.rawag.data.models.CountryModel
import com.elwaha.rawag.data.models.requests.EditAdRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Event
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class EditAdViewModel : AppViewModel() {
    var selectedCategory: CategoryModel? = null
    var selectedSubCategory: CategoryModel? = null
    var selectedCountry: CountryModel? = null
    var selectedCity: CityModel? = null
    var ad: AdModel? = null
    fun editInfo(editAdRequest: EditAdRequest) {
        checkNetworkEvent {
            runOnMainThread {
                _uiStateEvent.value = Event(ViewState.Loading)
            }

            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getAdsRepo().editInfo(editAdRequest)) {
                    is DataResource.Success -> {
                        runOnMainThread {
                            _uiStateEvent.value = Event(ViewState.Success)
                        }
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
