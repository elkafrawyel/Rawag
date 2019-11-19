package com.elwaha.rawag.data.models

import android.os.Parcel
import android.os.Parcelable

data class AdImages(
    var images: List<String>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createStringArrayList())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdImages> {
        override fun createFromParcel(parcel: Parcel): AdImages {
            return AdImages(parcel)
        }

        override fun newArray(size: Int): Array<AdImages?> {
            return arrayOfNulls(size)
        }
    }
}