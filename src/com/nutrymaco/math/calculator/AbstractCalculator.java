package com.nutrymaco.math.calculator;

import com.nutrymaco.math.tree.VariableContext;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCalculator {
    private static final char OPEN_BRACE = '(';
    private static final char CLOSE_BRACE = ')';
    final VariableContext variableContext;

    AbstractCalculator() {
        this(new VariableContext());
    }

    AbstractCalculator(VariableContext variableContext) {
        this.variableContext = variableContext;
    }

    public AbstractCalculator setValue(String varName, double value) {
        variableContext.setValue(varName, value);
        return this;
    }

    // (2 + 3) * 5 + 3 * (4 - 5) -> [(2 + 3) * 5, + , 3 * (4 - 5)]
    static List<String> lightSplit(String string, char[] delimiters) {
        List<String> parts = new ArrayList<>();
        StringBuffer currentPart = new StringBuffer();
        boolean inBraces = false;
        int countOfBraces = 0;
        for (char element : string.toCharArray()) {

            if (element == OPEN_BRACE) {
                countOfBraces++;
                currentPart.append(element);
            } else if (element == CLOSE_BRACE) {
                countOfBraces--;
                currentPart.append(element);
            } else if (equalsAny(element, delimiters) && !inBraces) {
                parts.add(currentPart.toString());
                parts.add(String.valueOf(element));
                currentPart = new StringBuffer();
            } else {
                currentPart.append(element);
            }
            inBraces = countOfBraces > 0;
        }
        parts.add(currentPart.toString());
        return parts;
    }

    static boolean equalsAny(char value, char[] values) {
        for (char v : values) {
            if (value == v) {
                return true;
            }
        }
        return false;
    }

    String prepareExpression(String expression) {
        expression = expression.replaceAll(" ", "");
        if (expression.startsWith("(") && expression.endsWith(")")) {
            expression = expression.substring(1, expression.length() - 1);
        }
        return expression;
    }

    boolean containsAny(String string, String ... values) {
        for (String value : values) {
            if (string.contains(value)) {
                return true;
            }
        }
        return false;
    }
}
