package com.fecklessweasel.service;

/**
 * 4-Tuple class for returning multiple values from function.
 * @param <TValue1> The first value's type.
 * @param <TValue2> The second value's type.
 * @param <TValue3> The third value's type.
 * @param <TValue4> The fourth value's type.
 * @param <TValue5> The fifth value's type.
 * @author Hayden Schmackpfeffer
 */
public final class Tuple5<TValue1, TValue2, TValue3, TValue4, TValue5> extends Tuple4 {
    /** The fifth value. */
    public final TValue5 value5;

    /**
     * Creates a new readonly tuple.
     * @param value1 The first value.
     * @param value2 The second value.
     * @param value3 The third value.
     * @param value4 The fourth value.
     * @param value5 The fifth value.
     */
    public Tuple5(TValue1 value1,
                  TValue2 value2,
                  TValue3 value3,
                  TValue4 value4,
                  TValue5 value5) {
        super(value1, value2, value3, value4);
        this.value5 = value5;
    }
}
