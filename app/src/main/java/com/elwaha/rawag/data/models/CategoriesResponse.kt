package com.elwaha.rawag.data.models

import com.elwaha.rawag.utilies.Constants
import com.elwaha.rawag.utilies.Injector
import com.squareup.moshi.Json

data class CategoriesResponse(
    @field:Json(name = "data")
    val categories: List<CategoryModel>,
    @field:Json(name = "status")
    val status: Boolean,
    @field:Json(name = "msg")
    val msg: String?
)

data class CategoryModel(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "img")
    val img: String,
    @field:Json(name = "name_ar")
    val name_ar: String,
    @field:Json(name = "name_en")
    val name_en: String,
    @field:Json(name = "created_at")
    val created_at: String,
    @field:Json(name = "update_at")
    val updated_at: String
) {
    override fun toString(): String {
        return if (Injector.getPreferenceHelper().language == Constants.Language.ARABIC.value)
            name_ar
        else
            name_en
    }
}