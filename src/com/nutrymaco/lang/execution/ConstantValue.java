package com.nutrymaco.lang.execution;

public class ConstantValue implements Value {
    private final Object object;

    public ConstantValue(Object object) {
        this.object = object;
    }

    @Override
    public Object get() {
        return object;
    }

    @Override
    public String toString() {
        return "const{" +
                 object +
                '}';
    }
}
