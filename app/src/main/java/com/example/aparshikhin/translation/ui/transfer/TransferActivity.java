package com.example.aparshikhin.translation.ui.transfer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.aparshikhin.translation.R;
import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.model.data.Commission;
import com.example.aparshikhin.translation.model.data.LoadModel;
import com.example.aparshikhin.translation.model.data.Money;
import com.example.aparshikhin.translation.model.data.Transfer;
import com.example.aparshikhin.translation.model.repository.LocalRepository;
import com.example.aparshikhin.translation.presentation.ValidationModel;
import com.example.aparshikhin.translation.presentation.transfer.TransferPresenter;
import com.example.aparshikhin.translation.presentation.transfer.TransferView;
import com.example.aparshikhin.translation.presentation.transfer.TransferViewState;
import com.example.aparshikhin.translation.ui.widget.AccountView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

import static android.content.DialogInterface.OnClickListener;
import static com.annimon.stream.function.Function.Util.safe;

public class TransferActivity extends MvpAppCompatActivity implements TransferView, SourceDialog.Callback,
        TargetDialog.Callback {

    private AccountView mSourceAccountView;
    private ProgressBar mSourceAccountProgressBar;
    private TextView mDescriptionTargetAccountsTextView;
    private AccountView mTargetAccountView;
    private ProgressBar mTargetAccountProgressBar;
    private TextView mErrorAccountsTextView;
    private EditText mAmountEditText;
    private TextView mErrorAmountTextView;
    private TextView mCommissionTextView;
    private ProgressBar mCommissionProgressBar;
    private TextView mCommissionErrorTextView;
    private ProgressBar mTransferProgressBar;
    private Button mSendButton;

    @InjectPresenter()
    TransferPresenter mPresenter;
    private Disposable mDisposable;

    @ProvidePresenter()
    TransferPresenter provideMainPresenter() {
        return new TransferPresenter(new LocalRepository());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSourceAccountView = findViewById(R.id.activity_main_account_vew_source);
        mSourceAccountProgressBar = findViewById(R.id.activity_main_progress_bar_source_);

        mTargetAccountView = findViewById(R.id.activity_main_account_vew_target);
        mTargetAccountProgressBar = findViewById(R.id.activity_main_progress_bar_target);
        mDescriptionTargetAccountsTextView = findViewById(R.id.activity_main_text_veiw_description_target_account);

        mErrorAccountsTextView = findViewById(R.id.activity_main_text_view_error_accounts);
        mAmountEditText = findViewById(R.id.activity_main_edit_text_amount);
        mErrorAmountTextView = findViewById(R.id.activity_main_text_view_error_amount);

        mCommissionTextView = findViewById(R.id.activity_main_text_view_commission);
        mCommissionProgressBar = findViewById(R.id.activity_main_progress_bar_commission);
        mCommissionErrorTextView = findViewById(R.id.activity_main_text_view_error_commission);

        mTransferProgressBar = findViewById(R.id.activity_main_progress_bar_transfer);

        mSendButton = findViewById(R.id.activity_main_button_send);

        mSourceAccountView.setVisibility(View.VISIBLE);
        mSourceAccountProgressBar.setVisibility(View.GONE);

        mTargetAccountProgressBar.setVisibility(View.GONE);
        mTargetAccountView.setVisibility(View.GONE);
        mDescriptionTargetAccountsTextView.setVisibility(View.GONE);

        mErrorAccountsTextView.setVisibility(View.GONE);

        mCommissionTextView.setVisibility(View.GONE);
        mCommissionProgressBar.setVisibility(View.GONE);
        mCommissionErrorTextView.setVisibility(View.GONE);

        mAmountEditText.setEnabled(false);
        mSendButton.setEnabled(false);

        mTransferProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDisposable = RxTextView.textChanges(mAmountEditText)
                .map(this::parseAmount)
                .subscribe(mPresenter::onAmountChanged);
    }

    @NonNull
    private Optional<BigDecimal> parseAmount(@NonNull final CharSequence charSequence) {
        return Optional.ofNullable(charSequence)
                .map(String::valueOf)
                .filterNot(String::isEmpty)
                .map(safe(BigDecimal::new));
    }

    @Override
    public void render(@NonNull final TransferViewState viewState) {
        // render Accounts
        renderSourceAccountsSpinner(viewState.getSourcesAccounts(), viewState.getSelectedSourceAccount());
        renderTargetAccountsSpinner(viewState);

        // render Validation
        renderResultAccountsValidation(viewState.getValidationAccounts());
        renderResultAmountValidation(viewState.getValidationAmount());

        renderCommission(viewState.getCommission());
        renderTransfer(viewState.getTransfer());

        renderEnableAllViews(viewState.getEnabledAll());
    }

    private void renderSourceAccountsSpinner(@NonNull final LoadModel<List<Account>> sourcesAccounts,
                                             @NonNull final Optional<Account> selectedSourceAccount) {
        switch (sourcesAccounts.getStatus()) {
            case Successful:
                renderSourceAccountsSpinnerSuccessful(sourcesAccounts, selectedSourceAccount);
                break;

            case Loading:
                renderAttributesOfSourceAccounts(false, false, true);
                break;

            case Error:
                renderAttributesOfSourceAccounts(false, true, false);
                break;
        }
    }

    private void renderSourceAccountsSpinnerSuccessful(@NonNull LoadModel<List<Account>> sourcesAccounts, @NonNull Optional<Account> selectedSourceAccount) {
        mAmountEditText.setEnabled(true);
        renderAttributesOfSourceAccounts(true, false, false);
        setSelectedAccount(selectedSourceAccount, mSourceAccountView);
        setContentToSourceDialog(sourcesAccounts.getData());
    }

    private void renderAttributesOfSourceAccounts(final boolean enabledSourceAccountsView,
                                                  final boolean visibleError,
                                                  final boolean visibilityProgressBar) {
        mSourceAccountView.setEnabled(enabledSourceAccountsView);
        mErrorAccountsTextView.setVisibility(visibleError ? View.VISIBLE : View.GONE);
        mSourceAccountProgressBar.setVisibility(visibilityProgressBar ? View.VISIBLE : View.GONE);
        mSourceAccountView.setOnClickListener(null);
    }

    private void setSelectedAccount(@NonNull final Optional<Account> selectedAccount,
                                    @NonNull final AccountView accountView) {
        selectedAccount.ifPresentOrElse(accountView::setAccount, accountView::clear);
    }

    private void setContentToSourceDialog(@NonNull final List<Account> sourcesAccounts) {
        if (!sourcesAccounts.isEmpty()) {
            mSourceAccountView.setOnClickListener(view -> clickSourceAccount(sourcesAccounts));
            mSourceAccountView.setEnabled(true);
        } else {
            mSourceAccountView.setOnClickListener(null);
            mSourceAccountView.setEnabled(false);
        }
    }

    private void clickSourceAccount(@NonNull final List<Account> accounts) {
        MainSpinnerAdapter spinnerAdapter = new MainSpinnerAdapter(this, R.layout.list_item_account);
        spinnerAdapter.addAll(accounts);
        showSourceAccountDialog(accounts);
    }

    private void showSourceAccountDialog(@NonNull List<Account> accounts) {
        ArrayList<Account> list = new ArrayList<>(accounts);
        SourceDialog sourceDialog = SourceDialog.newInstance(list);
        sourceDialog.show(getSupportFragmentManager(), "source dialog");
    }

    private void renderTargetAccountsSpinner(@NonNull final TransferViewState viewState) {
        Optional<LoadModel<List<Account>>> targetsAccounts = viewState.getTargetsAccounts();
        Optional<Account> selectedTargetAccount = viewState.getSelectedTargetAccount();

        targetsAccounts.ifPresentOrElse(listLoadModel -> {
            switch (listLoadModel.getStatus()) {
                case Successful:
                    renderTargetAccountsSpinnerSuccessful(targetsAccounts, selectedTargetAccount);
                    break;

                case Loading:
                    renderAttributesOfTarget(View.GONE, View.VISIBLE);
                    break;

                case Error:
                    renderAttributesOfTarget(View.GONE, View.GONE);
                    break;
            }
        }, this::renderTargetAccountsWithoutData);
    }

    private void renderTargetAccountsSpinnerSuccessful(Optional<LoadModel<List<Account>>> targetsAccounts, Optional<Account> selectedTargetAccount) {
        renderAttributesOfTarget(View.VISIBLE, View.GONE);
        setSelectedAccount(selectedTargetAccount, mTargetAccountView);
        setContentTargetDialog(targetsAccounts);
    }

    private void renderAttributesOfTarget(final int visibilityTarget,
                                          final int visibilityProgressBar) {
        mDescriptionTargetAccountsTextView.setVisibility(visibilityTarget);
        mTargetAccountView.setVisibility(visibilityTarget);
        mTargetAccountProgressBar.setVisibility(visibilityProgressBar);
        mTargetAccountView.setOnClickListener(null);
    }

    private void setContentTargetDialog(@NonNull final Optional<LoadModel<List<Account>>> targetsAccounts) {
        MainSpinnerAdapter spinnerAdapter = new MainSpinnerAdapter(this, R.layout.list_item_account);
        targetsAccounts.ifPresentOrElse(loadModel -> createClickListener(loadModel.getData()),
                () -> hideTargetAccountDialog(spinnerAdapter));
    }

    private void createClickListener(@NonNull final List<Account> accounts) {
        if (!accounts.isEmpty()) {
            mTargetAccountView.setOnClickListener(view ->
                    showTargetAccountDialog(accounts)
            );
        }
    }

    private void hideTargetAccountDialog(@NonNull final MainSpinnerAdapter spinnerAdapter) {
        spinnerAdapter.clear();
        mTargetAccountView.setOnClickListener(null);
    }

    private void showTargetAccountDialog(@NonNull final List<Account> accounts) {
        ArrayList<Account> accountArrayList = new ArrayList<>(accounts);
        TargetDialog targetDialog = TargetDialog.newInstance(accountArrayList);
        targetDialog.show(getSupportFragmentManager(), "target dialog");
    }

    private void renderTargetAccountsWithoutData() {
        mTargetAccountView.clear();
        mDescriptionTargetAccountsTextView.setVisibility(View.GONE);
        mTargetAccountView.setVisibility(View.GONE);
        mTargetAccountProgressBar.setVisibility(View.GONE);
    }

    private void renderResultAccountsValidation(@NonNull final Optional<ValidationModel> validationAccounts) {
        String message = getMessage(validationAccounts);
        mErrorAccountsTextView.setText(message);
        mErrorAccountsTextView.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
    }

    private void renderResultAmountValidation(@NonNull final Optional<ValidationModel> validationAmount) {
        String message = getMessage(validationAmount);
        mErrorAmountTextView.setText(message);
        mErrorAmountTextView.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
    }

    @NonNull
    private String getMessage(@NonNull final Optional<ValidationModel> validationAccounts) {
        return validationAccounts
                .filterNot(ValidationModel::isValid)
                .flatMap(ValidationModel::getMessage)
                .orElse(null);
    }

    private void renderCommission(@NonNull final Optional<LoadModel<Commission>> commissionOptional) {
        commissionOptional.ifPresentOrElse((LoadModel<Commission> commissionModel) -> {
            switch (commissionModel.getStatus()) {
                case Successful:
                    renderCommissionSuccessful(commissionModel);
                    break;

                case Error:
                    renderCommissionError(commissionModel);
                    break;

                case Loading:
                    renderCommissionLoading();
                    break;
            }

        }, () -> mCommissionTextView.setVisibility(View.GONE));
    }

    private void renderCommissionSuccessful(LoadModel<Commission> commissionModel) {
        mCommissionErrorTextView.setVisibility(View.GONE);
        mCommissionProgressBar.setVisibility(View.GONE);

        Money commission = commissionModel.getData().getCommission();
        switch (commissionModel.getData().getTypeCommission()) {
            case Fixed:
                renderCommissionSuccessfulFixed(commission);
                break;

            case Zero:
                mCommissionTextView.setText("Комиссия не взимается");
                break;

            case Percent:
                renderCommissionSuccessfulPercent(commissionModel, commission);
                break;
        }
        mCommissionTextView.setVisibility(View.VISIBLE);
    }

    private void renderCommissionSuccessfulPercent(LoadModel<Commission> commissionModel, Money commission) {
        String messagePercent = getResources().getString(
                R.string.activity_main_commission_description_percent,
                commission.getAmount().toString(),
                commission.getCurrency().toString(),
                commissionModel.getData().getPercent().toString());
        mCommissionTextView.setText(messagePercent);
    }

    private void renderCommissionSuccessfulFixed(Money commission) {
        String messageFixed = getResources().getString(
                R.string.activity_main_commission_description_fixed,
                commission.getAmount().toString(),
                commission.getCurrency().toString(),
                commission.getCurrency().toString());
        mCommissionTextView.setText(messageFixed);
    }

    private void renderCommissionError(LoadModel<Commission> commissionModel) {
        mCommissionErrorTextView.setText(commissionModel.getThrowable().getMessage());
        mCommissionErrorTextView.setVisibility(View.VISIBLE);
        mCommissionTextView.setVisibility(View.GONE);
        mCommissionProgressBar.setVisibility(View.GONE);
    }

    private void renderCommissionLoading() {
        mCommissionErrorTextView.setVisibility(View.GONE);
        mCommissionTextView.setVisibility(View.GONE);
        mCommissionProgressBar.setVisibility(View.VISIBLE);
    }

    private void renderTransfer(@NonNull final Optional<Transfer> transfer) {
        transfer.ifPresentOrElse(this::enableTransfer, this::disableTransfer);
    }

    private void enableTransfer(@NonNull final Transfer transfer) {
        mSendButton.setOnClickListener(view -> mPresenter.onSendTransfer(transfer));
        mSendButton.setEnabled(true);
    }

    private void disableTransfer() {
        mSendButton.setOnClickListener(null);
    }

    private void renderEnableAllViews(final boolean flag) {
        mTransferProgressBar.setVisibility(flag ? View.GONE : View.VISIBLE);
        mSourceAccountView.setEnabled(flag);
        mTargetAccountView.setEnabled(flag);
        mAmountEditText.setEnabled(flag);
        if (!flag) {
            mSendButton.setEnabled(false);
        }
    }

    @Override
    public void showTransferDialog(@NonNull final Transfer transfer) {
        TransferDialog transferDialog = TransferDialog.newInstance(transfer);
        transferDialog.show(getSupportFragmentManager(), "transfer dialog");
    }

    @Override
    public void showExceptionDialog(@NonNull final String message) {
        ExceptionDialog exceptionDialog = ExceptionDialog.newInstance(message);
        exceptionDialog.show(getSupportFragmentManager(), "exception dialog");
    }

    @Override
    @NonNull
    public OnClickListener onClickSourceAccount(@NonNull final List<Account> accounts) {
        return (dialogInterface, i) -> mPresenter.onSourceAccountChanged(accounts.get(i));
    }

    @Override
    @NonNull
    public OnClickListener onClickTargetAccount(@NonNull final MainSpinnerAdapter spinnerAdapter) {
        return (dialogInterface, i) -> {
            final Account item;
            item = spinnerAdapter.getItem(i);
            if (item == null) {
                throw new RuntimeException("item is null");
            } else {
                mPresenter.onTargetAccountChanged(item);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}