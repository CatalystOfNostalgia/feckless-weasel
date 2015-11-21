package com.fecklessweasel.service;

/**
 * 3-Tuple class for returning multiple values from function.
 * @param <TValue1> The first value's type.
 * @param <TValue2> The second value's type.
 * @author Christian Gunderman
 */
public final class Tuple3<TValue1, TValue2, TValue3> extends Tuple {
    /** The third value. */
    public final TValue3 value3;

    /**
     * Creates a new readonly tuple.
     * @param value1 The first value.
     * @param value2 The second value.
     * @param value3 The third value.
     */
    public Tuple3(TValue1 value1, TValue2 value2, TValue3 value3) {
        super(value1, value2);
        this.value3 = value3;
    }
}
