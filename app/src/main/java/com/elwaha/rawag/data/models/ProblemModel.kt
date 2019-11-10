package com.elwaha.rawag.data.models

import com.squareup.moshi.Json

data class ProblemModel(
    @field:Json(name = "created_at")
    val createdAt: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "report")
    val report: String,
    @field:Json(name = "updated_at")
    val updatedAt: String
)