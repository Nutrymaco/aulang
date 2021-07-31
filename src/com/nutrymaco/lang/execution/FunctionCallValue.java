package com.nutrymaco.lang.execution;

import java.util.List;

public class FunctionCallValue implements Value {

    private final String name;

    private final List<Value> parameters;

    private final Frame frame;

    // локальные переменные могут не скопироваться,
    // подумать над ленивой инициализацией
    // может какая-то система подписки на изменения парентового фрейма
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

        var functionValue = frame.getLocalValue(name);

        return functionValue.get();
    }
}
