package com.elwaha.rawag.data.models

import com.squareup.moshi.Json

data class CommentModel(
    @Json(name = "ad_id")
    val adId: Int?,
    @Json(name = "comment")
    val comment: String?,
    @Json(name = "created_at")
    val createdAt: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "updated_at")
    val updatedAt: String?,
    @Json(name = "user_id")
    val userId: Int?
)