package com.elwaha.rawag.data.models

import com.squareup.moshi.Json

class ApiResponse<T>(
    @field:Json(name = "data")
    val data: T,
    @field:Json(name = "status")
    val status: Boolean,
    @field:Json(name = "msg")
    val msg: String
)

class ApiResponseNoData(
    @field:Json(name = "status")
    val status: Boolean,
    @field:Json(name = "msg")
    val msg: String
)