package com.example.aparshikhin.translation.presentation.transfer;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.annimon.stream.function.Function;
import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.model.data.Commission;
import com.example.aparshikhin.translation.model.data.LoadModel;
import com.example.aparshikhin.translation.model.data.Transfer;
import com.example.aparshikhin.translation.presentation.ValidationModel;

import java.util.List;

public interface PartialStates extends Function<TransferViewState, TransferViewState> {

    class SourcesAccounts implements PartialStates {

        @NonNull
        private final LoadModel<List<Account>> mAccounts;

        public SourcesAccounts(@NonNull final LoadModel<List<Account>> accounts) {
            mAccounts = accounts;
        }

        @NonNull
        @Override
        public TransferViewState apply(@NonNull final TransferViewState previousViewState) {
            return previousViewState.builder()
                    .setSourceAccounts(mAccounts)
                    .build();
        }
    }

    class SourceAccount implements PartialStates {

        @NonNull
        private final Optional<Account> mAccount;

        public SourceAccount(@NonNull final Optional<Account> account) {
            mAccount = account;
        }

        @NonNull
        @Override
        public TransferViewState apply(@NonNull final TransferViewState previousViewState) {
            return previousViewState.builder()
                    .setSelectedSourceAccount(mAccount)
                    .build();
        }
    }

    class TargetAccounts implements PartialStates {

        @NonNull
        private final LoadModel<List<Account>> mTargetAccounts;

        public TargetAccounts(@NonNull final LoadModel<List<Account>> targetAccounts) {
            mTargetAccounts = targetAccounts;
        }

        @NonNull
        @Override
        public TransferViewState apply(@NonNull final TransferViewState previousViewState) {
            return previousViewState.builder()
                    .setTargetAccounts(Optional.of(mTargetAccounts))
                    .build();
        }
    }

    class TargetAccount implements PartialStates {

        @NonNull
        private final Optional<Account> mAccount;

        public TargetAccount(@NonNull final Optional<Account> account) {
            mAccount = account;
        }

        @NonNull
        @Override
        public TransferViewState apply(@NonNull final TransferViewState previousViewState) {
            return previousViewState.builder()
                    .setSelectedTargetAccount(mAccount)
                    .build();
        }
    }

    class ValidationAccounts implements PartialStates {

        @NonNull
        private final ValidationModel mValidationModel;

        public ValidationAccounts(@NonNull final ValidationModel validationModel) {
            mValidationModel = validationModel;
        }

        @NonNull
        @Override
        public TransferViewState apply(@NonNull final TransferViewState previousViewState) {
            return previousViewState.builder()
                    .setResultValidationAccounts(Optional.of(mValidationModel))
                    .build();
        }
    }

    class ValidationAmount implements PartialStates {

        @NonNull
        private final ValidationModel mValidationModel;

        public ValidationAmount(@NonNull final ValidationModel validationModel) {
            mValidationModel = validationModel;
        }

        @NonNull
        @Override
        public TransferViewState apply(@NonNull final TransferViewState previousViewState) {
            return previousViewState.builder()
                    .setResultValidationAmount(Optional.of(mValidationModel))
                    .build();
        }
    }

    class ResultTransfer implements PartialStates {

        @NonNull
        private final Optional<Transfer> mTransfer;

        public ResultTransfer(@NonNull Optional<Transfer> transfer) {
            mTransfer = transfer;
        }

        @NonNull
        @Override
        public TransferViewState apply(@NonNull final TransferViewState previousViewState) {
            return previousViewState.builder()
                    .setTransfer(mTransfer)
                    .build();
        }
    }

    class EnableAll implements PartialStates {

        private final boolean mTransfer;

        public EnableAll(final boolean transfer) {
            mTransfer = transfer;
        }

        @Override
        public TransferViewState apply(@NonNull final TransferViewState previous) {
            return previous.builder()
                    .setEnableAllViews(mTransfer)
                    .build();
        }
    }

    class CommissionState implements PartialStates {

        @NonNull
        private final Optional<LoadModel<Commission>> mCommission;

        public CommissionState(@NonNull Optional<LoadModel<Commission>> comission) {
            mCommission = comission;
        }

        @Override
        public TransferViewState apply(TransferViewState previous) {
            return previous.builder()
                    .setComission(mCommission)
                    .build();
        }
    }
}
