package com.nutrymaco.lang.execution;

public class ReferenceValue implements Value {

    private final String variableName;

    private final Frame frame;

    public ReferenceValue(String variableName, Frame frame) {
        this.variableName = variableName;
        this.frame = frame;
    }

    @Override
    public Object get() {
        return frame.getLocalValue(variableName).get();
    }

    @Override
    public String toString() {
        return "Ref{" +
                "name='" + variableName + '\'' +
                ", value='" + get() +
                ", frame=" + frame.hashCode() +
                '}';
    }
}
