package com.nutrymaco.lang.execution;

import com.nutrymaco.math.tree.LogicExpressionNode;

public class LogicValue implements Value {

    private final LogicExpressionNode logicExpressionNode;

    public LogicValue(LogicExpressionNode logicExpressionNode) {
        this.logicExpressionNode = logicExpressionNode;
    }

    @Override
    public Boolean get() {
        return logicExpressionNode.calculate();
    }
}
