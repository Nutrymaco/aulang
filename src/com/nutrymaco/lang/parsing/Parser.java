package com.nutrymaco.lang.parsing;

import com.nutrymaco.lang.execution.*;
import com.nutrymaco.math.calculator.Calculator;
import com.nutrymaco.math.calculator.Logicator;
import com.nutrymaco.math.tree.VariableContext;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.nutrymaco.lang.execution.NativeFunctionCallExpression.NativeFunctionType.PRINT;

public class Parser {

    private static final Pattern CONST_INIT_REGEX = Pattern.compile("const[\\s\\t]*([a-zA-Z]+[0-9]*)[\\s\\t]*=(.*);");
    private static final Pattern FUN_CALL_REGEX = Pattern.compile("([a-zA-Z]+[0-9]*)\\((.*)\\)");
    private static final Pattern FUN_HEADER_REGEX = Pattern.compile("fun([a-zA-Z]+[0-9]*)\\((.*)\\)");
    private static final Pattern RETURN_VALUE_REGEX = Pattern.compile("return([a-zA-Z]+[0-9]*);}");
    private static final Pattern IF_ELSE_REGEX = Pattern.compile(".*if\\((.*)\\)\\{(.*)}else\\{(.*)}.*");

    public static final Map<String, Function<Frame, Expression>> functionInits = new HashMap<>();
    private static Map<String, List<String>> functionParameters = new HashMap<>();

    private final String code;

    private final List<Function<Frame, Expression>> expressions = new ArrayList<>();

    private boolean wasParsed = false;

    private final Frame frame;

    private int index = 0;

    public Parser(String code, Frame frame) {
        this.code = code;
        this.frame = frame;
    }

    public static String getFunParameterName(String funName, int parameterIndex) {
        return functionParameters.get(funName).get(parameterIndex);
    }

    public void execute() {
        if (!wasParsed) {
            parseWithError();
            wasParsed = true;
        }
        expressions.stream().map(sup -> sup.apply(frame)).forEach(Expression::perform);
    }

    List<Function<Frame, Expression>> getExpressions() {
        if (!wasParsed) {
            parseWithError();
            wasParsed = true;
        }
        return expressions;
    }

    private void parseWithError() {
        try {
            parse();
        } catch (Exception e) {
            System.out.printf("code = %s%ncurrent = %s%n", code, code.substring(index));
            throw e;
        }
    }

