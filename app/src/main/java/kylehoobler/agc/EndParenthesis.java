package kylehoobler.agc;

import java.io.Serializable;

public class EndParenthesis extends EquationPart implements Serializable {


    protected EndParenthesis(){

        this.setDisplayItem(")");
        this.setPriority(9);
    }

}
