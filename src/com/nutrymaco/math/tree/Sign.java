package com.nutrymaco.math.tree;

public enum Sign {
    PLUS, MINUS, MUL, DIV, MOD;
    public double calculate(double d1, double d2) {
        return switch (this) {
            case MINUS -> d1 - d2;
            case PLUS -> d1 + d2;
            case MUL -> d1 * d2;
            case DIV -> d1 / d2;
            case MOD -> d1 % d2;
        };
    }
}
