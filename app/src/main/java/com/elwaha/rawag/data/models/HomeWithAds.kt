package com.elwaha.rawag.data.models

import com.squareup.moshi.Json

data class HomeWithAds(
    @field:Json(name = "ads")
    val ads: List<AdModel>,
    @field:Json(name = "categories")
    val categories: List<CategoryModel>
)