package com.nutrymaco.math.tree;

public class CompareNode implements LogicExpressionNode {
    private final CompareSign sign;
    private final ExpressionNode leftNode;
    private final ExpressionNode rightNode;

    private CompareNode(CompareSign sign, ExpressionNode leftNode, ExpressionNode rightNode) {
        this.sign = sign;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public static CompareNode of(String sign, ExpressionNode leftNode, ExpressionNode rightNode) {
        return new CompareNode(mapToSign(sign), leftNode, rightNode);
    }

    public static CompareNode of(char sign, ExpressionNode leftNode, ExpressionNode rightNode) {
        return new CompareNode(mapToSign(String.valueOf(sign)), leftNode, rightNode);
    }

    private static CompareSign mapToSign(String sign) {
        return switch (sign) {
            case "<" -> CompareSign.MORE;
            case ">" -> CompareSign.LESS;
            case "=" -> CompareSign.EQUALS;
            case "!=" -> CompareSign.NOT_EQUALS;
            default -> throw new IllegalArgumentException(String.format("not right compare sign - %s", sign));
        };
    }

    @Override
    public boolean calculate() {
        return sign.calculate(leftNode.calculate(), rightNode.calculate());
    }
}
