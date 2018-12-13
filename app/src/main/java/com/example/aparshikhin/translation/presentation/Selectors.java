package com.example.aparshikhin.translation.presentation;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.model.data.LoadModel;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class Selectors {

    @NonNull
    public static <T> Function<List<T>, Optional<T>> first() {
        return list -> Stream.of(list).findFirst();
    }

    @NonNull
    public static BiFunction<List<Account>, Optional<Account>, Optional<Account>> chooseDefaultTarget() {

        return (targetAccounts, selectedSourceAccount) -> selectedSourceAccount
                    .flatMap(sourceAccount -> Stream.of(targetAccounts)
                            .filter(account -> account.getBalance().getCurrency() == sourceAccount.getBalance().getCurrency())
                            .filter(account -> account.getId() != sourceAccount.getId())
                            .findFirst());
    }

    @NonNull
    public static Observable<List<Account>> filterAccounts(@NonNull final LoadModel<List<Account>> listLoadModel) {
        switch (listLoadModel.getStatus()) {
            case Successful:
                return Observable.just(listLoadModel.getData());

            case Error:
                return Observable.just(Collections.emptyList());

            case Loading:
                return Observable.empty();
        }
        throw new RuntimeException("Nothing selected");
    }
}
