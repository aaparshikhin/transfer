package com.example.aparshikhin.translation.model.repository;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.model.data.AmountException;
import com.example.aparshikhin.translation.model.data.Commission;
import com.example.aparshikhin.translation.model.data.Currency;
import com.example.aparshikhin.translation.model.data.Money;
import com.example.aparshikhin.translation.model.data.Transfer;
import com.example.aparshikhin.translation.model.data.TransferException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.example.aparshikhin.translation.model.data.TransferException.Type.AccountException;
import static com.example.aparshikhin.translation.model.data.TransferException.Type.AmountException;

public class LocalRepository implements Repository {

    private Random mRandom;

    @NonNull
    @Override
    public Single<List<Account>> sourceList() {
        mRandom = new Random();
        int flag = mRandom.nextInt(3);

        return Single.<List<Account>>create(emitter -> {
            List<Account> list = new ArrayList<>();

//            if (flag == 2) {
//                emitter.onSuccess(Collections.emptyList());
//                return;
//            }
//
//            if (flag == 1) {
//                emitter.onError(new Exception("ExeptionSource"));
//                return;
//            }

            for (int i = 0; i < 8; i++) {
                if (i % 2 == 0) {
                    Money money = new Money(new BigDecimal(100 * i), Currency.RUB);
                    Account account = new Account(String.valueOf("Person " + i), i, money, true);
                    list.add(account);
                } else if (i % 3 == 0) {
                    Money money = new Money(new BigDecimal(100 * i), Currency.EUR);
                    Account account = new Account(String.valueOf("Person " + i), i, money, false);
                    list.add(account);
                } else {
                    Money money = new Money(new BigDecimal(100 * i), Currency.USD);
                    Account account = new Account(String.valueOf("Person " + i), i, money, false);
                    list.add(account);
                }
            }
            emitter.onSuccess(list);
        })
                .delay(2, TimeUnit.SECONDS, Schedulers.computation());
    }

    @NonNull
    @Override
    public Single<List<Account>> targetList(@NonNull final Optional<Account> sourceAccount) {
        mRandom = new Random();
        int flag = mRandom.nextInt(3);

        return Single.<List<Account>>create(emitter -> {
            List<Account> list = new ArrayList<>();

//            if (flag == 2) {
//                emitter.onSuccess(Collections.emptyList());
//                return;
//            }
//
//            if (flag == 1) {
//                emitter.onError(new Exception("ExeptionTarget"));
//                return;
//            }

            for (int i = 0; i < 8; i++) {
                if (i % 3 == 0) {
                    Money money = new Money(new BigDecimal(100 * i), Currency.RUB);
                    Account account = new Account(String.valueOf("Person " + i), i, money, false);
                    list.add(account);
                } else if (i % 2 == 0) {
                    Money money = new Money(new BigDecimal(100 * i), Currency.EUR);
                    Account account = new Account(String.valueOf("Person " + i), i, money, true);
                    list.add(account);
                } else {
                    Money money = new Money(new BigDecimal(100 * i), Currency.USD);
                    Account account = new Account(String.valueOf("Person " + i), i, money, false);
                    list.add(account);
                }
            }

            List<Account> accounts = sourceAccount
                    .map(account -> Stream.of(list)
                            .filter(value -> value.getBalance().getCurrency() == account.getBalance().getCurrency())
                            .toList())
                    .orElse(Collections.emptyList());
            emitter.onSuccess(accounts);
        }).delay(2, TimeUnit.SECONDS, Schedulers.computation());
    }

    @NonNull
    @Override
    public Single<Commission> commission(@NonNull final Account account, @NonNull final Money money) {
        return Single.<Commission>create(emitter -> {
            if (account.isCreditCardAccount()) {

                if (money.getAmount().compareTo(new BigDecimal(500)) >= 0) {
                    BigDecimal result = money.getAmount().divide(new BigDecimal(5), 2, RoundingMode.HALF_UP);
                    Money moneyCommission = new Money(result, account.getBalance().getCurrency());
                    emitter.onSuccess(Commission.createWithPercent(moneyCommission, new BigDecimal(5)));
                } else {
                    Money moneyCommission = new Money(new BigDecimal(100), account.getBalance().getCurrency());
                    emitter.onSuccess(Commission.createFixed(moneyCommission));
                }
//                emitter.onError(new CommissionException("custom exception"));
            } else {
                emitter.onSuccess(Commission.createWithZero());
            }
        }).delay(2, TimeUnit.SECONDS, Schedulers.computation());
    }

    @NonNull
    @Override
    public Single<Transfer> validateTransfer(@NonNull final Transfer transfer) {
        return Single.<Transfer>create(emitter -> {
            if (transfer.getAmount().getAmount().compareTo(new BigDecimal(99)) <= 0) {
                emitter.onError(new AmountException("Сумма трансфера меньше 100 " + transfer.getAmount().getCurrency()));
            } else {
                emitter.onSuccess(transfer);
//                emitter.onError(new IOException("Нет сети"));
//                emitter.onError(new TransferException("акканунты invalid", AccountException));
            }
        }).delay(2, TimeUnit.SECONDS, Schedulers.computation());
    }
}
