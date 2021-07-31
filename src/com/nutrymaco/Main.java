package com.nutrymaco;

import com.nutrymaco.lang.execution.*;

import java.util.List;

import static com.nutrymaco.lang.execution.NativeFunctionCallExpression.NativeFunctionType.PRINT;


public class Main {
    public static void main(String[] args) {
        var baseFrame = new Frame();

        var aInit = new ConstantInitExpression("a", new ConstantValue(5), baseFrame);
        var bInit = new ConstantInitExpression("b", new ConstantValue(12), baseFrame);

        var fFrame = new Frame(baseFrame);
        var funFInit = new FunctionInitExpression("f", List.of("arg1", "arg2"),
                List.of(
                        new NativeFunctionCallExpression(PRINT, List.of(new ReferenceValue("arg1", fFrame))),
                        new IfElseExpression(
                                () -> true,
                                List.of(new ConstantInitExpression("c", new ReferenceValue("arg1", fFrame), fFrame)),
                                List.of(new ConstantInitExpression("c", new ReferenceValue("arg2", fFrame), fFrame))
                        )
                ),
                new ReferenceValue("c", fFrame),
                fFrame
                );


        var fCall = new FunctionCallValue("f",
                List.of(new ReferenceValue("a", baseFrame), new ReferenceValue("b", baseFrame)),
                fFrame);

        aInit.perform();
        bInit.perform();
        funFInit.perform();
        System.out.println(fCall.get());
    }
}
