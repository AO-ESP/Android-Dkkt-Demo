package ru.esm.tspiot.dkkt.service;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ServiceVersion implements Parcelable {
    @NonNull
    final private String mName;
    final private int mCode;

    public ServiceVersion(@NotNull String name, int code) {
        mName = name;
        mCode = code;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public int getCode() {
        return mCode;
    }

    @NotNull
    @Override
    public String toString() {
        return "ServiceVersion{" +
                "mName='" + mName + '\'' +
                ", mCode=" + mCode +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ServiceVersion that = (ServiceVersion) o;
        return mCode == that.mCode && Objects.equals(mName, that.mName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mName, mCode);
    }

    @Override
    public void writeToParcel(@NonNull @NotNull Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mCode);
    }

    public static final Creator<ServiceVersion> CREATOR = new Creator<>() {
        @Override
        public ServiceVersion createFromParcel(Parcel in) {
            return new ServiceVersion(Objects.requireNonNull(in.readString()), in.readInt());
        }

        @Override
        public ServiceVersion[] newArray(int size) {
            return new ServiceVersion[size];
        }
    };
}
