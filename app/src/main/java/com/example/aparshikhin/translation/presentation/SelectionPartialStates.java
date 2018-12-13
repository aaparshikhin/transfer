package com.example.aparshikhin.translation.presentation;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.example.aparshikhin.translation.model.data.Account;

import java.util.List;

import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public interface SelectionPartialStates extends Function<SelectionState, SelectionState> {

    class WithBiSelector implements SelectionPartialStates {

        @NonNull
        private final List<Account> mAccounts;
        @NonNull
        private final Optional<Account> mSource;
        @NonNull
        private final BiFunction<List<Account>, Optional<Account>, Optional<Account>> mSelector;

        public WithBiSelector(@NonNull final List<Account> targetAccounts,
                              @NonNull final Optional<Account> sourceAccount,
                              @NonNull final BiFunction<List<Account>, Optional<Account>, Optional<Account>> func) {

            mAccounts = targetAccounts;
            mSource = sourceAccount;
            mSelector = func;
        }

        @NonNull
        @Override
        public SelectionState apply(@NonNull final SelectionState previous) throws Exception {
            final Optional<Account> account = mSelector.apply(mAccounts, mSource);
            return previous.builder()
                    .setItems(mAccounts)
                    .setAccount(account)
                    .build();
        }
    }

    class ChoosedAccount implements SelectionPartialStates {

        @NonNull
        private final Optional<Account> mAccount;

        public ChoosedAccount(@NonNull final Optional<Account> account) {
            mAccount = account;
        }

        @NonNull
        @Override
        public SelectionState apply(@NonNull final SelectionState previous) {
            final Optional<Account> result = mAccount
                    .filter(previous.getItems()::contains);

            return previous.builder()
                    .setAccount(result)
                    .build();
        }
    }

    class Items implements SelectionPartialStates {

        @NonNull
        private final List<Account> mItems;

        public Items(@NonNull final List<Account> items) {
            mItems = items;
        }

        @NonNull
        @Override
        public SelectionState apply(@NonNull final SelectionState previous) {
            final Optional<Account> result = previous.getAccount()
                    .filter(mItems::contains);

            return previous.builder()
                    .setItems(mItems)
                    .setAccount(result)
                    .build();
        }
    }

    class WithSelector implements SelectionPartialStates {

        @NonNull
        private final List<Account> mAccounts;
        @NonNull
        private final Function<List<Account>, Optional<Account>> mSelector;

        public WithSelector(@NonNull final List<Account> accounts,
                            @NonNull final Function<List<Account>, Optional<Account>> selector) {
            mAccounts = accounts;
            mSelector = selector;
        }

        @NonNull
        @Override
        public SelectionState apply(@NonNull final SelectionState previous) throws Exception {
            final Optional<Account> defaultAccount = mSelector.apply(mAccounts);
            return previous.builder()
                    .setAccount(defaultAccount)
                    .setItems(mAccounts)
                    .build();
        }
    }
}
