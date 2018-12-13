package com.example.aparshikhin.translation.stream;

import android.support.annotation.NonNull;

import com.annimon.stream.Optional;
import com.annimon.stream.function.BiFunction;

public class Optionals {

    @NonNull
    public static <T1, T2, R> Optional<R> combine(@NonNull final Optional<T1> optional,
                                                  @NonNull final Optional<T2> optional2,
                                                  @NonNull final BiFunction<T1, T2, R> combiner) {
        return optional.flatMap(t1 -> optional2.flatMap(t2 -> Optional.ofNullable(combiner.apply(t1, t2))));
    }

//    @NonNull
//    public static <T1, T2, T3, R> Optional<R> combine(@NonNull final Optional<T1> optional1,
//                                                      @NonNull final Optional<T2> optional2,
//                                                      @NonNull final Optional<T3> optional3,
//                                                      @NonNull final Function3<T1, T2, T3, R> combiner) {
//        return optional1.flatMap(t1 -> optional2.flatMap(t2 -> optional3.flatMap(t3 ->
//                Optional.ofNullable(combiner.apply(t1, t2, t3)))));
//    }

    @NonNull
    public static <T1, T2, T3, T4, R> Optional<R> combine(@NonNull final Optional<T1> optional1,
                                                      @NonNull final Optional<T2> optional2,
                                                      @NonNull final Optional<T3> optional3,
                                                      @NonNull final Optional<T4> optional4,
                                                      @NonNull final Function4<T1, T2, T3, T4, R> combiner) {
        return optional1.flatMap(t1 -> optional2.flatMap(t2 -> optional3.flatMap(t3 -> optional4.flatMap(t4 ->
                Optional.ofNullable(combiner.apply(t1, t2, t3, t4))))));
    }
}