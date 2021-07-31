package com.nutrymaco.lang.execution;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionInitExpression implements Expression {

    private final String name;

    private final List<String> parameters;

    private final List<Expression> expressions;

    private final Value returnValue;

    private final Frame frame;

    public FunctionInitExpression(String name,
                                  List<String> parameters,
                                  List<Expression> expressions,
                                  Value returnValue,
                                  Frame frame) {
        this.name = name;
        this.parameters = parameters;
        this.expressions = expressions;
        this.returnValue = returnValue;
        this.frame = frame;
    }

    @Override
    public void perform() {
        List<Expression> parametersInit = parameters.stream()
                .map(name -> new LocalValueInitFromStackExpression(name, frame))
                .collect(Collectors.toList());

        var allExpressions = List.of(parametersInit, expressions).stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        var functionValue = new FunctionValue(allExpressions, returnValue);

        frame.putLocalValue(name, functionValue);
    }
}
