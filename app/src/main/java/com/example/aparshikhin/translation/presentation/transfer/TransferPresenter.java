package com.example.aparshikhin.translation.presentation.transfer;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.arellomobile.mvp.InjectViewState;
import com.example.aparshikhin.translation.model.data.Account;
import com.example.aparshikhin.translation.model.data.AccountException;
import com.example.aparshikhin.translation.model.data.AmountException;
import com.example.aparshikhin.translation.model.data.Commission;
import com.example.aparshikhin.translation.model.data.LoadModel;
import com.example.aparshikhin.translation.model.data.Money;
import com.example.aparshikhin.translation.model.data.Transfer;
import com.example.aparshikhin.translation.model.repository.Repository;
import com.example.aparshikhin.translation.presentation.BasePresenter;
import com.example.aparshikhin.translation.presentation.SelectionPartialStates;
import com.example.aparshikhin.translation.presentation.SelectionPartialStates.Items;
import com.example.aparshikhin.translation.presentation.SelectionPartialStates.WithBiSelector;
import com.example.aparshikhin.translation.presentation.SelectionPartialStates.WithSelector;
import com.example.aparshikhin.translation.presentation.SelectionState;
import com.example.aparshikhin.translation.presentation.Selectors;
import com.example.aparshikhin.translation.presentation.ValidationModel;
import com.example.aparshikhin.translation.presentation.Validators;
import com.example.aparshikhin.translation.presentation.transfer.PartialStates.EnableAll;
import com.example.aparshikhin.translation.presentation.transfer.PartialStates.ResultTransfer;
import com.example.aparshikhin.translation.presentation.transfer.PartialStates.SourceAccount;
import com.example.aparshikhin.translation.presentation.transfer.PartialStates.TargetAccount;
import com.example.aparshikhin.translation.presentation.transfer.PartialStates.TargetAccounts;
import com.example.aparshikhin.translation.presentation.transfer.PartialStates.ValidationAccounts;
import com.example.aparshikhin.translation.presentation.transfer.PartialStates.ValidationAmount;
import com.example.aparshikhin.translation.stream.Optionals;
import com.example.aparshikhin.translation.stream.SingleConverters;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static com.example.aparshikhin.translation.presentation.SelectionPartialStates.ChoosedAccount;
import static com.example.aparshikhin.translation.presentation.transfer.PartialStates.CommissionState;
import static com.example.aparshikhin.translation.presentation.transfer.PartialStates.SourcesAccounts;

@InjectViewState
public class TransferPresenter extends BasePresenter<TransferView> {

    @NonNull
    private final Repository mRepository;
    @NonNull
    private final PublishSubject<Account> mChangeSourceAccountsSubject;
    @NonNull
    private final PublishSubject<Account> mChangeTargetAccountSubject;
    @NonNull
    private final PublishSubject<Optional<BigDecimal>> mChangeAmountSubject;
    @NonNull
    private final PublishSubject<Transfer> mSendTransferSubject;

