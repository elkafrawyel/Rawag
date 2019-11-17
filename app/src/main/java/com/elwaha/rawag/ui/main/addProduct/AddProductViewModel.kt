package com.elwaha.rawag.ui.main.addProduct

import android.net.Uri
import com.elwaha.rawag.data.models.CategoryModel
import com.elwaha.rawag.data.models.CityModel
import com.elwaha.rawag.data.models.CountryModel
import com.elwaha.rawag.ui.AppViewModel

class AddProductViewModel : AppViewModel() {

    var uriList = ArrayList<Uri>()
    var baqaId: String? = null
    var price: String? = null

    var selectedCategory: CategoryModel? = null
    var selectedSubCategory: CategoryModel? = null
    var selectedCountry: CountryModel? = null
    var selectedCity: CityModel? = null
    val days = ArrayList<String>()
    var selectedDays: String? = null

    init {
        days.add("1")
        days.add("2")
        days.add("3")
        days.add("4")
        days.add("5")
    }
}
