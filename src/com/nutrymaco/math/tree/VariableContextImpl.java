package com.nutrymaco.math.tree;

import java.util.HashMap;
import java.util.Map;

public class VariableContextImpl implements VariableContext {
    Map<String, Double> values = new HashMap<>();

    public double getValueOfVariable(String varName) {
        return values.get(varName);
    }

    public void setValue(String varName, double value){
        values.put(varName, value);
    }

    public void clear() {
        values.clear();
    }
}
