package com.elwaha.rawag.ui.main.profiles

import com.elwaha.rawag.data.models.UserModel
import com.elwaha.rawag.data.models.requests.AddLikeRequest
import com.elwaha.rawag.data.models.requests.UsersRequest
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Event
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch
import java.text.FieldPosition

class ProfilesViewModel : AppViewModel() {

    var subCategoryId: String? = null
    var usersList: ArrayList<UserModel> = arrayListOf()
    var lastLikeActionResult: Boolean? = null
    var lastLikeActionPosition: Int? = null
    fun getUsers() {
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }

            scope.launch(dispatcherProvider.io) {
                when (val result =
                    Injector.getCategoriesRepo().getUsers(UsersRequest(subCategoryId!!))) {
                    is DataResource.Success -> {
                        if (result.data.isEmpty()) {
                            runOnMainThread {
                                _uiState.value = ViewState.Empty
                            }
                        } else {
                            usersList.clear()
                            usersList.addAll(result.data)
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

    fun like(userId: String,position: Int) {
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