    public TransferPresenter(@NonNull final Repository repository) {
        super();
        mRepository = repository;
        mChangeSourceAccountsSubject = PublishSubject.create();
        mChangeTargetAccountSubject = PublishSubject.create();
        mChangeAmountSubject = PublishSubject.create();
        mSendTransferSubject = PublishSubject.create();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        /*
         *  Source Account
         */
        Observable<LoadModel<List<Account>>> rawSourceAccountsObservable = mRepository.sourceList()
                .observeOn(Schedulers.computation())
                .as(SingleConverters.loadModel())
                .share()
                .cacheWithInitialCapacity(1);

        Observable<WithSelector> sourceWithSelectorAccountObservable = rawSourceAccountsObservable
                .flatMap(Selectors::filterAccounts)
                .take(1)
                .map(listLoadModel ->
                        new SelectionPartialStates.WithSelector(listLoadModel, Selectors.first()));

        Observable<ChoosedAccount> rawChangeSourceAccountObservable = mChangeSourceAccountsSubject
                .observeOn(Schedulers.computation())
                .map(Optional::of)
                .map(ChoosedAccount::new)
                .share()
                .cacheWithInitialCapacity(1);

        Observable<Optional<Account>> rawSelectedSourceAccountObservable = Observable
                .merge(rawChangeSourceAccountObservable, sourceWithSelectorAccountObservable)
                .observeOn(Schedulers.computation())
                .scan(SelectionState.create(), (previousState, choosedAccount) -> choosedAccount
                        .apply(previousState))
                .skip(1)
                .map(SelectionState::getAccount)
                .share()
                .cacheWithInitialCapacity(1);

        /*
         *  Target Account
         */
        Observable<LoadModel<List<Account>>> rawTargetAccountsObservable = rawSelectedSourceAccountObservable
                .switchMap(accountOptional -> mRepository.targetList(accountOptional)
                        .as(SingleConverters.loadModel()))
                .cacheWithInitialCapacity(1);

        Observable<ChoosedAccount> rawChangeTargetAccountObservable = mChangeTargetAccountSubject
                .observeOn(Schedulers.computation())
                .map(Optional::of)
                .map(ChoosedAccount::new)
                .share()
                .cacheWithInitialCapacity(1);

        Observable<List<Account>> targetAccountsObservable = rawTargetAccountsObservable
                .flatMap(Selectors::filterAccounts)
                .share()
                .cacheWithInitialCapacity(1);

        Observable<Items> changeTargetAccountObservable = targetAccountsObservable
                .skip(1)
                .map(Items::new)
                .share()
                .cacheWithInitialCapacity(1);

        Observable<WithBiSelector> targetAccountsWithSelectorObservable = Observable
                .combineLatest(targetAccountsObservable,
                        rawSelectedSourceAccountObservable,
                        (accounts, accountOptional) ->
                                new WithBiSelector(accounts, accountOptional,
                                        Selectors.chooseDefaultTarget()))
                .observeOn(Schedulers.computation())
                .take(1)
                .share()
                .cacheWithInitialCapacity(1);

        Observable<Optional<Account>> rawSelectedTargetAccountObservable = Observable
                .merge(rawChangeTargetAccountObservable, targetAccountsWithSelectorObservable, changeTargetAccountObservable)
                .observeOn(Schedulers.computation())
                .scan(SelectionState.create(), (previousState, choosedAccount) ->
                        choosedAccount.apply(previousState))
                .skip(1)
                .map(SelectionState::getAccount)
                .share()
                .cacheWithInitialCapacity(1);

        /*
         *  Money
         */
        Observable<Optional<BigDecimal>> amountObservable = mChangeAmountSubject
                .distinctUntilChanged();

        Observable<Optional<Money>> changeAmountObservable = Observable
                .combineLatest(
                        amountObservable,
                        rawSelectedSourceAccountObservable,
                        createMoney());

        /*
         * Get Transfer from repository
         */
        Observable<LoadModel<Transfer>> rawSendTransferObservable = mSendTransferSubject
                .observeOn(Schedulers.computation())
                .switchMap(transfer -> mRepository.validateTransfer(transfer)
                        .as(SingleConverters.loadModel()))
                .share()
                .cacheWithInitialCapacity(1);

        /*
         *  Validation Account
         */
        Observable<ValidationModel> rawValidationAccountOfTransferObservable = rawSendTransferObservable
                .filter(transferLoadModel -> transferLoadModel.getStatus() == LoadModel.Status.Error)
                .map(LoadModel::getThrowable)
                .filter(throwable -> throwable instanceof AccountException)
                .map(Throwable::getMessage)
                .map(ValidationModel::inValid)
                .share()
                .cacheWithInitialCapacity(1);

        Observable<ValidationModel> validationAccountsLocalObservable = Observable
                .combineLatest(
                        rawSelectedSourceAccountObservable,
                        rawSelectedTargetAccountObservable,
                        Validators.validateAccounts())
                .observeOn(Schedulers.computation())
                .mergeWith(rawValidationAccountOfTransferObservable)
                .share()
                .cacheWithInitialCapacity(1);

        Observable<ValidationModel> validationAccountsObservable = Observable
                .merge(validationAccountsLocalObservable, rawValidationAccountOfTransferObservable);

        /*
         *  Validataion Amount
         */
        Observable<ValidationModel> rawValidationAmountOfTransferFromRepositoryObservable = rawSendTransferObservable
                .filter(transferLoadModel -> transferLoadModel.getStatus() == LoadModel.Status.Error)
                .map(LoadModel::getThrowable)
                .filter(throwable -> throwable instanceof AmountException)
                .map(Throwable::getMessage)
                .map(ValidationModel::inValid)
                .share()
                .cacheWithInitialCapacity(1);

        Observable<ValidationModel> validationAmountLocalObservable = Observable
                .combineLatest(
                        rawSelectedSourceAccountObservable,
                        changeAmountObservable,
                        Validators.validateAmount())
                .mergeWith(rawValidationAmountOfTransferFromRepositoryObservable)
                .observeOn(Schedulers.computation())
                .share()
                .cacheWithInitialCapacity(1);

        Observable<ValidationModel> validationAmountObservable = Observable
                .merge(rawValidationAmountOfTransferFromRepositoryObservable, validationAmountLocalObservable);

        /*
         *  Commission
         */
        Observable<Optional<LoadModel<Commission>>> commissionObservable = Observable
                .combineLatest(
                        rawSelectedSourceAccountObservable,
                        changeAmountObservable,
                        getCommissionBiFunction())
                .debounce(700, TimeUnit.MILLISECONDS, Schedulers.computation())
                .switchMap(Functions.identity());

        /*
         *  Transfer
         */
        Observable<Optional<Transfer>> rawTransferObservable = Observable
                .combineLatest(
                        validationAccountsObservable,
                        validationAmountObservable,
                        rawSelectedSourceAccountObservable,
                        rawSelectedTargetAccountObservable,
                        changeAmountObservable,
                        commissionObservable,
                        TransferFactory.createTransfer());

        Observable<Boolean> rawEnableAllViews = rawSendTransferObservable
                .map(transferLoadModel -> transferLoadModel.getStatus() != LoadModel.Status.Loading);

        /*
         * Show Dialog
         */
        rawSendTransferObservable
                .filter(transferLoadModel -> transferLoadModel.getStatus() == LoadModel.Status.Successful)
                .map(LoadModel::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable())
                .subscribe(getViewState()::showTransferDialog);

        rawSendTransferObservable
                .filter(transferLoadModel -> transferLoadModel.getStatus() == LoadModel.Status.Error)
                .map(LoadModel::getThrowable)
                .filter(throwable -> throwable instanceof IOException)
                .map(Throwable::getMessage)
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable())
                .subscribe(getViewState()::showExceptionDialog);

