package com.example.aparshikhin.translation.ui.transfer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.example.aparshikhin.translation.R;
import com.example.aparshikhin.translation.model.data.Transfer;
import com.example.aparshikhin.translation.ui.helpers.DialogHelper;

public class TransferDialog extends DialogFragment {

    public static TransferDialog newInstance(@NonNull final Transfer transfer) {
        Bundle args = new Bundle();
        args.putParcelable(DialogHelper.TRANSFER_TAG, transfer);

        TransferDialog fragment = new TransferDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments;
        Transfer transfer;

        arguments = DialogHelper.getCheckedArguments(this);
        transfer = DialogHelper.getTransfer(arguments);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Информация по переводу")
                .setMessage(getTransferInfo(transfer))
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                }).create();

    }

    @NonNull
    private String getTransferInfo(@NonNull final Transfer transfer) {
        return getResources().getString(
                R.string.transfer_info,
                transfer.getSourceAccountId() + "",
                transfer.getTargetAccountId() + "",
                transfer.getAmount().getAmount().toString() + " " + transfer.getAmount().getCurrency());
    }
}
