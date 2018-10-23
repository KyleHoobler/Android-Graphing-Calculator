package kylehoobler.agc;


import java.util.ArrayList;

public class StartParenthesis extends EquationPart {

    private Equation eq;

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
            if(eq.get(i).getClass() != Number.class)
            tmp += eq.get(i).getDisplayItem();
            else
                tmp += ((Number)eq.get(i)).getValue().stripTrailingZeros();
        }

        return tmp;
    }


    protected boolean hasEndParen(){

        for(int i = 0; i < eq.size(); i++){
            if(eq.get(i).getClass() == EndParenthesis.class)
                return true;
        }
        return false;
    }

    protected EquationPart ParenSolve(){
        eq.solve();

        if(!eq.isEmpty())
            return eq.get(0);

        return null;

    }



}
