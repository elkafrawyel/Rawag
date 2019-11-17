package com.elwaha.rawag.data.models

import com.elwaha.rawag.utilies.Constants
import com.elwaha.rawag.utilies.Injector
import com.squareup.moshi.Json

data class CountryModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name_ar")
    val nameAr: String,
    @field:Json(name = "name_en")
    val nameEn: String,
    @field:Json(name = "img")
    val img: String = "",
    @field:Json(name = "updated_at")
    val updatedAt: String,
    @field:Json(name = "created_at")
    val createdAt: String
) {
    override fun toString(): String {
        return if (Injector.getPreferenceHelper().language == Constants.Language.ARABIC.value)
            nameAr
        else
            nameAr
    }
}