package com.mct.base.ui.transition.options;

enum AnimOptionsStorage {

    // current use 9 bit of int
    TYPE(1), STYLE(4), DIRECTION(3), OVERLAY(1);

    static {
        int bitSum = 0;
        for (AnimOptionsStorage option : values()) {
            option.startBit = bitSum;
            bitSum += option.totalBit;
        }
        if (bitSum > 31) {
            throw new IllegalArgumentException("The sum of totalBit must not exceed 31.");
        }
    }

    private int startBit;
    private final int totalBit;
    private final int mask;

    AnimOptionsStorage(int totalBit) {
        if (totalBit > 31) {
            throw new IllegalArgumentException("totalBit cannot exceed 31.");
        }
        this.totalBit = totalBit;
        this.mask = (1 << totalBit) - 1;
    }

    public int set(int options, int value) {
        if (value < 0 || value > mask) {
            throw new IllegalArgumentException(String.format("Value of %s must be between 0 and %d.", name(), mask));
        }
        return options | (value << startBit);
    }

    public int get(int options) {
        return (options >> startBit) & mask;
    }
}
