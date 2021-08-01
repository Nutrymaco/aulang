package com.nutrymaco.lang.execution;

public class ConstantValue<T> implements Value {
    private final T object;

    public ConstantValue(T object) {
        this.object = object;
    }

    @Override
    public T get() {
        return object;
    }

    @Override
    public String toString() {
        return "const{" +
                 object +
                '}';
    }
}
