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

public class SourceDialog extends DialogFragment {

    public interface Callback {
        DialogInterface.OnClickListener onClickSourceAccount(@NonNull final List<Account> accounts);
    }

    @Nullable
    private Callback mCallback;

    public static SourceDialog newInstance(@NonNull final ArrayList<Account> accounts) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(DialogHelper.ACCOUNTS_TAG, accounts);

        SourceDialog fragment = new SourceDialog();
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
    public Dialog onCreateDialog(@NonNull final Bundle savedInstanceState) {
        MainSpinnerAdapter spinnerAdapter;
        List<Account> accounts;
        Bundle arguments;

        arguments = DialogHelper.getCheckedArguments(this);
        accounts = DialogHelper.getAccounts(arguments);
        spinnerAdapter = DialogHelper.getSpinnerAdapter(accounts, getActivity());

        if (mCallback != null) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Выберите счет списания")
                    .setAdapter(spinnerAdapter, mCallback.onClickSourceAccount(accounts))
                    .create();
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
