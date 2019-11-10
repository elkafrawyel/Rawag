package com.elwaha.rawag.data.models

import com.squareup.moshi.Json

data class ImageModel(
    @Json(name = "ad_id")
    val adId: Int?,
    @Json(name = "created_at")
    val createdAt: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "img")
    val img: String?,
    @Json(name = "updated_at")
    val updatedAt: String?
)