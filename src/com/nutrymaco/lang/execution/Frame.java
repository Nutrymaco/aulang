package com.nutrymaco.lang.execution;

import java.util.*;

public class Frame {

    private final Map<String, Value> localValues;

    private final Stack<Value> stack;

    private final List<Frame> childFrames = new ArrayList<>();

    public Frame(Frame parentFrame) {
        this.localValues = new HashMap<>(parentFrame.localValues);
        this.stack = new Stack<>();
        parentFrame.subscribe(this);
    }

    public Frame() {
        this.localValues = new HashMap<>();
        this.stack = new Stack<>();
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

    public void subscribe(Frame child) {
        childFrames.add(child);
    }
}
