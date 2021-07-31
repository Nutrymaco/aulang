package com.nutrymaco.math.calculator;


import com.nutrymaco.math.tree.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Calculator extends AbstractCalculator {
    private final static char OPEN_BRACE = '(';
    private final static char CLOSE_BRACE = ')';
    private final static char[] FIRST_LEVEL_DELIMITERS = new char[]{'+', '-'};
    private final static char[] SECOND_LEVEL_DELIMITERS = new char[]{'*', '/', '%'};
    private final static String NUMBER_REGEX = "[1-9][0-9]*(\\.[0-9]*[1-9])?";
    private final static String VAR_REGEX = "[a-zA-Z]+";
    private final static String FUN_REGEX = "ROUND\\(.+";

    public Calculator() {
        super();
    }

    public Calculator(VariableContext variableContext) {
        super(variableContext);
    }

    public ExpressionNode buildTree(String expression) {
        expression = prepareExpression(expression);
        return buildFirstLevel(expression);
    }

    private ExpressionNode buildFirstLevel(String expression) {
        Iterator<String> elements = lightSplit(expression, FIRST_LEVEL_DELIMITERS).iterator();
        String element = elements.next();
        ExpressionNode previousNode, currentNode = null;
        if (!elements.hasNext()) {
            return buildSecondLevelNode(element);
        }
        previousNode = buildSecondLevelNode(element);
        while (elements.hasNext()) {
            String sign = elements.next();
            element = elements.next();
            currentNode = SignNode.of(sign, previousNode, buildSecondLevelNode(element));
            previousNode = currentNode;
        }
        return currentNode;
    }

    private ExpressionNode buildSecondLevelNode(String expression) {
        Iterator<String> elements = lightSplit(expression, SECOND_LEVEL_DELIMITERS).iterator();
        String element = elements.next();
        ExpressionNode previousNode, currentNode = null;
        if (!elements.hasNext()) {
            return buildNode(element);
        }
        previousNode = buildNode(element);
        while (elements.hasNext()) {
            String sign = elements.next();
            element = elements.next();
            currentNode = SignNode.of(sign, previousNode, buildNode(element));
            previousNode = currentNode;
        }
        return currentNode;
    }

    private FuncNode getFuncNodeFromFunCall(String funCall) {
        int firstOpenBrace = funCall.indexOf(OPEN_BRACE);
        String arguments = funCall.substring(firstOpenBrace + 1, funCall.length() - 1);
        return FuncNode.of(funCall.substring(0, firstOpenBrace),
               Arrays.stream(arguments.split(","))
                       .map(this::buildTree)
                       .collect(Collectors.toList()));
    }

    private ExpressionNode buildNode(String expression) {
        if (expression.matches(NUMBER_REGEX)) {
            return ValueNode.of(expression);
        } else if (expression.matches(FUN_REGEX)) {
            return getFuncNodeFromFunCall(expression);
        } else if (expression.matches(VAR_REGEX)) {
            return VarNode.of(variableContext, expression);
        } else {
            return buildTree(expression);
        }
    }



    private static String getRegexFromValues(List<String> values) {
        return String.join("|", values);
    }


}
