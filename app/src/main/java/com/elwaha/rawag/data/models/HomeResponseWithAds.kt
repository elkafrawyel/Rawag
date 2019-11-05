package com.elwaha.rawag.data.models

import com.squareup.moshi.Json

data class HomeResponseWithAds(
    @field:Json(name = "data")
    val homeCategoriesWithAds: HomeCategoriesWithAds,
    @field:Json(name = "status")
    val status: Boolean,
    @field:Json(name = "msg")
    val msg: String
)

data class HomeCategoriesWithAds(
    @field:Json(name = "ads")
    val ads: List<AdModel>,
    @field:Json(name = "categories")
    val categories: List<CategoryModel>
)

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
    val images: List<Image?>?,
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

data class Image(
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