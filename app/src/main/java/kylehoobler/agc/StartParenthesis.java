package kylehoobler.agc;


public class StartParenthesis extends EquationPart {

    private Equation eq;


    protected StartParenthesis(){
        eq = new Equation();
        this.setPriority(7);
    }

    protected Equation getEq(){
        return eq;
    }


    @Override
    protected String getDisplayItem(){

        String tmp = "(";
        for(int i = 0; i < eq.size(); i++){
            if(eq.get(i).getClass() != Number.class) {
                tmp += eq.get(i).getDisplayItem();
            }
            else{
                tmp += ((Number) eq.get(i)).getValue();
            }
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
