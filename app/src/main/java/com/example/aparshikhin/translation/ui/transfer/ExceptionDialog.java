package com.example.aparshikhin.translation.ui.transfer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.example.aparshikhin.translation.ui.helpers.DialogHelper;

public class ExceptionDialog extends DialogFragment {

    public static ExceptionDialog newInstance(@NonNull final String message) {
        Bundle args = new Bundle();
        args.putString(DialogHelper.MESSAGE_TAG, message);

        ExceptionDialog fragment = new ExceptionDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments;
        String message;

        arguments = DialogHelper.getCheckedArguments(this);
        message = DialogHelper.getMessage(arguments);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Ошибка!!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                })
                .create();
    }
}
