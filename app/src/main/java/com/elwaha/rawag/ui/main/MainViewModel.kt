package com.elwaha.rawag.ui.main

import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.data.models.CityModel
import com.elwaha.rawag.data.models.CountryModel
import com.elwaha.rawag.ui.AppViewModel
import com.elwaha.rawag.utilies.DataResource
import com.elwaha.rawag.utilies.Injector
import com.elwaha.rawag.utilies.ViewState
import kotlinx.coroutines.launch

class MainViewModel : AppViewModel() {

    val fakeId = 999
    private val fakeCategory =
        CategoryModel(fakeId, "", "الاقسام الرئيسية", "Categories", "", "")

    private val fakeSubCategory =
        CategoryModel(fakeId, "", "الاقسام الفرعية", "SubCategories", "", "")

    private val fakeCountry =
        CountryModel(fakeId, "الدول", "Countries", "", "")

    private val fakeCity =
        CityModel(fakeId, "المدن", "Cities", 999, "", "")

    var categoriesList = ArrayList<CategoryModel>()
    var subCategoriesList = ArrayList<CategoryModel>()
    var countriesList = ArrayList<CountryModel>()
    var citiesList = ArrayList<CityModel>()

    var countryId: String? = null
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
                    MainActions.COUNTRIES -> {
                        when (val result = Injector.getStaticRepo().getCountries()) {
                            is DataResource.Success -> {
                                countriesList.clear()
                                countriesList.add(fakeCountry)
                                countriesList.addAll(result.data)

                                citiesList.clear()
                                citiesList.add(fakeCity)
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
                    MainActions.CITIES -> {
                        when (val result =
                            Injector.getStaticRepo().getCities(countryId!!)) {
                            is DataResource.Success -> {
                                citiesList.clear()
                                citiesList.add(fakeCity)
                                citiesList.addAll(result.data)
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
    }

    enum class MainActions(val value: String) {
        CATEGORIES("categories"),
        SUB_CATEGORIES("sub_categories"),
        COUNTRIES("countriesList"),
        CITIES("cities")
    }

}