package com.elwaha.rawag.ui.main.mainFragment.favourites

import com.elwaha.rawag.data.models.AdModel
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.data.models.requests.AddLikeRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Event
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class FavouritesViewModel : AppViewModel() {
    val ads = ArrayList<AdModel>()
    val usersList = ArrayList<UserModel>()
    var lastLikeActionResult: Boolean? = null
    var lastLikeActionPosition: Int? = null
    fun get() {
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }
            scope.launch(dispatcherProvider.io) {
                when (val result = Injector.getUserRepo().myFavouritesWithAds()) {
                    is DataResource.Success -> {
                        usersList.clear()
                        usersList.addAll(result.data.users)
                        ads.clear()
                        ads.addAll(result.data.ads)
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

    fun like(userId: String, position: Int) {
        checkNetworkEvent {
            runOnMainThread { _uiStateEvent.value = Event(ViewState.Loading) }
            scope.launch(dispatcherProvider.io) {
                when (val result =
                    Injector.getUserRepo().addLike(AddLikeRequest(userId))) {
                    is DataResource.Success -> {
                        lastLikeActionPosition = position
                        lastLikeActionResult = result.data == 1
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
