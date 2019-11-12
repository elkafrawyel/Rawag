package com.elwaha.rawag.ui.main.profile

import com.elwaha.rawag.data.models.AdModel
import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.data.models.requests.ProfileRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class ProfileViewModel : AppViewModel() {
    val images = ArrayList<AdModel>()
    var action: ProfileActions? = null
    var userId: String? = null
    var user: UserModel? = null

    fun get(profileActions: ProfileActions) {
        checkNetwork {
            action = profileActions
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }
            scope.launch(dispatcherProvider.io) {
                when (action) {
                    ProfileActions.GET_ADS -> {
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
                    ProfileActions.GET_PROFILE -> {
                        when (val result =
                            Injector.getUserRepo().profile(ProfileRequest(userId!!))) {
                            is DataResource.Success -> {
                                user = result.data
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
                    null -> {

                    }
                }
            }
        }
    }

    enum class ProfileActions(val value: String) {
        GET_PROFILE("getProfile"),
        GET_ADS("getMyAds")
    }
}
