package com.nutrymaco.lang.execution;

import java.util.List;

public class NativeFunctionCallExpression implements Expression {

    private final NativeFunctionType type;

    private final List<Value> args;

    public NativeFunctionCallExpression(NativeFunctionType type, List<Value> args) {
        this.type = type;
        this.args = args;
    }

    public enum NativeFunctionType {
        PRINT,
    }

    @Override
    public void perform() {
         switch (type) {
            case PRINT -> args.stream().map(Value::get).forEach(System.out::println);
         };
    }
}
