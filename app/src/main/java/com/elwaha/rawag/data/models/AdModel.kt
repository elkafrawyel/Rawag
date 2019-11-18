package com.elwaha.rawag.data.models

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class AdModel(
    @field:Json(name = "baqa_id")
    val baqaId: Int?,
    @field:Json(name = "sub_category_id")
    val sub_categoryId: Int?,
    @field:Json(name = "city_id")
    val cityId: Int?,
    @field:Json(name = "created_at")
    val createdAt: String?,
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "images")
    val imageModels: List<ImageModel?>?,
    @field:Json(name = "img")
    val img: String?,
    @field:Json(name = "payment_type")
    val paymentType: String?,
    @field:Json(name = "updated_at")
    val updatedAt: String?,
    @field:Json(name = "user_id")
    val userId: Int?,
    @field:Json(name = "views")
    val views: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(ImageModel),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(baqaId)
        parcel.writeValue(sub_categoryId)
        parcel.writeValue(cityId)
        parcel.writeString(createdAt)
        parcel.writeValue(id)
        parcel.writeTypedList(imageModels)
        parcel.writeString(img)
        parcel.writeString(paymentType)
        parcel.writeString(updatedAt)
        parcel.writeValue(userId)
        parcel.writeValue(views)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdModel> {
        override fun createFromParcel(parcel: Parcel): AdModel {
            return AdModel(parcel)
        }

        override fun newArray(size: Int): Array<AdModel?> {
            return arrayOfNulls(size)
        }
    }
}

