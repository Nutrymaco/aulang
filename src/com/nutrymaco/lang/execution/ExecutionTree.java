package com.nutrymaco.lang.execution;

import java.util.List;

public class ExecutionTree {

    private final List<Expression> expressions;

    public ExecutionTree(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public void execute() {
        expressions.forEach(Expression::perform);
    }
}
