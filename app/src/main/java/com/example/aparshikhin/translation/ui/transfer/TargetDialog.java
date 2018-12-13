package com.example.aparshikhin.translation.ui.transfer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.ui.helpers.DialogHelper;

import java.util.ArrayList;
import java.util.List;

public class TargetDialog extends DialogFragment {

    public interface Callback {
        DialogInterface.OnClickListener onClickTargetAccount(@NonNull final MainSpinnerAdapter spinnerAdapter);
    }

    @Nullable
    private Callback mCallback;

    public static TargetDialog newInstance(@NonNull final ArrayList<Account> accounts) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(DialogHelper.ACCOUNTS_TAG, accounts);

        TargetDialog fragment = new TargetDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callback) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MainSpinnerAdapter spinnerAdapter;
        List<Account> accounts;
        Bundle arguments;

        arguments = DialogHelper.getCheckedArguments(this);
        accounts = DialogHelper.getAccounts(arguments);
        spinnerAdapter = DialogHelper.getSpinnerAdapter(accounts, getActivity());

        if (mCallback != null) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Выберите счет зачисления")
                    .setAdapter(spinnerAdapter, mCallback.onClickTargetAccount(spinnerAdapter)).create();
        } else {
            throw new RuntimeException("Callback is null");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
