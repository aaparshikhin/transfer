package com.example.aparshikhin.translation.presentation;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.model.data.Money;
import com.example.aparshikhin.translation.stream.Optionals;

import java.math.BigDecimal;

import io.reactivex.functions.BiFunction;

public class Validators {

    @NonNull
    public static BiFunction<Optional<Account>, Optional<Money>, ValidationModel> validateAmount() {
        return (sourceAccount, amount) -> Optionals.combine(sourceAccount, amount, Validators::validateAmount)
                .orElse(ValidationModel.inValid(""));
    }

    @NonNull
    public static BiFunction<Optional<Account>, Optional<Account>, ValidationModel> validateAccounts() {
        return (sourceAccount, targetAccount) -> Optionals.combine(sourceAccount, targetAccount, Validators::validateAccounts)
                .orElse(ValidationModel.inValid(""));
    }

    @NonNull
    private static ValidationModel validateAmount(@NonNull final Account sourceAccount,
                                                  @NonNull final Money money) {
        final Money balance = sourceAccount.getBalance();
        if (balance.getCurrency() != money.getCurrency()) {
            return ValidationModel.inValid("Разная валюта");
        }

        final BigDecimal amount = money.getAmount();
        if (amount.compareTo(balance.getAmount()) > 0) {
            return ValidationModel.inValid("На счете недостаточно средств");
        }
        if (amount.compareTo(BigDecimal.valueOf(10)) < 0) {
            return ValidationModel.inValid("Минимальная сумма перевода 10 " + balance.getCurrency());
        }
        if (amount.compareTo(BigDecimal.valueOf(1_000_000)) > 0) {
            return ValidationModel.inValid("Максимальная сумма перевода 1000000 " + balance.getCurrency());
        }

        return ValidationModel.valid();
    }

    @NonNull
    private static ValidationModel validateAccounts(@NonNull final Account sourceAccount,
                                                    @NonNull final Account targetAccount) {
        if (sourceAccount.getBalance().getCurrency() != targetAccount.getBalance().getCurrency()) {
            return ValidationModel.inValid("Валюта не одинаковая");
        }
        if (sourceAccount.getId() == targetAccount.getId()) {
            return ValidationModel.inValid("Счета списания и зачисления одинаковы");
        }
        return ValidationModel.valid();
    }
}
