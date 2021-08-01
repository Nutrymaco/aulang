package com.nutrymaco.lang.execution;

import com.nutrymaco.math.calculator.Calculator;

public class CalculableValue implements Value {

    private final Calculator calculator;

    private final Frame frame;

    public CalculableValue(Calculator calculator, Frame frame) {
        this.calculator = calculator;
        this.frame = frame;
    }

    @Override
    public Object get() {
        // init vars in calc
        return calculator.getResult();
    }
}
