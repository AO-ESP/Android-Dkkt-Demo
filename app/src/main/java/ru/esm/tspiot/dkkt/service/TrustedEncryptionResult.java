package ru.esm.tspiot.dkkt.service;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class TrustedEncryptionResult implements Parcelable {
    private final byte[] mControl;
    private final byte[] mData;

    public TrustedEncryptionResult(byte[] mControl, byte[] mData) {
        this.mControl = mControl;
        this.mData = mData;
    }

    public byte[] getControl() {
        return mControl;
    }

    public byte[] getData() {
        return mData;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TrustedEncryptionResult that = (TrustedEncryptionResult) o;
        return Objects.deepEquals(mControl, that.mControl) && Objects.deepEquals(mData, that.mData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(mControl), Arrays.hashCode(mData));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull @NotNull Parcel dest, int flags) {
        dest.writeByteArray(mControl);
        dest.writeByteArray(mData);
    }

    public static final Creator<TrustedEncryptionResult> CREATOR = new Creator<>() {
        @Override
        public TrustedEncryptionResult createFromParcel(Parcel in) {
            return new TrustedEncryptionResult(in.createByteArray(), in.createByteArray());
        }

        @Override
        public TrustedEncryptionResult[] newArray(int size) {
            return new TrustedEncryptionResult[size];
        }
    };
}
