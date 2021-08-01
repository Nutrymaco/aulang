package com.nutrymaco.lang.execution;

import com.nutrymaco.lang.parsing.Parser;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Frame {

    private static int COUNT_OF_PARENT_FRAMES = 0;

    private static int FRAMES_COUNT = 0;

    private final Map<String, Value> localValues;

    private final Map<String, Function<Frame, FunctionValue>> functions;

    private final Stack<Value> stack;

    private final List<Frame> childFrames = new ArrayList<>();

    private final Frame parentFrame;

    private final int frameIndex = FRAMES_COUNT++;

    public Frame(Frame parentFrame) {
        this.localValues = new HashMap<>(parentFrame.localValues);
//        this.localValues = new HashMap<>();
        this.functions = new HashMap<>(parentFrame.functions);
        this.stack = new Stack<>();
        parentFrame.subscribe(this);
        this.parentFrame = parentFrame;
    }

    public Frame() {
        if (COUNT_OF_PARENT_FRAMES != 0) {
            throw new IllegalStateException("only one parent frame allowed");
        }
        COUNT_OF_PARENT_FRAMES++;
        this.localValues = new HashMap<>();
        this.stack = new Stack<>();
        this.functions = new HashMap<>();
        this.parentFrame = null;
    }

    public Value getLocalValue(String variableName) {
        var value = localValues.get(variableName);
        if (value == null) {
            throw new IllegalArgumentException("not found variable '%s' on frame %s".formatted(variableName, this));
        }
        return value;
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
        var function =  functions.get(name);
        if (function == null) {
            throw new IllegalArgumentException("cant find function '%s' in frame %s".formatted(name, this));
        }
        return function;
    }

    public void subscribe(Frame child) {
        childFrames.add(child);
    }

    public Stream<Map.Entry<String, Value>> localVariables() {
        return localValues.entrySet().stream();
    }

    @Override
    public String toString() {
        return "Frame" + frameIndex + "{" +
                "stackSize=" + stack.size() +
                ", localValues=" + localValues.keySet() +
                ", functions=" + functions.keySet() +
                ", parentFrameIndex=" + (parentFrame == null? -1 : parentFrame.frameIndex) +
                '}';
    }

    public Optional<Function<Frame, FunctionValue>> optGetFunction(String functionName) {
        return Optional.ofNullable(functions.get(functionName));
    }
}
