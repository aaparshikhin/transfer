package com.example.aparshikhin.translation.presentation.transfer;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.model.data.Commission;
import com.example.aparshikhin.translation.model.data.LoadModel;
import com.example.aparshikhin.translation.model.data.Money;
import com.example.aparshikhin.translation.model.data.Transfer;
import com.example.aparshikhin.translation.presentation.ValidationModel;
import com.example.aparshikhin.translation.stream.Function4;
import com.example.aparshikhin.translation.stream.Optionals;

import io.reactivex.functions.Function6;

public class TransferFactory {

    @NonNull
    public static Function6<ValidationModel, ValidationModel, Optional<Account>, Optional<Account>,
            Optional<Money>, Optional<LoadModel<Commission>>, Optional<Transfer>> createTransfer() {
        return (validationAccounts, validationAmount, sourceAccount, targetAccount, amount, commission) ->
                commission.flatMap(commissionLoadModel -> {
                    if (validationAccounts.isValid() && validationAmount.isValid() &&
                            commissionLoadModel.getStatus() == LoadModel.Status.Successful) {
                        return Optionals.combine(sourceAccount, targetAccount, amount, commission,
                                create());
                    }
                    return Optional.empty();
                });
    }

    @NonNull
    private static Function4<Account, Account, Money, LoadModel<Commission>, Transfer> create() {
        return ((sourceAccount, targetAccount, money, commission) ->
                new Transfer(sourceAccount.getId(), targetAccount.getId(), money));
    }
}
