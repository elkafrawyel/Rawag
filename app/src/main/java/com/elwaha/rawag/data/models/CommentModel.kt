package com.elwaha.rawag.data.models

import com.squareup.moshi.Json

data class CommentModel(
    @field:Json(name = "comment")
    val comment: String?,
    @field:Json(name = "created_at")
    val createdAt: String?,
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "rateable_id")
    val rateableId: Int?,
    @field:Json(name = "updated_at")
    val updatedAt: String?,
    @field:Json(name = "user_id")
    val userId: Int?,
    @field:Json(name = "user_name")
    val user_name: String?,
    @field:Json(name = "user_img")
    val user_img: String?,
    @field:Json(name = "value")
    val value: Int?
)