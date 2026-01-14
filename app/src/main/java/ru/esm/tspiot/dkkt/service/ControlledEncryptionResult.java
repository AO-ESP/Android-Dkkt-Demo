package ru.esm.tspiot.dkkt.service;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class ControlledEncryptionResult implements Parcelable {
    private final byte[] mData;

    public ControlledEncryptionResult(byte[] mData) {
        this.mData = mData;
    }

    public byte[] getData() {
        return mData;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ControlledEncryptionResult that = (ControlledEncryptionResult) o;
        return Objects.deepEquals(mData, that.mData);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull @NotNull Parcel dest, int flags) {
        dest.writeByteArray(mData);
    }

    public static final Creator<ControlledEncryptionResult> CREATOR = new Creator<>() {
        @Override
        public ControlledEncryptionResult createFromParcel(Parcel in) {
            return new ControlledEncryptionResult(in.createByteArray());
        }

        @Override
        public ControlledEncryptionResult[] newArray(int size) {
            return new ControlledEncryptionResult[size];
        }
    };
}
