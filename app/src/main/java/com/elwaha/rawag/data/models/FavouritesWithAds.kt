package com.elwaha.rawag.data.models
import com.squareup.moshi.Json

data class FavouritesWithAds(
    @Json(name = "ads")
    val ads: List<AdModel>,
    @Json(name = "users")
    val users: List<UserModel>
)
