package com.example.aparshikhin.translation.model.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Account implements Parcelable {

    @NonNull
    private final String mName;
    private final int mId;
    @NonNull
    private final Money mBalance;
    private final boolean isCreditCardAccount;

    public Account(@NonNull String name, int id, @NonNull Money balance, final boolean canComission) {
        mName = name;
        mId = id;
        mBalance = balance;
        this.isCreditCardAccount = canComission;
    }

    protected Account(Parcel in) {
        mName = in.readString();
        mId = in.readInt();
        mBalance = in.readParcelable(Money.class.getClassLoader());
        isCreditCardAccount = in.readByte() != 0;
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    @NonNull
    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public Money getBalance() {
        return mBalance;
    }

    public boolean isCreditCardAccount() {
        return isCreditCardAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return mId == account.mId &&
                isCreditCardAccount == account.isCreditCardAccount &&
                Objects.equals(mName, account.mName) &&
                Objects.equals(mBalance, account.mBalance);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mName, mId, mBalance, isCreditCardAccount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeInt(mId);
        parcel.writeParcelable(mBalance, i);
        parcel.writeByte((byte) (isCreditCardAccount ? 1 : 0));
    }
}
