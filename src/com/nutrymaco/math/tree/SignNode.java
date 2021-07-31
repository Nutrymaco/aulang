package com.nutrymaco.math.tree;

public class SignNode implements ExpressionNode {
    private final Sign sign;
    private final ExpressionNode leftNode;
    private final ExpressionNode rightNode;

    private SignNode(Sign sign, ExpressionNode leftNode, ExpressionNode rightNode) {
        this.sign = sign;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public static SignNode of(Sign sign, ExpressionNode leftNode, ExpressionNode rightNode) {
        return new SignNode(sign, leftNode, rightNode);
    }

    public static SignNode of(String sign, ExpressionNode leftNode, ExpressionNode rightNode) {
        return new SignNode(signValueToEnum(sign), leftNode, rightNode);
    }

    private static Sign signValueToEnum(String sign) {
        return switch (sign) {
            case "+" -> Sign.PLUS;
            case "-" -> Sign.MINUS;
            case "*" -> Sign.MUL;
            case "/" -> Sign.DIV;
            case "%" -> Sign.MOD;
            default -> throw new IllegalArgumentException(
                    String.format("sign %s not represented in enum", sign));
        };
    }

    public Sign getSign() {
        return sign;
    }

    public ExpressionNode getLeftNode() {
        return leftNode;
    }

    public ExpressionNode getRightNode() {
        return rightNode;
    }

    @Override
    public double calculate() {
        return sign.calculate(leftNode.calculate(), rightNode.calculate());
    }
}
