package com.example.aparshikhin.translation.presentation;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.example.aparshikhin.translation.model.data.Account;

import java.util.Collections;
import java.util.List;

public class SelectionState {

    @NonNull
    public static SelectionState create() {
        return new SelectionState(Optional.empty(), Collections.emptyList());
    }

    @NonNull
    private final Optional<Account> mAccount;
    @NonNull
    private final List<Account> mItems;

    private SelectionState(@NonNull final Optional<Account> choosedTargetAccount, @NonNull final List<Account> targetList) {
        mAccount = choosedTargetAccount;
        mItems = targetList;
    }

    @NonNull
    public Optional<Account> getAccount() {
        return mAccount;
    }

    @NonNull
    public List<Account> getItems() {
        return mItems;
    }

    @NonNull
    public Builder builder() {
        return new Builder(this);
    }

    public class Builder {

        @NonNull
        private Optional<Account> mChoosedTargetAccount;
        @NonNull
        private List<Account> mTargetList;

        private Builder(@NonNull final SelectionState state) {
            mChoosedTargetAccount = state.getAccount();
            mTargetList = state.getItems();
        }

        @NonNull
        public Builder setAccount(@NonNull final Optional<Account> choosedTargetAccount) {
            mChoosedTargetAccount = choosedTargetAccount;
            return this;
        }

        @NonNull
        public Builder setItems(@NonNull final List<Account> accounts) {
            mTargetList = accounts;
            return this;
        }

        @NonNull
        public SelectionState build() {
            return new SelectionState(mChoosedTargetAccount, mTargetList);
        }
    }

    @Override
    public String toString() {
        return "SelectionState{" +
                "mAccount=" + mAccount +
                ", mItems=" + mItems +
                '}';
    }
}
