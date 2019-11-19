package com.elwaha.rawag.ui.main.profile.myAds.editAd

import android.net.Uri
import com.elwaha.rawag.data.models.*
import com.elwaha.rawag.data.models.requests.DeleteAdImageRequest
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
    var uriList = ArrayList<Uri>()
    var action: EditAdActions? = null
    var editAdRequest: EditAdRequest? = null
    var imageId: String? = null
    var deletedPosition = -1
    var adImages = ArrayList<SealedImageModel>()


    fun fireAction(editAdActions: EditAdActions) {
        checkNetworkEvent {
            action = editAdActions
            runOnMainThread {
                _uiStateEvent.value = Event(ViewState.Loading)
            }

            scope.launch(dispatcherProvider.io) {
                when (editAdActions) {
                    EditAdActions.EDIT_INFO -> {
                        when (val result = Injector.getAdsRepo().editInfo(editAdRequest!!)) {
                            is DataResource.Success -> {
                                runOnMainThread {
                                    _uiStateEvent.value = Event(ViewState.Success)
                                }
                            }
                            is DataResource.Error -> {
                                runOnMainThread {
                                    _uiStateEvent.value =
                                        Event(ViewState.Error(result.errorMessage))
                                }
                            }
                        }
                    }
                    EditAdActions.DELETE_IMAGE -> {
                        when (val result =
                            Injector.getAdsRepo().deleteAdImage(DeleteAdImageRequest(imageId!!))) {
                            is DataResource.Success -> {
                                runOnMainThread {
                                    _uiStateEvent.value = Event(ViewState.Success)
                                }
                            }
                            is DataResource.Error -> {
                                runOnMainThread {
                                    _uiStateEvent.value =
                                        Event(ViewState.Error(result.errorMessage))
                                }
                            }
                        }
                    }
                    EditAdActions.SAVE -> {
                        val adImages = AdImages(uriList.map {
                            it.toString()
                        })
                        when (val result = Injector.getAdsRepo().addImagesToAd(
                            ad!!.id.toString(), adImages
                        )) {
                            is DataResource.Success -> {
                                runOnMainThread {
                                    _uiStateEvent.value = Event(ViewState.Success)
                                }
                            }
                            is DataResource.Error -> {
                                runOnMainThread {
                                    _uiStateEvent.value =
                                        Event(ViewState.Error(result.errorMessage))
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    enum class EditAdActions(val value: String) {
        EDIT_INFO("editInfo"),
        DELETE_IMAGE("deleteImage"),
        SAVE("save")
    }
}
