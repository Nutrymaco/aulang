package com.nutrymaco.math.tree;

public enum CompareSign {
    MORE, LESS, EQUALS, NOT_EQUALS;

    public boolean calculate(double d1, double d2) {
        return switch (this) {
            case MORE -> d2 > d1;
            case LESS -> d2 < d1;
            case EQUALS -> d1 == d2;
            case NOT_EQUALS -> d1 != d2;
        };
    }
}
