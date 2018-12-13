package com.example.aparshikhin.translation.model.repository;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.model.data.Commission;
import com.example.aparshikhin.translation.model.data.Money;
import com.example.aparshikhin.translation.model.data.Transfer;

import java.util.List;

import io.reactivex.Single;

public interface Repository {

    /**
     * Get source list
     */
    @NonNull
    Single<List<Account>> sourceList();

    /**
     * Get target list filter by @param sourceAccount
     */
    @NonNull
    Single<List<Account>> targetList(@NonNull final Optional<Account> sourceAccount);

    /**
     * Calculate commission
     */
    @NonNull
    Single<Commission> commission(@NonNull final Account account, @NonNull final Money money);

    @NonNull
    Single<Transfer> validateTransfer(@NonNull final Transfer transfer);
}
