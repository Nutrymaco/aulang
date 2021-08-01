package com.nutrymaco.lang.execution;

import com.nutrymaco.math.calculator.Logicator;
import com.nutrymaco.math.tree.VariableContextImpl;

public class LogicatorTest {
    public static void main(String[] args) {
        var logicator = new Logicator(new VariableContextImpl());
        var node = logicator.buildTree("first<second");
        System.out.println(node);
    }
}
