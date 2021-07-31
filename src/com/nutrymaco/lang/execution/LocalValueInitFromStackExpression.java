package com.nutrymaco.lang.execution;

public class LocalValueInitFromStackExpression implements Expression {

    private final String name;

    private final Frame frame;

    public LocalValueInitFromStackExpression(String name, Frame frame) {
        this.name = name;
        this.frame = frame;
    }


    @Override
    public void perform() {
        frame.putLocalValue(name, frame.getNextStackValue());
    }
}
