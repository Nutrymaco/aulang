package com.nutrymaco.lang.execution;

import java.util.List;

// похоже на ReferenceValue, но с аргументами
public class FunctionCallValue implements Value {

    private final String name;

    private final List<Value> parameters;

    private final Frame frame;

    public FunctionCallValue(String name, List<Value> parameters, Frame frame) {
        this.name = name;
        this.parameters = parameters;
        this.frame = frame;
    }


    @Override
    public Object get() {
        for (int i = parameters.size() - 1; i >= 0; i--) {
            frame.putOnStack(parameters.get(i));
        }

        var functionValue = frame.getFunction(name).apply(frame);

        return functionValue.get();
    }
}
