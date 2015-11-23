package com.fecklessweasel.service;

/**
 * 4-Tuple class for returning multiple values from function.
 * @param <TValue1> The first value's type.
 * @param <TValue2> The second value's type.
 * @param <TValue3> The third value's type.
 * @author Christian Gunderman
 */
public final class Tuple4<TValue1, TValue2, TValue3, TValue4> extends Tuple3 {
    /** The fourth value. */
    public final TValue4 value4;

    /**
     * Creates a new readonly tuple.
     * @param value1 The first value.
     * @param value2 The second value.
     * @param value3 The third value.
     */
    public Tuple4(TValue1 value1,
                  TValue2 value2,
                  TValue3 value3,
                  TValue4 value4) {
        super(value1, value2, value3);
        this.value4 = value4;
    }
}
