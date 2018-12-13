package com.example.aparshikhin.translation.stream;

import io.reactivex.annotations.NonNull;

/**
 * A functional interface (callback) that computes a value based on multiple input values.
 * @param <T1> the first value type
 * @param <T2> the second value type
 * @param <T3> the third value type
 * @param <T4> the fourth value type
 * @param <R> the result type
 */
public interface Function4<T1, T2, T3, T4, R> {
    /**
     * Calculate a value based on the input values.
     * @param t1 the first value
     * @param t2 the second value
     * @param t3 the third value
     * @param t4 the fourth value
     * @return the result value
     * @throws Exception on error
     */
    @NonNull
    R apply(@NonNull T1 t1, @NonNull T2 t2, @NonNull T3 t3, @NonNull T4 t4);
}
