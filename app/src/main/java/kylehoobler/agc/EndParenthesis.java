package kylehoobler.agc;

public class EndParenthesis extends EquationPart implements java.io.Serializable {


    protected EndParenthesis(){

        this.setDisplayItem(")");
        this.setPriority(1);
    }

}
