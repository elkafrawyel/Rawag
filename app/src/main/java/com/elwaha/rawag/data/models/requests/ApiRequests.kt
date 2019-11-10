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
    val about: String,
    val sub_category_id: String,
    val accepted: String
)

data class SubCategoriesRequest(
    val category_id: String
)

data class ProfileRequest(
    val user_id: String
)

data class AddCommentRequest(
    val comment: String,
    val ad_id: String
)

data class EditProfileRequest(
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val lang: String,
    val lat: String,
    val about: String,
    val sub_category_id: String
)

data class UpdatePasswordRequest(
    val old_password: String,
    val new_password: String,
    val confirmation_password: String
)

data class EditSocialRequest(
    var facebook: String = "",
    var twitter: String = "",
    var instagram: String = "",
    var snabchat: String = "",
    var youtube: String = ""
)