package com.example.aparshikhin.translation.ui.transfer;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.aparshikhin.translation.R;
import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.ui.widget.AccountView;

import java.io.Serializable;

public class MainSpinnerAdapter extends ArrayAdapter<Account> implements Serializable {

    @NonNull
    private final LayoutInflater mInflater;
    @LayoutRes
    private final int mResource;


    public MainSpinnerAdapter(@NonNull final Context context, final int resource) {
        super(context, resource);
        mInflater = LayoutInflater.from(context);
        mResource = resource;
    }

    @Override
    public View getDropDownView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);

        AccountView accountView = view.findViewById(R.id.list_item_account_account);

        final Account account = getItem(position);
        if (account == null) {
            throw new RuntimeException("account is null");
        }
        accountView.setAccount(account);
        return view;
    }
}
