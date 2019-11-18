package com.elwaha.rawag.data.models

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class ImageModel(
    @Json(name = "ad_id")
    val adId: Int?,
    @Json(name = "created_at")
    val createdAt: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "img")
    val img: String?,
    @Json(name = "updated_at")
    val updatedAt: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(adId)
        parcel.writeString(createdAt)
        parcel.writeValue(id)
        parcel.writeString(img)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageModel> {
        override fun createFromParcel(parcel: Parcel): ImageModel {
            return ImageModel(parcel)
        }

        override fun newArray(size: Int): Array<ImageModel?> {
            return arrayOfNulls(size)
        }
    }
}