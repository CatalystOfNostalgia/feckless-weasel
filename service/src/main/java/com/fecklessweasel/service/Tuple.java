package com.fecklessweasel.service;

/**
 * 2-Tuple class for returning multiple values from function.
 * @param <TValue1> The first value's type.
 * @param <TValue2> The second value's type.
 * @author Christian Gunderman
 */
public final class Tuple<TValue1, TValue2> {
    /** The first value. */
    public final TValue1 value1;
    /** The second value. */
    public final TValue2 value2;

    /**
     * Creates a new readonly tuple.
     * @param value1 The first value.
     * @param value2 The second value.
     */
    public Tuple(TValue1 value1, TValue2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }
}
