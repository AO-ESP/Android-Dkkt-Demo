package ru.esm.tspiot.dkkt.service

import android.os.Parcel
import android.os.Parcelable

data class ControlledEncryptionResult(
    val encryptedData: ByteArray,
    val statusCode: Int,
    val statusMessage: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createByteArray() ?: byteArrayOf(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByteArray(encryptedData)
        parcel.writeInt(statusCode)
        parcel.writeString(statusMessage)
    }

    override fun describeContents(): Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ControlledEncryptionResult

        if (!encryptedData.contentEquals(other.encryptedData)) return false
        if (statusCode != other.statusCode) return false
        if (statusMessage != other.statusMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = encryptedData.contentHashCode()
        result = 31 * result + statusCode
        result = 31 * result + statusMessage.hashCode()
        return result
    }

    companion object CREATOR : Parcelable.Creator<ControlledEncryptionResult> {
        override fun createFromParcel(parcel: Parcel): ControlledEncryptionResult =
            ControlledEncryptionResult(parcel)

        override fun newArray(size: Int): Array<ControlledEncryptionResult?> =
            arrayOfNulls(size)
    }
}