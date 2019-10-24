package com.elwaha.rawag.ui.main.subCategories

import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.ViewState

class SubCategoriesViewModel  : AppViewModel() {

    fun getSubCategories() {
        checkNetwork {
           runOnMainThread {
               _uiState.value = ViewState.Empty
           }
        }
    }

    fun refresh() {
        getSubCategories()
    }
}
