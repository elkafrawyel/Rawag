package com.elwaha.rawag.data.models
import com.squareup.moshi.Json


data class AboutModel(
    @field:Json(name = "aboutus")
    val aboutus: String
)