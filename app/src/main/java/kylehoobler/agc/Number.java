package kylehoobler.agc;


import java.math.BigDecimal;

public class Number extends EquationPart {

    private BigDecimal value;

    protected Number(){
        value = new BigDecimal(0);
        this.setPriority(0);
    }

    protected Number(BigDecimal value){
        this.setDisplayItem(value +"");
        this.value = value;
        this.setPriority(0);

    }

    protected Number(Double value){
        this.setDisplayItem(value +"");
        this.value = new BigDecimal(value);
        this.setPriority(0);
    }


    protected BigDecimal getValue(){
        return value;
    }

    protected void setValue(BigDecimal value){
        this.value = value;
    }

}
