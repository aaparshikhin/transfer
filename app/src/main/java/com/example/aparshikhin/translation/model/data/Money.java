package com.example.aparshikhin.translation.model.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Money implements Parcelable {

    @Nullable
    private BigDecimal mAmount;
    @Nullable
    private Currency mCurrency;

    public Money(@NonNull BigDecimal amount, @NonNull Currency currency) {
        mAmount = amount;
        mCurrency = currency;
    }

    protected Money(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Money> CREATOR = new Creator<Money>() {
        @Override
        public Money createFromParcel(Parcel in) {
            return new Money(in);
        }

        @Override
        public Money[] newArray(int size) {
            return new Money[size];
        }
    };

    @NonNull
    public BigDecimal getAmount() {
        return mAmount;
    }

    @NonNull
    public Currency getCurrency() {
        return mCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(mAmount, money.mAmount) &&
                mCurrency == money.mCurrency;
    }

    @Override
    public int hashCode() {

        return Objects.hash(mAmount, mCurrency);
    }

    @Override
    public String toString() {
        return "Money{" +
                "mAmount=" + mAmount +
                ", mCurrency=" + mCurrency +
                '}';
    }
}
