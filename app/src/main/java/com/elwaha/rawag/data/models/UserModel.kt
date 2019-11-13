package com.elwaha.rawag.data.models

import com.squareup.moshi.Json

data class UserModel(
    @field:Json(name = "about")
    val about: String,
    @field:Json(name = "address")
    val address: String,
    @field:Json(name = "avatar")
    val avatar: String,
    @field:Json(name = "sub_category_id")
    val subCategoryId: Int,
    @field:Json(name = "category_id")
    val categoryId: Int,
    @field:Json(name = "email")
    val email: String,
    @field:Json(name = "views")
    val views: Int,
    @field:Json(name = "facebook")
    val facebook: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "instagram")
    val instagram: String,
    @field:Json(name = "lang")
    val lang: String,
    @field:Json(name = "lat")
    val lat: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "phone")
    val phone: String,
    @field:Json(name = "reset_code")
    val resetCode: String,
    @field:Json(name = "snabchat")
    val snabchat: String,
    @field:Json(name = "status")
    val status: Int,
    @field:Json(name = "token")
    val token: String,
    @field:Json(name = "twitter")
    val twitter: String,
    @field:Json(name = "youtube")
    val youtube: String,
    @field:Json(name = "rate_value")
    val rate_value: Int,
    @field:Json(name = "isLiked")
    var isLiked: Int
)