package com.nutrymaco.lang.execution;

import com.nutrymaco.lang.parsing.Parser;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionInitExpression implements Expression {

    private final String name;

    private final List<String> parameters;

    private final List<Function<Frame, Expression>> expressions;

    private final Function<Frame, Value> returnValue;

    private final Frame frame;

    public FunctionInitExpression(String name,
                                  List<String> parameters,
                                  List<Function<Frame, Expression>> expressions,
                                  Function<Frame, Value> returnValue,
                                  Frame frame) {
        this.name = name;
        this.parameters = parameters;
        this.expressions = expressions;
        this.returnValue = returnValue;
        this.frame = frame;
    }

    @Override
    public void perform() {
        List<Function<Frame, Expression>> parametersInit = parameters.stream()
                .map(name -> (Function<Frame, Expression>)(Frame frame) -> new LocalValueInitFromStackExpression(name, frame))
                .collect(Collectors.toList());

        var allExpressions = List.of(parametersInit, expressions).stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Function<Frame, FunctionValue> functionValue = frame -> new FunctionValue(
                allExpressions.stream().map(expression -> expression.apply(frame)).collect(Collectors.toList()),
                returnValue.apply(frame));

        frame.putFunction(name, functionValue);
    }
}
