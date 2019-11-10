package com.elwaha.rawag.data.models

import com.squareup.moshi.Json

data class AdModel(
    @field:Json(name = "baqa_id")
    val baqaId: Int?,
    @field:Json(name = "category_id")
    val categoryId: Int?,
    @field:Json(name = "city_id")
    val cityId: Int?,
    @field:Json(name = "created_at")
    val createdAt: String?,
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "images")
    val imageModels: List<ImageModel?>?,
    @field:Json(name = "img")
    val img: String?,
    @field:Json(name = "payment_type")
    val paymentType: String?,
    @field:Json(name = "updated_at")
    val updatedAt: String?,
    @field:Json(name = "user_id")
    val userId: Int?,
    @field:Json(name = "views")
    val views: Int?
)

