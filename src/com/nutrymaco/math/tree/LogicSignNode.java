package com.nutrymaco.math.tree;

public class LogicSignNode implements LogicExpressionNode {
    private final LogicSign sign;
    private final LogicExpressionNode leftNode;
    private final LogicExpressionNode rightNode;

    private LogicSignNode(LogicSign sign, LogicExpressionNode leftNode, LogicExpressionNode rightNode) {
        this.sign = sign;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public static LogicSignNode of(String sign, LogicExpressionNode leftNode, LogicExpressionNode rightNode) {
        return new LogicSignNode(mapToSign(sign), leftNode, rightNode);
    }

    public static LogicSignNode of(char sign, LogicExpressionNode leftNode, LogicExpressionNode rightNode) {
        return of(String.valueOf(sign), leftNode, rightNode);
    }

    private static LogicSign mapToSign(String sign) {
        return switch (sign) {
            case "|" -> LogicSign.OR;
            case "&" -> LogicSign.AND;
            default -> throw new IllegalArgumentException(String.format("this is wrong sign - %s", sign));
        };
    }

    @Override
    public boolean calculate() {
        return sign.calculate(leftNode.calculate(), rightNode.calculate());
    }
}
