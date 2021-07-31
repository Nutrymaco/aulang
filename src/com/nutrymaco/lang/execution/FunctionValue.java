package com.nutrymaco.lang.execution;

// похоже на ReferenceValue, но с аргументами

import java.util.List;

public class FunctionValue implements Value {


    private final List<Expression> expressions;

    private final Value returnValue;

    // локальные переменные могут не скопироваться,
    // подумать над ленивой инициализацией
    // может какая-то система подписки на изменения парентового фрейма
    public FunctionValue(List<Expression> expressions, Value returnValue) {
        this.expressions = expressions;
        this.returnValue = returnValue;
    }

    @Override
    public Object get() {
        expressions.forEach(Expression::perform);
        return returnValue.get();
    }
}

