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
    val user_id: String,
    val value:String
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

data class AddAdRequest(
    val sub_category_id: String,
    val city_id: String,
    val price: String,
    val baqa_id: String,
    val payment_type: String,
    val days: String
)

data class CitiesRequest(
    val country_id: String
)

data class AddContactRequest(
    val email: String,
    val message: String,
    val name: String,
    val phone: String
)

data class AllCommentsRequest(
    val user_id: String
)

data class AddLikeRequest(
    val profile_id: String
)

data class AddReportRequest(
    val comment_id: String,
    val problem_id: String
)

data class UsersRequest(
    val sub_category_id: String?,
    val city_id: String?
)

data class DeleteCommentRequest(
    val comment_id: String
)

data class DeleteAdRequest(
    val ad_id: String
)

data class SearchRequest(
    val name: String
)

data class DeleteAdImageRequest(
    val ad_image_id: String
)

data class EditAdRequest(
    val sub_category_id: String,
    val city_id: String,
    val ad_id: String
)