package com.nutrymaco.math.tree;

import java.util.List;

public class FuncNode implements ExpressionNode {
    private final Func func;
    private final List<ExpressionNode> arguments;

    private FuncNode(Func func, List<ExpressionNode> arguments) {
        this.func = func;
        this.arguments = arguments;
    }

    public static FuncNode of(Func func, List<ExpressionNode> arguments) {
        return new FuncNode(func, arguments);
    }

    public static FuncNode of(String func, List<ExpressionNode> arguments) {
        return new FuncNode(Func.valueOf(func), arguments);
    }

    public Func getFunc() {
        return func;
    }

    public List<ExpressionNode> getArguments() {
        return arguments;
    }

    @Override
    public double calculate() {
        return switch (func) {
            case ROUND -> Math.round(arguments.get(0).calculate());
        };
    }
}
