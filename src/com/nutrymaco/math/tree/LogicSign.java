package com.nutrymaco.math.tree;

public enum LogicSign {
    AND, OR;

    public boolean calculate(boolean b1, boolean b2) {
        return switch (this) {
            case AND -> b1 & b2;
            case OR -> b1 | b2;
        };
    }
}