    private void parse() {
        while (code.length() != index) {
            if (code.startsWith("const", index)) {
                String constInitRaw = code.substring(
                        code.indexOf("const", index),
                        code.indexOf(";", index) + 1
                );
                index += constInitRaw.length();
                var matcher = CONST_INIT_REGEX.matcher(constInitRaw);
                matcher.matches();
                String constName = matcher.group(1);
                String constValueRaw = matcher.group(2);

                Function<Frame, Value> constValue;
                if (constValueRaw.matches("[a-zA-Z].*") &&
                        !(constValueRaw.contains("+") || constValueRaw.contains("-") || constValueRaw.contains("*"))) {
                    // build fun call
                    var funCallMatch = FUN_CALL_REGEX.matcher(constValueRaw);
                    if (funCallMatch.matches()){
                        var funName = funCallMatch.group(1);
                        var args = funCallMatch.group(2).split("\s*,\s*");

                        constValue = frame -> {
                            var childFrame = new Frame(frame);
                            return new FunctionCallValue(
                                    funName,
                                    Arrays.stream(args)
                                            .map(arg -> {
                                                if (functionInits.containsKey(arg)) {
                                                    return new FunctionReferenceValue(arg, functionInits.get(arg));
                                                }
                                                return new ReferenceValue(arg, frame);
                                            })
                                            .collect(Collectors.toList()),
                                    childFrame
                            );
                        };
                    } else {
                        // dereference
                        var otherValueName = constValueRaw;
                        constValue = frame -> new ReferenceValue(otherValueName, frame);
                    }
                } else {
                    // build math exp
                    Function<Frame, Calculator> calculatorByFrame = frame -> new Calculator(new VariableContext() {
                        @Override
                        public double getValueOfVariable(String varName) {
                            return (Double) frame.getLocalValue(varName).get();
                        }

                        @Override
                        public void setValue(String varName, double value) {

                        }
                    });

                    constValue = frame -> {
                        var calculator = calculatorByFrame.apply(frame);
                        calculator.buildTree(constValueRaw);
                        return new CalculableValue(calculator, frame);
                    };
                }

                expressions.add(frame -> new ConstantInitExpression(constName, constValue.apply(frame), frame));
            } else if (code.startsWith("fun", index)) {
                var funHeader = FUN_HEADER_REGEX.matcher(code.substring(index, code.indexOf(')', index) + 1));
                funHeader.matches();
                var funName = funHeader.group(1);
                var funParameters = Arrays.stream(funHeader.group(2).split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());

                // find last } of function
                int count = 1;
                index = code.indexOf("{", index);
                int lastBraceIndex = index + 1;
                while (count > 0) {
                    if (code.charAt(lastBraceIndex) == '}') {
                        count--;
                    } else if (code.charAt(lastBraceIndex) == '{') {
                        count++;
                    }
                    lastBraceIndex++;
                }
                var funRawLines = code.substring(index + 1, lastBraceIndex);
                index = lastBraceIndex;

                var returnVariableMatcher = RETURN_VALUE_REGEX.matcher(
                        funRawLines.substring(funRawLines.indexOf("return")));
                returnVariableMatcher.matches();
                var returnVariable = returnVariableMatcher.group(1);
                Function<Frame, Value> returnValue = frame -> new ReferenceValue(returnVariable, frame);

                Function<Frame, Parser> functionParser = frame -> new Parser(funRawLines.substring(0, funRawLines.indexOf("return")), new Frame(frame));
                Function<Frame, Expression> functionInitExpression = frame -> new FunctionInitExpression(funName,
                        funParameters,
                        functionParser.apply(frame).getExpressions(),
                        returnValue,
                        frame);
                expressions.add(functionInitExpression);
                functionInits.put(funName, functionInitExpression);
                functionParameters.put(funName, funParameters);
            } else if (code.startsWith("print", index)) {
                // function call
                var args = code.substring(index + 6, code.indexOf(')', index)).split(",");
                Function<Frame, List<Value>> references = frame ->
                        Arrays.stream(args).map(arg -> new ReferenceValue(arg, frame)).collect(Collectors.toList());
                Function<Frame, Expression> nativeFunctionCallExpression = frame ->
                        new NativeFunctionCallExpression(PRINT, references.apply(frame));
                expressions.add(nativeFunctionCallExpression);
                index = code.indexOf(';', index) + 1;
            } else if (code.startsWith("if", index)) {
                // todo search last ) brace because brace can be inside if
                index += "if".length();
                var closeRoundBraceIndex = code.indexOf(')', index);
                var conditionRaw = code.substring(index + 1, closeRoundBraceIndex);
                index = closeRoundBraceIndex + 1;

                int ifEnd = getLastCompleteBraceIndex(code.substring(index));
                var ifExpressionsRaw = code.substring(index + 1, index + ifEnd - 1);
                index = index + ifEnd;
                index += "else".length();

                int elseEnd = getLastCompleteBraceIndex(code.substring(index));
                var elseExpressionsRaw = code.substring(index + 1, index + elseEnd - 1);
                index = index + elseEnd;

                Function<Frame, Expression> ifElseExpression = frame -> {
                    var logicator = new Logicator(new VariableContext() {

                        @Override
                        public double getValueOfVariable(String varName) {
                            return (Double) frame.getLocalValue(varName).get();
                        }

                        @Override
                        public void setValue(String varName, double value) { }
                    });
                    logicator.buildTree(conditionRaw);
                    var condition = new LogicalValue(logicator, frame);
                    var ifParser = new Parser(ifExpressionsRaw, frame);
                    var elseParser = new Parser(elseExpressionsRaw, frame);
                    return new IfElseExpression(condition,
                            ifParser.getExpressions().stream().map(e -> e.apply(frame)).collect(Collectors.toList()),
                            elseParser.getExpressions().stream().map(e -> e.apply(frame)).collect(Collectors.toList()));
                };
                expressions.add(ifElseExpression);
            } else  {
                throw new IllegalArgumentException("illegal start = " + code.substring(index, index + 5));
            }
        }
    }

    // todo use this fun allwhere
    //    "{ergerg{}  --> } <--  {}"
    private static int getLastCompleteBraceIndex(String str) {
        int count = 1;
        int lastBraceIndex = str.indexOf('{') + 1;
        while (count > 0) {
            if (str.charAt(lastBraceIndex) == '}') {
                count--;
            } else if (str.charAt(lastBraceIndex) == '{') {
                count++;
            }
            lastBraceIndex++;
        }
        return lastBraceIndex;
    }

}
