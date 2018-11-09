package kylehoobler.agc;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class Operation extends EquationPart implements java.io.Serializable{

    protected String operation;

    protected Operation(String op){

        operation = op;
        operationPriority();
        id = 5;

        //Default value


        switch (this.operation) {
            case SUB:
                this.setDisplayItem(dispSub); break;
            case ADD:
                this.setDisplayItem(dispPlus); break;
            case DIV:
                this.setDisplayItem(dispDiv); break;
            case MULT:
                this.setDisplayItem(dispTimes); break;
            case POW:
                this.setDisplayItem(dispPow);break;
        }

    }


    /**
     * This is in case I want to go back and add settings to this application so the user can specify how many decimal places that they want
     * @param op
     * @param decLen
     */
    protected Operation(String op, int decLen){

        operation = op;
        operationPriority();
        id = 5;

        //Default value
        decimalLength = decLen;

        switch (this.operation) {
            case SUB:
                this.setDisplayItem(dispSub); break;
            case ADD:
                this.setDisplayItem(dispPlus); break;
            case DIV:
                this.setDisplayItem(dispDiv); break;
            case MULT:
                this.setDisplayItem(dispTimes); break;
            case POW:
                this.setDisplayItem(dispPow);break;
        }

    }

    protected void operationPriority(){
        switch (this.operation) {
            case SUB:
                    this.addPriority(2); break;
            case ADD:
                    this.addPriority(2); break;
            case DIV:
                    this.addPriority(3); break;
            case MULT:
                    this.addPriority(3); break;
            case POW:
                this.addPriority(4); break;
        }

    }

    protected void setDecimalLength(int i){
        decimalLength = i;
    }

    protected Number solveOp(Number x, Number y){

        switch (this.operation) {
            case SUB:
                return new Number(x.getValue().subtract(y.getValue()));
            case ADD:
                return new Number(x.getValue().add(y.getValue()));
            case DIV:
                return new Number(x.getValue().divide(y.getValue(), decimalLength, RoundingMode.HALF_UP).stripTrailingZeros());
            case MULT:
                return new Number(x.getValue().multiply(y.getValue()).divide(new BigDecimal(1), decimalLength, RoundingMode.HALF_UP).stripTrailingZeros());
            case POW:
                //Loss of precision but oh well
                return new Number(new BigDecimal(Math.pow(x.getValue().doubleValue(), y.getValue().doubleValue())).divide(new BigDecimal(1), decimalLength, RoundingMode.HALF_UP).stripTrailingZeros());
        }
        return null;

    }








}
