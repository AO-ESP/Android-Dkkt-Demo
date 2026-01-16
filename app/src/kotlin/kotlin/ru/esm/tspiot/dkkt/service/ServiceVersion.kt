package ru.esm.tspiot.dkkt.service

import android.os.Parcel
import android.os.Parcelable

data class ServiceVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val build: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(major)
        parcel.writeInt(minor)
        parcel.writeInt(patch)
        parcel.writeString(build)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ServiceVersion> {
        override fun createFromParcel(parcel: Parcel): ServiceVersion =
            ServiceVersion(parcel)

        override fun newArray(size: Int): Array<ServiceVersion?> =
            arrayOfNulls(size)
    }
}