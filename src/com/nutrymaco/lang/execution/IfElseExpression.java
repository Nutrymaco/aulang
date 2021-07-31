package com.nutrymaco.lang.execution;

import java.util.List;

public class IfElseExpression implements Expression {

    private final Value valueToCheck;

    private final List<Expression> ifExpressions;

    private final List<Expression> elseExpressions;

    public IfElseExpression(Value valueToCheck, List<Expression> ifExpressions, List<Expression> elseExpressions) {
        this.valueToCheck = valueToCheck;
        this.ifExpressions = ifExpressions;
        this.elseExpressions = elseExpressions;
    }

    public IfElseExpression(Value valueToCheck, List<Expression> ifExpressions) {
        this.valueToCheck = valueToCheck;
        this.ifExpressions = ifExpressions;
        this.elseExpressions = List.of();
    }

    @Override
    public void perform() {
        if (isTrue(valueToCheck.get())) {
            ifExpressions.forEach(Expression::perform);
        } else {
            elseExpressions.forEach(Expression::perform);
        }
    }

    // добавить поддержку других типов как в питоне
    private boolean isTrue(Object object) {
        return Boolean.TRUE.equals(object);
    }
}
