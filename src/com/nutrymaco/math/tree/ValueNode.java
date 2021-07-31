package com.nutrymaco.math.tree;

public class ValueNode implements ExpressionNode {
    private final double value;

    private ValueNode(double value) {
        this.value = value;
    }

    public static ValueNode of(double value) {
        return new ValueNode(value);
    }

    public static ValueNode of(String value) {
        return new ValueNode(Double.parseDouble(value));
    }

    public double getValue() {
        return value;
    }

    @Override
    public double calculate() {
        return value;
    }
}
