package com.nutrymaco.lang.execution;

import java.util.List;


public class FunctionValue implements Value {

    private final List<Expression> expressions;

    private final Value returnValue;


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

