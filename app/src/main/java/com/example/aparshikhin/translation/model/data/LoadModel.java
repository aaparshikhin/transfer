package com.example.aparshikhin.translation.model.data;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class LoadModel<T> {

    public enum Status {
        Loading, Successful, Error
    }

    @NonNull
    public static <T> LoadModel<T> error(@NonNull Throwable throwable) {
        return new LoadModel<>(Status.Error, throwable, null, System.currentTimeMillis());
    }

    @NonNull
    public static <T> LoadModel<T> data(@NonNull T data) {
        return new LoadModel<>(Status.Successful, null, data, System.currentTimeMillis());
    }

    @NonNull
    public static <T> LoadModel<T> loading() {
        return new LoadModel<>(Status.Loading, null, null, System.currentTimeMillis());
    }

    @NonNull private final Status mStatus;
    @Nullable
    private final Throwable mThrowable;
    @Nullable private final T mData;
    private final long mTime;

    private LoadModel(@NonNull Status status, @Nullable Throwable throwable, @Nullable T data, long time) {
        this.mStatus = status;
        this.mThrowable = throwable;
        this.mData = data;
        this.mTime = time;
    }

    @NonNull
    public Status getStatus() {
        return mStatus;
    }

    @NonNull
    public Throwable getThrowable() {
        if (mStatus == Status.Error && mThrowable != null) {
            return mThrowable;
        }
        throw new RuntimeException("Status not error and method can't be called");
    }

    @NonNull
    public T getData() {
        if (mStatus == Status.Successful && mData != null) {
            return mData;
        }
        throw new RuntimeException("Data is null, or mStatus not successfull");
    }

    public long getTime() {
        return mTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoadModel<?> loadModel = (LoadModel<?>) o;

        if (mTime != loadModel.mTime) return false;
        if (mStatus != loadModel.mStatus) return false;
        if (mThrowable != null ? !mThrowable.equals(loadModel.mThrowable) : loadModel.mThrowable != null)
            return false;
        return mData != null ? mData.equals(loadModel.mData) : loadModel.mData == null;
    }

    @Override
    public int hashCode() {
        int result = mStatus.hashCode();
        result = 31 * result + (mThrowable != null ? mThrowable.hashCode() : 0);
        result = 31 * result + (mData != null ? mData.hashCode() : 0);
        result = 31 * result + (int) (mTime ^ (mTime >>> 32));
        return result;
    }
}