        /*
         *  Partial states of observables
         */
        Observable<SourcesAccounts> partialSourceAccounts = rawSourceAccountsObservable
                .map(SourcesAccounts::new);

        Observable<SourceAccount> partialSourceAccount = rawSelectedSourceAccountObservable
                .map(SourceAccount::new);

        Observable<TargetAccounts> partialTargetAccounts = rawTargetAccountsObservable
                .map(TargetAccounts::new);

        Observable<TargetAccount> partialTargetAccount = rawSelectedTargetAccountObservable
                .map(TargetAccount::new);

        Observable<ValidationAccounts> partialValidationAccounts = validationAccountsObservable
                .map(ValidationAccounts::new);

        Observable<ValidationAmount> partialValidationAmount = validationAmountObservable
                .map(ValidationAmount::new);

        Observable<ResultTransfer> partialTransfer = rawTransferObservable
                .map(ResultTransfer::new);

        Observable<CommissionState> partialCommission = commissionObservable
                .map(CommissionState::new);

        Observable<EnableAll> partialValidationTransfer = rawEnableAllViews
                .map(EnableAll::new);

        /*
         * Render view state
         */
        List<Observable<? extends PartialStates>> partialStates = Arrays.asList(
                partialSourceAccounts,
                partialSourceAccount,
                partialTargetAccounts,
                partialTargetAccount,
                partialValidationAccounts,
                partialValidationAmount,
                partialTransfer,
                partialCommission,
                partialValidationTransfer);

        Observable.merge(partialStates)
                .scan(TransferViewState.create(), (previousViewState, partialState) -> partialState
                        .apply(previousViewState))
                .skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable())
                .subscribe(getViewState()::render);
    }

    @NonNull
    private BiFunction<Optional<Account>, Optional<Money>, Observable<Optional<LoadModel<Commission>>>> getCommissionBiFunction() {
        return (optionalAccount, optionalMoney) -> Optionals.combine(optionalAccount, optionalMoney, (account, money) ->
                mRepository.commission(account, money)
                        .as(SingleConverters.loadModel())
                        .map(Optional::of))
                .orElse(Observable.just(Optional.empty()));
    }

    @NonNull
    private BiFunction<Optional<BigDecimal>, Optional<Account>, Optional<Money>> createMoney() {
        return (bigDecimal, accountOptional) ->
                Optionals.combine(bigDecimal, accountOptional, this::createMoney)
                        .orElse(Optional.empty());
    }

    @NonNull
    private Optional<Money> createMoney(@NonNull final BigDecimal amount,
                                        @NonNull final Account sourceAccount) {
        return Optional.of(new Money(amount, sourceAccount.getBalance().getCurrency()));
    }

    public void onSourceAccountChanged(@NonNull final Account account) {
        mChangeSourceAccountsSubject.onNext(account);
    }

    public void onTargetAccountChanged(@NonNull final Account account) {
        mChangeTargetAccountSubject.onNext(account);
    }

    public void onAmountChanged(@NonNull final Optional<BigDecimal> number) {
        mChangeAmountSubject.onNext(number);
    }

    public void onSendTransfer(@NonNull final Transfer transfer) {
        mSendTransferSubject.onNext(transfer);
    }
}
