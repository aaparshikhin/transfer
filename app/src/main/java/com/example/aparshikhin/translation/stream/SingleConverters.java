package com.example.aparshikhin.translation.stream;

import com.example.aparshikhin.translation.model.data.LoadModel;

import io.reactivex.Observable;
import io.reactivex.SingleConverter;
import io.reactivex.annotations.NonNull;

public class SingleConverters {

    @NonNull
    public static <T> SingleConverter<T, Observable<LoadModel<T>>> loadModel() {
        return upstream -> upstream
                .map(LoadModel::data)
                .onErrorReturn(LoadModel::error)
                .toObservable()
                .startWith(LoadModel.loading());
    }

    private SingleConverters() {
    }
}
