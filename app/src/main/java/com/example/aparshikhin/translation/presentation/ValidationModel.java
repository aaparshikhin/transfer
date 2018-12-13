package com.example.aparshikhin.translation.presentation;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;

public class ValidationModel {

    @NonNull
    public static ValidationModel inValid(@NonNull String message) {
        return new ValidationModel(Optional.of(message), false);
    }

    @NonNull
    public static ValidationModel valid() {
        return new ValidationModel(Optional.empty(), true);
    }

    @NonNull
    private final Optional<String> mMessage;
    private final boolean isValid;

    private ValidationModel(@NonNull Optional<String> message, boolean resultOfValidation) {
        this.mMessage = message;
        this.isValid = resultOfValidation;
    }

    @NonNull
    public Optional<String> getMessage() {
        return mMessage;
    }

    public boolean isValid() {
        return isValid;
    }

    @Override
    public String toString() {
        return "ValidationModel{" +
                "mMessage=" + mMessage +
                ", isValid=" + isValid +
                '}';
    }
}
