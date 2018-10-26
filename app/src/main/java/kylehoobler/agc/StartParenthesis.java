package kylehoobler.agc;


import android.util.Log;
import android.widget.TextView;

import java.math.RoundingMode;
import java.util.ArrayList;

public class StartParenthesis extends EquationPart {

    private Equation eq;
    private TextView text;


    protected StartParenthesis(){


        eq = new Equation();
        this.setPriority(7);
    }

    protected Equation getEq(){
        return eq;
    }


    protected void setEq(Equation e){
        eq = e;
    }

    protected void addToParen(EquationPart e){
        ArrayList<EquationPart> tmp = eq.getEquation();
        tmp.add(e);
        eq.setEquation(tmp);
    }



    @Override
    protected String getDisplayItem(){

        String tmp = "(";
        for(int i = 0; i < eq.size(); i++){
            if(eq.get(i).getClass() != Number.class) {
                tmp += eq.get(i).getDisplayItem();
            }

            if(eq.get(i).getClass() == Number.class) {

                tmp += ((Number) eq.get(i)).getValue();
                if(eq.getDecimalCount() > 0){
                    if(((Number)eq.get(i)).getValue().doubleValue() == ((Number)eq.get(i)).getValue().intValue()){
                        //tmp += ".0";
                    }
                }
            }
        }
        return tmp;
    }

    protected StartParenthesis addToInnerMost(EquationPart e) throws Exception{

        if(!this.getEq().isEmpty()) {
            if (this.getEq().get(this.getEq().size() - 1).getClass() == StartParenthesis.class) {

                ((StartParenthesis) this.getEq().get(this.getEq().size() - 1)).addToInnerMost(e);
            }

            if(this.getEq().getEquation().get(this.getEq().size()-1).getClass() != EndParenthesis.class) {
                    this.getEq().getEquation().add(e);
                throw new Exception();
            }

        }

        return this;
    }


    protected boolean hasEndParen(){

        for(int i = 0; i < eq.size(); i++){
            if(eq.get(i).getClass() == EndParenthesis.class)
                return true;
        }
        return false;
    }

    protected Number ParenSolve(){
        if(!this.getEq().isEmpty()){
            if(!(this.getEq().size() == 1 && this.getEq().get(0).getClass() == EndParenthesis.class))
                eq.solve();
            else{
                return new Number(0.0);
            }
        }


        if(!eq.isEmpty())
            return (Number)eq.get(0);

        return null;

    }



}
