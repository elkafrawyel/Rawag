package com.elwaha.rawag.data.models
import com.squareup.moshi.Json

data class TermsModel(
    @field:Json(name = "rules")
    val rules: String
)