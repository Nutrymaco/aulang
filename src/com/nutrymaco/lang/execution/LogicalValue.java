package com.nutrymaco.lang.execution;

import com.nutrymaco.math.calculator.Logicator;

public class LogicalValue implements Value {

    private final Logicator logicator;

    private final Frame frame;

    public LogicalValue(Logicator logicator, Frame frame) {
        this.logicator = logicator;
        this.frame = frame;
    }

    @Override
    public Object get() {
        return logicator.getResult();
    }
}
