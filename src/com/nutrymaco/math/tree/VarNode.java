package com.nutrymaco.math.tree;

public class VarNode implements ExpressionNode {
    private final VariableContext variableContext;
    private final String variable;

    private VarNode(VariableContext variableContext, String variable) {
        this.variableContext = variableContext;
        this.variable = variable;
    }

    public static VarNode of(VariableContext variableContext, String variable) {
        return new VarNode(variableContext, variable);
    }

    @Override
    public double calculate() {
        return variableContext.getValueOfVariable(variable);
    }
}
