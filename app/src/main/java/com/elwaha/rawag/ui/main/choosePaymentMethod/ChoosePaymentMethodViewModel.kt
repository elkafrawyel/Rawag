package com.elwaha.rawag.ui.main.choosePaymentMethod

import com.elwaha.rawag.data.models.AdImages
import com.elwaha.rawag.data.models.AdModel
import com.elwaha.rawag.data.models.requests.AddAdRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Event
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class ChoosePaymentMethodViewModel : AppViewModel() {
    var paymentType: String? = null
    var adModel: AdModel? = null

    var subCategoryId = ""
    var cityId = ""
    var price = ""
    var baqaId = ""
    var days = ""
    var images: AdImages? = null

    fun uploadAd(addAdRequest: AddAdRequest, adImages: AdImages) {
        checkNetworkEvent {
            runOnMainThread {
                _uiStateEvent.value = Event(ViewState.Loading)
            }

            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getAdsRepo().uploadAds(addAdRequest, adImages)) {
                    is DataResource.Success -> {
                        adModel = result.data
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
