package com.nutrymaco.lang.execution;

public class ConstantInitExpression implements Expression {

    private final String name;

    private final Value value;

    private final Frame frame;


    public ConstantInitExpression(String name, Value value, Frame frame) {
        this.name = name;
        this.value = value;
        this.frame = frame;
    }


    @Override
    public void perform() {
        frame.putLocalValue(name, value);
    }
}
