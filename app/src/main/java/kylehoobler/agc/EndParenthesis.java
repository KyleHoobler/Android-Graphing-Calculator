package kylehoobler.agc;

public class EndParenthesis extends EquationPart {


    protected EndParenthesis(){

        this.setDisplayItem(")");
        this.setPriority(1);
    }

}
