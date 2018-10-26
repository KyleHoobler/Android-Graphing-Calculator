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


    protected Number numOpSolve(Number tmp) {

        try {
            switch (this.getOperation()) {

                case NEG:
                    tmp = new Number(tmp.getValue().multiply(new BigDecimal(-1)));
                    break;
                case SIN:
                    tmp = new Number(new BigDecimal(Math.sin(tmp.getValue().doubleValue())).setScale(decimalLength, BigDecimal.ROUND_DOWN).stripTrailingZeros());
                    break;
                case COS:
                    tmp = new Number(new BigDecimal(Math.cos(tmp.getValue().doubleValue())).setScale(decimalLength, BigDecimal.ROUND_DOWN).stripTrailingZeros());
                    break;
                case TAN:
                    tmp = new Number(new BigDecimal(Math.tan(tmp.getValue().doubleValue())).setScale(decimalLength, BigDecimal.ROUND_DOWN).stripTrailingZeros());
                    break;
                case SQRT:
                    tmp = new Number(new BigDecimal(Math.sqrt(tmp.getValue().doubleValue())).setScale(decimalLength, BigDecimal.ROUND_DOWN).stripTrailingZeros());
                    break;
                case LOG:
                    tmp = new Number(new BigDecimal(Math.log10(tmp.getValue().doubleValue())).setScale(decimalLength, BigDecimal.ROUND_DOWN).stripTrailingZeros());
                    break;
                case LN:
                    tmp = new Number(new BigDecimal(Math.log(tmp.getValue().doubleValue())).setScale(decimalLength, BigDecimal.ROUND_DOWN).stripTrailingZeros());
                    break;
                default:
                    return null;
            }


            return tmp;
        }
        catch(Exception e){

        }

       return null;

    }

}
