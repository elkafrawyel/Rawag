package com.elwaha.rawag.data.models

import com.squareup.moshi.Json

data class BaqaModel(
    @field:Json(name = "content")
    val content: String,
    @field:Json(name = "created_at")
    val createdAt: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name_ar")
    val nameAr: String,
    @field:Json(name = "name_en")
    val nameEn: String,
    @field:Json(name = "price")
    val price: Int,
    @field:Json(name = "updated_at")
    val updatedAt: String
)