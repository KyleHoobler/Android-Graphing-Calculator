package kylehoobler.agc;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SpecialNumber extends Number {

    protected String opcode;
    protected int decimalLength;

    protected SpecialNumber(String opcode){

        this.opcode = opcode;

        //Default placeholder
        decimalLength = 10;


        switch (opcode){
            case PI: this.setValue(new BigDecimal(Math.PI).divide(new BigDecimal(1), decimalLength, RoundingMode.HALF_UP)); this.setDisplayItem(dispPi); break;
            case E: this.setValue(new BigDecimal(Math.E).divide(new BigDecimal(1), decimalLength, RoundingMode.HALF_UP)); this.setDisplayItem(E); break;
            default: this.setValue(new BigDecimal(0));
        }

    }
}
