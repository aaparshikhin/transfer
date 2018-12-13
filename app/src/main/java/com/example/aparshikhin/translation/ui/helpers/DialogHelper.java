package com.example.aparshikhin.translation.ui.helpers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.example.aparshikhin.translation.R;
import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.model.data.Transfer;
import com.example.aparshikhin.translation.ui.transfer.MainSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DialogHelper {

    public static final String ACCOUNTS_TAG = "accounts";
    public static final String MESSAGE_TAG = "message";
    public static final String TRANSFER_TAG = "transfer";

    @NonNull
    public static <T extends DialogFragment> Bundle getCheckedArguments(@NonNull final T dialogFragment) {
        Bundle arguments;
        Bundle bundle = dialogFragment.getArguments();
        if (bundle != null) {
            arguments = bundle;
        } else {
            throw new RuntimeException("Arguments is empty!!!");
        }
        return arguments;
    }

    @NonNull
    public static List<Account> getAccounts(@NonNull final Bundle arguments) {
        List<Account> accounts;
        ArrayList<Account> list = arguments.getParcelableArrayList(ACCOUNTS_TAG);
        if (list != null) {
            accounts = list;
        } else {
            throw new RuntimeException("list is empty!!!");
        }
        return accounts;
    }

    @NonNull
    public static MainSpinnerAdapter getSpinnerAdapter(@NonNull final List<Account> accounts,
                                                       @Nullable final FragmentActivity fragmentActivity) {
        MainSpinnerAdapter spinnerAdapter;
        if (fragmentActivity != null) {
            spinnerAdapter = new MainSpinnerAdapter(fragmentActivity, R.layout.list_item_account);
            spinnerAdapter.addAll(accounts);
        } else {
            throw new RuntimeException("spinner adapter is empty!!!");
        }
        return spinnerAdapter;
    }

    @NonNull
    public static String getMessage(@NonNull final Bundle arguments) {
        String message = arguments.getString(MESSAGE_TAG);
        if (message != null) {
            return message;
        } else {
            throw new RuntimeException("message is null!!!");
        }
    }

    @NonNull
    public static Transfer getTransfer(@NonNull final Bundle arguments) {
        Transfer transfer = arguments.getParcelable(TRANSFER_TAG);
        if (transfer != null) {
            return transfer;
        } else {
            throw new RuntimeException("transfer is null!!!");
        }
    }
}
