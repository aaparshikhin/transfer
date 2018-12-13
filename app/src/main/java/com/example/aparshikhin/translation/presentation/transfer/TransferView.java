package com.example.aparshikhin.translation.presentation.transfer;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.aparshikhin.translation.model.data.Transfer;

public interface TransferView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void render(@NonNull final TransferViewState viewState);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showTransferDialog(@NonNull final Transfer transfer);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showExceptionDialog(@NonNull final String message);
}
