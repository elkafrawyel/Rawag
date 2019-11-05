package com.elwaha.rawag.data.models.requests

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val address: String,
    val lang: String,
    val lat: String,
    val sub_category_id: String,
    val accepted: String
)

data class SubCategoriesRequest(
    val category_id:String
)