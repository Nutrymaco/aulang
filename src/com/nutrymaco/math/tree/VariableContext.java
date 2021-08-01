package com.nutrymaco.math.tree;

public interface VariableContext {

    double getValueOfVariable(String varName);

    void setValue(String varName, double value);

}
