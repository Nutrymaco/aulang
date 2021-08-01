package com.nutrymaco.math.tree;

import java.util.HashMap;
import java.util.Map;

public class VariableContext {
    Map<String, Double> values = new HashMap<>();

    public double getValueOfVariable(String varName) {
        return values.get(varName);
    }

    public VariableContext setValue(String varName, double value){
        values.put(varName, value);
        return this;
    }

    public void clear() {
        values.clear();
    }
}
