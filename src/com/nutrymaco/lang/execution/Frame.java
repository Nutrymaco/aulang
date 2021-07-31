package com.nutrymaco.lang.execution;

import java.util.*;
import java.util.function.Function;

public class Frame {

    private final Map<String, Value> localValues;

    private final Map<String, Function<Frame, FunctionValue>> functions;

    private final Stack<Value> stack;

    private final List<Frame> childFrames = new ArrayList<>();

    public Frame(Frame parentFrame) {
        this.localValues = new HashMap<>(parentFrame.localValues);
        this.functions = new HashMap<>(parentFrame.functions);
        this.stack = new Stack<>();
        parentFrame.subscribe(this);
    }

    public Frame() {
        this.localValues = new HashMap<>();
        this.stack = new Stack<>();
        this.functions = new HashMap<>();
    }

    public Value getLocalValue(String variableName) {
        return localValues.get(variableName);
    }

    public Value getNextStackValue() {
        return stack.pop();
    }

    public void putOnStack(Value value) {
        stack.push(value);
    }

    public void putLocalValue(String name, Value value) {
        localValues.put(name, value);
        childFrames.forEach(
                child -> child.putLocalValue(name, value)
        );
    }

    public void putFunction(String name, Function<Frame, FunctionValue> functionWithoutFrame) {
        functions.put(name, functionWithoutFrame);
        childFrames.forEach(
                child -> child.putFunction(name, functionWithoutFrame)
        );
    }

    public Function<Frame, FunctionValue> getFunction(String name) {
        return functions.get(name);
    }

    public void subscribe(Frame child) {
        childFrames.add(child);
    }
}
