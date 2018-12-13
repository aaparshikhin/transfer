package com.example.aparshikhin.translation.model.data;

import java.math.BigDecimal;

import io.reactivex.annotations.NonNull;

public class Commission {

    public enum TypeCommission {Percent, Fixed, Zero}

    @NonNull
    public static Commission createWithZero() {
        Money money = new Money(BigDecimal.ZERO, Currency.RUB);
        return new Commission(money, BigDecimal.ZERO, TypeCommission.Zero);
    }

    @NonNull
    public static Commission createWithPercent(@NonNull final Money commission, @NonNull final BigDecimal percent) {
        return new Commission(commission, percent, TypeCommission.Percent);
    }

    @NonNull
    public static Commission createFixed(@NonNull final Money commission) {
        return new Commission(commission, commission.getAmount(), TypeCommission.Fixed);
    }

    @NonNull
    private final Money mCommission;
    @NonNull
    private final BigDecimal mValue;
    private final TypeCommission mTypeCommission;

    private Commission(@NonNull final Money commission,@NonNull final BigDecimal percent,
                       @NonNull final TypeCommission typeCommission) {
        mCommission = commission;
        mValue = percent;
        mTypeCommission = typeCommission;
    }

    @NonNull
    public Money getCommission() {
        return mCommission;
    }

    @NonNull
    public BigDecimal getPercent() {
        return mValue;
    }

    @NonNull
    public TypeCommission getTypeCommission() {
        return mTypeCommission;
    }
}
