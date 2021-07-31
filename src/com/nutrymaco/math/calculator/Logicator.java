package com.nutrymaco.math.calculator;

import com.nutrymaco.math.tree.CompareNode;
import com.nutrymaco.math.tree.ExpressionNode;
import com.nutrymaco.math.tree.LogicExpressionNode;
import com.nutrymaco.math.tree.LogicSignNode;

import java.util.Iterator;
import java.util.List;

public class Logicator extends AbstractCalculator {
    private final static char[] FIRST_LEVEL_DELIMITERS = new char[]{'|'};
    private final static char[] SECOND_LEVEL_DELIMITERS = new char[]{'&'};


    public LogicExpressionNode buildTree(String expression) {
        expression = prepareExpression(expression);
        return buildFirstLevelNode(expression);
    }

    private LogicExpressionNode buildFirstLevelNode(String expression) {
        Iterator<String> elements = lightSplit(expression, FIRST_LEVEL_DELIMITERS).iterator();
        String element = elements.next();
        LogicExpressionNode previousNode, currentNode = null;
        if (!elements.hasNext()) {
            return buildSecondLevelNode(element);
        }
        previousNode = buildSecondLevelNode(element);
        while (elements.hasNext()) {
            String sign = elements.next();
            element = elements.next();
            currentNode = LogicSignNode.of(sign, previousNode, buildSecondLevelNode(element));
            previousNode = currentNode;
        }
        return currentNode;
    }

    private LogicExpressionNode buildSecondLevelNode(String expression) {
        Iterator<String> elements = lightSplit(expression, SECOND_LEVEL_DELIMITERS).iterator();
        String element = elements.next();
        LogicExpressionNode previousNode, currentNode = null;
        if (!elements.hasNext()) {
            return buildNode(element);
        }
        previousNode = buildNode(element);
        while (elements.hasNext()) {
            String sign = elements.next();
            element = elements.next();
            currentNode = LogicSignNode.of(sign, previousNode, buildNode(element));
            previousNode = currentNode;
        }
        return currentNode;
    }

    private LogicExpressionNode buildNode(final String expression) {
        if (!containsAny(expression, "|", "&")) {
            return buildCompareNode(expression);
        } else {
            return buildTree(expression);
        }
    }

    // *math expression* *compare sign* *math expression*
    private CompareNode buildCompareNode(String expression) {
        List<Character> charSigns = List.of('<', '>', '=');
        String notEquals = "!=";
        Calculator calculator = new Calculator(variableContext);

        for (int i = 0; i < expression.length() - 1; i++) {
            char symbol = expression.charAt(i);
            if (charSigns.contains(symbol)) {
                ExpressionNode leftNode = calculator.buildTree(expression.substring(0, i));
                ExpressionNode rightNode = calculator.buildTree(expression.substring(i + 1));
                return CompareNode.of(symbol, leftNode, rightNode);
            } else {
                String doubleSign = expression.substring(i, i + 2);
                if (doubleSign.equals(notEquals)) {
                    ExpressionNode leftNode = calculator.buildTree(expression.substring(0, i));
                    ExpressionNode rightNode = calculator.buildTree(expression.substring(i + 2));
                    return CompareNode.of(doubleSign, leftNode, rightNode);
                }
            }
        }
        return null;
    }
}
