package com.example.aparshikhin.translation.presentation.transfer;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.model.data.Commission;
import com.example.aparshikhin.translation.model.data.LoadModel;
import com.example.aparshikhin.translation.model.data.Money;
import com.example.aparshikhin.translation.model.data.Transfer;
import com.example.aparshikhin.translation.presentation.ValidationModel;

import java.util.List;

public class TransferViewState {

    @NonNull
    public static TransferViewState create() {
        return new TransferViewState(LoadModel.loading(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                true);
    }

    @NonNull
    private final LoadModel<List<Account>> mSourcesAccounts;
    @NonNull
    private final Optional<Account> mSelectedSourceAccount;
    @NonNull
    private final Optional<LoadModel<List<Account>>> mTargetsAccounts;
    @NonNull
    private final Optional<Account> mSelectedTargetAccount;
    @NonNull
    private final Optional<Money> mMoney;
    @NonNull
    private final Optional<Transfer> mTransfer;
    @NonNull
    private final Optional<ValidationModel> mValidationAccounts;
    @NonNull
    private final Optional<ValidationModel> mValidationAmount;
    @NonNull
    private final Optional<LoadModel<Commission>> mCommission;
    private final boolean mEnableAllViews;

    private TransferViewState(@NonNull final LoadModel<List<Account>> sourcesAccounts,
                              @NonNull final Optional<Account> selectedSourceAccount,
                              @NonNull final Optional<LoadModel<List<Account>>> targetsAccounts,
                              @NonNull final Optional<Account> selectedTargetAccount,
                              @NonNull final Optional<Money> money,
                              @NonNull final Optional<Transfer> transfer,
                              @NonNull final Optional<ValidationModel> validationAccounts,
                              @NonNull final Optional<ValidationModel> validationAmount,
                              @NonNull final Optional<LoadModel<Commission>> commission,
                              final boolean enabledAll) {
        mSourcesAccounts = sourcesAccounts;
        mSelectedSourceAccount = selectedSourceAccount;
        mTargetsAccounts = targetsAccounts;
        mSelectedTargetAccount = selectedTargetAccount;
        mMoney = money;
        mTransfer = transfer;
        mValidationAccounts = validationAccounts;
        mValidationAmount = validationAmount;
        mCommission = commission;
        mEnableAllViews = enabledAll;
    }

    public boolean getEnabledAll() {
        return mEnableAllViews;
    }

    @NonNull
    public Optional<ValidationModel> getValidationAmount() {
        return mValidationAmount;
    }

    @NonNull
    public LoadModel<List<Account>> getSourcesAccounts() {
        return mSourcesAccounts;
    }

    @NonNull
    public Optional<Account> getSelectedSourceAccount() {
        return mSelectedSourceAccount;
    }

    @NonNull
    public Optional<LoadModel<List<Account>>> getTargetsAccounts() {
        return mTargetsAccounts;
    }

    @NonNull
    public Optional<Account> getSelectedTargetAccount() {
        return mSelectedTargetAccount;
    }

    @NonNull
    public Optional<Money> getMoney() {
        return mMoney;
    }

    @NonNull
    public Optional<Transfer> getTransfer() {
        return mTransfer;
    }

    @NonNull
    public Optional<ValidationModel> getValidationAccounts() {
        return mValidationAccounts;
    }

    @NonNull
    public Optional<LoadModel<Commission>> getCommission() {
        return mCommission;
    }

    @NonNull
    public Builder builder() {
        return new Builder(this);
    }

    public class Builder {

        @NonNull
        private LoadModel<List<Account>> mSourcesAccounts;
        @NonNull
        private Optional<Account> mSelectedSourceAccount;
        @NonNull
        private Optional<LoadModel<List<Account>>> mTargetsAccounts;
        @NonNull
        private Optional<Account> mSelectedTargetAccount;
        @NonNull
        private Optional<Money> mMoney;
        @NonNull
        private Optional<Transfer> mTransfer;
        @NonNull
        private Optional<ValidationModel> mValidationAccounts;
        @NonNull
        private Optional<ValidationModel> mValidationAmount;
        @NonNull
        private Optional<LoadModel<Commission>> mCommission;
        private boolean mEnabledAll;

        private Builder(@NonNull final TransferViewState viewState) {
            mSourcesAccounts = viewState.getSourcesAccounts();
            mSelectedSourceAccount = viewState.getSelectedSourceAccount();
            mTargetsAccounts = viewState.getTargetsAccounts();
            mSelectedTargetAccount = viewState.getSelectedTargetAccount();
            mMoney = viewState.getMoney();
            mTransfer = viewState.getTransfer();
            mValidationAccounts = viewState.getValidationAccounts();
            mValidationAmount = viewState.getValidationAmount();
            mCommission = viewState.getCommission();
            mEnabledAll = viewState.getEnabledAll();
        }

        @NonNull
        public Builder setEnableAllViews(final boolean enableAll) {
            mEnabledAll = enableAll;
            return this;
        }

        @NonNull
        public Builder setResultValidationAmount(@NonNull final Optional<ValidationModel> optional) {
            mValidationAmount = optional;
            return this;
        }

        @NonNull
        public Builder setResultValidationAccounts(@NonNull final Optional<ValidationModel> validationAccounts) {
            mValidationAccounts = validationAccounts;
            return this;
        }

        @NonNull
        public Builder setSourceAccounts(@NonNull final LoadModel<List<Account>> loadModel) {
            mSourcesAccounts = loadModel;
            return this;
        }

        @NonNull
        public Builder setSelectedSourceAccount(@NonNull final Optional<Account> account) {
            mSelectedSourceAccount = account;
            return this;
        }

        @NonNull
        public Builder setTargetAccounts(@NonNull final Optional<LoadModel<List<Account>>> optional) {
            mTargetsAccounts = optional;
            return this;
        }

        @NonNull
        public Builder setSelectedTargetAccount(@NonNull final Optional<Account> account) {
            mSelectedTargetAccount = account;
            return this;
        }

        @NonNull
        public Builder setMoney(@NonNull final Optional<Money> money) {
            mMoney = money;
            return this;
        }

        @NonNull
        public Builder setTransfer(@NonNull final Optional<Transfer> transfer) {
            mTransfer = transfer;
            return this;
        }

        @NonNull
        public Builder setComission(@NonNull final Optional<LoadModel<Commission>> commission) {
            mCommission = commission;
            return this;
        }

        @NonNull
        public TransferViewState build() {
            return new TransferViewState(mSourcesAccounts, mSelectedSourceAccount, mTargetsAccounts,
                    mSelectedTargetAccount, mMoney, mTransfer, mValidationAccounts, mValidationAmount,
                    mCommission, mEnabledAll);
        }
    }
}
