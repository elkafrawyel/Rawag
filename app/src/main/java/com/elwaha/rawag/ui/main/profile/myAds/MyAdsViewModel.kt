package com.elwaha.rawag.ui.main.profile.myAds

import com.elwaha.rawag.data.models.AdModel
import com.elwaha.rawag.data.models.requests.DeleteAdRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Event
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class MyAdsViewModel : AppViewModel() {
    val images = ArrayList<AdModel>()
    var userId: String? = null
    var deletedPosition = -1
    fun getMyAds() {
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }
            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getAdsRepo().myAds(userId!!)) {
                    is DataResource.Success -> {
                        if (result.data.isEmpty()) {
                            runOnMainThread {
                                _uiState.value = ViewState.Empty
                            }
                        } else {
                            images.clear()
                            images.addAll(result.data)
                            runOnMainThread {
                                _uiState.value = ViewState.Success
                            }
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

    fun delete(adId: String, position: Int) {
        checkNetworkEvent {
            runOnMainThread {
                _uiStateEvent.value = Event(ViewState.Loading)
            }
            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getAdsRepo().delete(DeleteAdRequest(adId))) {
                    is DataResource.Success -> {
                        deletedPosition = position
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
