package com.example.aparshikhin.translation.model.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Objects;

public class Transfer implements Parcelable {

    private final int mSourceAccountId;
    private final int mTargetAccountId;
    @NonNull
    private final Money mAmount;

    public Transfer(int sourceAccountId, int targetAccountId, @NonNull Money amount) {
        mSourceAccountId = sourceAccountId;
        mTargetAccountId = targetAccountId;
        mAmount = amount;
    }

    protected Transfer(Parcel in) {
        mSourceAccountId = in.readInt();
        mTargetAccountId = in.readInt();
        mAmount = in.readParcelable(Money.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mSourceAccountId);
        dest.writeInt(mTargetAccountId);
        dest.writeParcelable(mAmount, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Transfer> CREATOR = new Creator<Transfer>() {
        @Override
        public Transfer createFromParcel(Parcel in) {
            return new Transfer(in);
        }

        @Override
        public Transfer[] newArray(int size) {
            return new Transfer[size];
        }
    };

    public int getSourceAccountId() {
        return mSourceAccountId;
    }

    public int getTargetAccountId() {
        return mTargetAccountId;
    }

    @NonNull
    public Money getAmount() {
        return mAmount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return mSourceAccountId == transfer.mSourceAccountId &&
                mTargetAccountId == transfer.mTargetAccountId &&
                Objects.equals(mAmount, transfer.mAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mSourceAccountId, mTargetAccountId, mAmount);
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "mSourceAccountId=" + mSourceAccountId +
                ", mTargetAccountId=" + mTargetAccountId +
                ", amount=" + mAmount +
                '}';
    }
}
