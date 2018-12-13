package com.example.aparshikhin.translation.model.data;

public class TransferException extends Exception {

    public enum Type {AccountException, AmountException}

    private final Type mTypeException;

    public TransferException(String message, Type type) {
        super(message);
        mTypeException = type;
    }

    public Type getTypeException() {
        return mTypeException;
    }
}
