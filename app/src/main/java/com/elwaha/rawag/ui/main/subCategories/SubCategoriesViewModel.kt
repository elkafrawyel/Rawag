package com.elwaha.rawag.ui.main.subCategories

import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class SubCategoriesViewModel : AppViewModel() {

    var categoryId: String? = null
    var subCategoriesList = ArrayList<CategoryModel>()

    fun getSubCategories() {
        checkNetwork {
            runOnMainThread {
                _uiState.value = ViewState.Loading
            }
            scope.launch(dispatcherProvider.io) {
                when (val result =
                    Injector.getCategoriesRepo().getSubCategories(categoryId!!)) {
                    is DataResource.Success -> {
                        if (result.data.isEmpty()) {
                            runOnMainThread {
                                _uiState.value = ViewState.Empty
                            }
                        } else {
                            subCategoriesList.clear()
                            subCategoriesList.addAll(result.data)
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

    fun refresh() {
        getSubCategories()
    }

}
