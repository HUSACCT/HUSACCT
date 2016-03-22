/**
 * *************************************************************************
 * Copyright 2012 by + Christian-Albrechts-University of Kiel + Department of
 * Computer Science + Software Engineering Group and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * *************************************************************************
 */
package husacct.analyse.infrastructure.antlr.csharp;

public class Expression {

    private boolean expression;

    public boolean isExpression() {
        return expression;
    }

    public void setExpression(final boolean expression) {
        this.expression = expression;
    }

    // custom methods used in lexer actions
    public void set(final boolean expr) {
        this.expression = expr;
    }

    public void set(final Expression exprParam) {
        this.expression = exprParam.expression;
    }

    public void or(final Expression expr1, final Expression expr2) {
        this.expression = expr1.expression || expr2.expression;
    }

    public void and(final Expression expr1, final Expression expr2) {
        this.expression = expr1.expression && expr2.expression;
    }

    public void equal(final Expression expr1, final Expression expr2) {
        this.expression = expr1.expression == expr2.expression;
    }

    public void unequal(final Expression expr1, final Expression expr2) {
        this.expression = expr1.expression != expr2.expression;
    }

    public void not(final Expression expr) {
        this.expression = !expr.expression;
    }
}
