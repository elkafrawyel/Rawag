package com.elwaha.rawag.ui.main.mainFragment.home

import androidx.lifecycle.ViewModel
import com.elwaha.rawag.data.models.AdModel
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class HomeViewModel : AppViewModel() {
    val ads: ArrayList<AdModel> = arrayListOf()
    val categories: ArrayList<CategoryModel> = arrayListOf()

    fun get(){
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }
            scope.launch(dispatcherProvider.io) {
                when(val result = Injector.getCategoriesRepo().getCategoriesWithAds()){
                    is DataResource.Success -> {
                        categories.clear()
                        categories.addAll(result.data.categories)
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
}
