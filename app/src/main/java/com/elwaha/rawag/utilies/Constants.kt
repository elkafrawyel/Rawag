package com.elwaha.rawag.utilies

object Constants {
    const val BASE_URL = "http://stbraq4it.com/rwaq/api/v1/"
    const val IMAGES_BASE_URL = "http://stbraq4it.com/rwaq/public/uploads/"
    const val AUTHORIZATION_START = "Bearer"

    enum class Language(val value: String) {
        ARABIC("ar"),
        ENGLISH("en")
    }
}