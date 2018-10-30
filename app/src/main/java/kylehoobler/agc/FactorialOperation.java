package kylehoobler.agc;

import java.math.BigDecimal;

/**
 * Special class needed because factorial operates in a different way than most number operations. Number operations appear before the number that they are evaluating where factorial appears after but
 * operates in the same way.
 */
class FactorialOperation extends EquationPart {

    protected Number num1;

    protected FactorialOperation() {
        num1 = null;
        this.setDisplayItem(EquationPart.dispFac);
        this.setPriority(6);
    }


    protected BigDecimal factorial(BigDecimal value) throws NumberFormatException{


            if (value.intValue() > 0)
                return value.multiply(factorial(value.subtract(new BigDecimal(1))));
            else
                return new BigDecimal(1);


    }

    protected Number getFactorial(Number x){

        return new Number(factorial(x.getValue()));
    }


}