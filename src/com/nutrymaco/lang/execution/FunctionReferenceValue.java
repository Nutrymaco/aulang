package com.nutrymaco.lang.execution;

import java.util.function.Function;

public class FunctionReferenceValue implements Value {

    private final Function<Frame, Expression> functionInitializer;
    private final String origFunName;

    // this is must be FunctionInitExpression
    public FunctionReferenceValue(String origFunName, Function<Frame, Expression> functionInitializer) {
        this.origFunName = origFunName;
        this.functionInitializer = functionInitializer;
    }

    @Override
    public Function<Frame, String> get() {
        return frame -> {
            functionInitializer.apply(frame).perform();
            return origFunName;
        };
    }
}
