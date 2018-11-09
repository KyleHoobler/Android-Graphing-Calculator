package kylehoobler.agc;

import java.math.BigDecimal;

public class Number extends EquationPart implements java.io.Serializable{

    private BigDecimal value;


    protected Number(){
        this.setDisplayItem("0");
        value = new BigDecimal(0);
        this.setPriority(0);
        id = 0;
    }

    protected Number(BigDecimal value){

        this.setDisplayItem(value.stripTrailingZeros() +"");
        this.value = value;
        this.setPriority(0);
        id = 0;

    }

    protected Number(Double value){
        this.setDisplayItem(value +"");
        this.value = new BigDecimal(value);
        this.setPriority(0);
        id  = 0;
    }


    protected BigDecimal getValue(){
        return value;
    }

    protected void setValue(BigDecimal value){
        this.value = value;
    }

}
