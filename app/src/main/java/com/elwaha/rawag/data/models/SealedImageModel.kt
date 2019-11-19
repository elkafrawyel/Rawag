package com.elwaha.rawag.data.models

import android.net.Uri

sealed class SealedImageModel {
    data class StringSealedImage(var image: String) : SealedImageModel()
    data class UriSealedImage(var image: Uri) : SealedImageModel()
}