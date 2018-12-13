package com.example.aparshikhin.translation.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aparshikhin.translation.R;
import com.example.aparshikhin.translation.model.data.Account;

public class AccountView extends LinearLayout {

    public AccountView(@NonNull final Context context) {
        this(context, null);
    }

    public AccountView(@NonNull final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccountView(@NonNull final Context context, @NonNull final AttributeSet attrs,
                       final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(HORIZONTAL);
        inflate(context, R.layout.view_account, this);
    }

    public void setAccount(@NonNull final Account account) {
        TextView nameAccount = findViewById(R.id.view_account_name);
        TextView amountAccount = findViewById(R.id.view_account_amount);
        TextView currencyAccount = findViewById(R.id.view_account_currency);

        nameAccount.setText(account.getName());
        amountAccount.setText(String.valueOf(account.getBalance().getAmount()));
        currencyAccount.setText(String.valueOf(account.getBalance().getCurrency()));
    }

    public void clear() {
        TextView nameAccount = findViewById(R.id.view_account_name);
        TextView amountAccount = findViewById(R.id.view_account_amount);
        TextView currencyAccount = findViewById(R.id.view_account_currency);

        nameAccount.setText("");
        amountAccount.setText("");
        currencyAccount.setText("");
    }
}
