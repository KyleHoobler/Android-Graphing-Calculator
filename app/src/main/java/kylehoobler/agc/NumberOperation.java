package kylehoobler.agc;

import java.math.BigDecimal;

/*
    Operation Class for items that only require one number rather than 2

 */
public class NumberOperation extends EquationPart {

    private String operation;


    protected NumberOperation(String op){

        operation = op;

        //Six is priority of Parenthesis
        this.addPriority(6);

        switch (this.getOperation()) {

            case NEG:
                this.setDisplayItem(dispSub);
                break;
            case SIN:
                this.setDisplayItem(dispSin);
                break;
            case COS:
                this.setDisplayItem(dispCos);
                break;
            case TAN:
                this.setDisplayItem(dispTan);
                break;
            case SQRT:
                this.setDisplayItem(dispSQRT);
                break;
            case FAC:
                this.setDisplayItem(dispFac);
                break;
            case LOG:
                this.setDisplayItem(dispLog);
                break;
            case LN:
                this.setDisplayItem(dispLn);
                break;

        }

    }

    protected String getOperation(){
        return operation;
    }


    protected BigDecimal factorial(BigDecimal value){

        if(value.intValue() > 0)
            return value.multiply( factorial(value.subtract(new BigDecimal(1))));
        else
            return new BigDecimal(1);
    }

    protected Number numOpSolve() {

        try {
            Number tmp = new Number();
            switch (this.getOperation()) {

                case NEG:
                    tmp = new Number(tmp.getValue().multiply(new BigDecimal(-1)));
                    break;
                case SIN:
                    tmp = new Number(Math.sin(tmp.getValue().doubleValue()));
                    break;
                case COS:
                    tmp = new Number(Math.cos(tmp.getValue().doubleValue()));
                    break;
                case TAN:
                    tmp = new Number(Math.tan(tmp.getValue().doubleValue()));
                    break;
                case SQRT:
                    tmp = new Number(Math.sqrt(tmp.getValue().doubleValue()));
                    break;
                case FAC:
                    tmp = new Number(factorial(tmp.getValue()));
                    break;
                case LOG:
                    tmp = new Number(Math.log10(tmp.getValue().doubleValue()));
                    break;
                case LN:
                    tmp = new Number(Math.log(tmp.getValue().doubleValue()));
                    break;
                default:
                    return null;
            }

            this.subtractPriority(6);

            return tmp;
        }
        catch(Exception e){

        }

       return null;

    }

}
