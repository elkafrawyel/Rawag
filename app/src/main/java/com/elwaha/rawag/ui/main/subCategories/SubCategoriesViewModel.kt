package com.elwaha.rawag.ui.main.subCategories

import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.ViewState

class SubCategoriesViewModel : AppViewModel() {

    var categoryId: String? = null
        set(value) {
            getSubCategories(value)
        }

    private fun getSubCategories(categoryId: String?) {
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Success
            }
        }
    }

    fun refresh() {
        getSubCategories(categoryId)
    }

}
