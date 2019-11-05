package com.elwaha.rawag.ui.main

import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class MainViewModel : AppViewModel() {

    val fakeCategoryId = 999
    private val fakeCategory =
        CategoryModel(fakeCategoryId, "", "الاقسام الرئيسية", "Categories", "", "")

    private val fakeSubCategory =
        CategoryModel(fakeCategoryId, "", "الاقسام الفرعية", "SubCategories", "", "")

    var categoriesList = ArrayList<CategoryModel>()
    var subCategoriesList = ArrayList<CategoryModel>()
    var categoryId: String? = null
    var action: MainActions? = null

    fun get(mainAction: MainActions) {
        checkNetwork {
            action = mainAction
            runOnMainThread {
                _uiState.value = ViewState.Success
            }
            scope.launch(dispatcherProvider.io) {
                when (action) {
                    MainActions.CATEGORIES -> {
                        when (val result = Injector.getCategoriesRepo().getCategories()) {
                            is DataResource.Success -> {
                                categoriesList.clear()
                                categoriesList.add(fakeCategory)
                                categoriesList.addAll(result.data)

                                subCategoriesList.clear()
                                subCategoriesList.add(fakeSubCategory)
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
                    MainActions.SUB_CATEGORIES -> {
                        when (val result =
                            Injector.getCategoriesRepo().getSubCategories(categoryId!!)) {
                            is DataResource.Success -> {
                                subCategoriesList.clear()
                                subCategoriesList.add(fakeSubCategory)
                                subCategoriesList.addAll(result.data)
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

    enum class MainActions(val value: String) {
        CATEGORIES("categories"),
        SUB_CATEGORIES("sub_categories")
    }

}