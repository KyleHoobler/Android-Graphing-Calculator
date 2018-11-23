package kylehoobler.agc;


/**
 * This class will hold the Start parenthesis object. This class contains an equation.
 */
public class StartParenthesis extends EquationPart implements java.io.Serializable{

    /*
     * This is the Equation that is contained in this object
     */
    protected Equation eq;


    protected StartParenthesis(){
        eq = new Equation();
        this.setPriority(7);
    }

    /**
     * Change Equation in object
     * @param x equation to replace with
     */
    protected void setEq(Equation x){
        this.eq = x;
    }

    /**
     * Getter for EQ
     * @return eq
     */
    protected Equation getEq(){
        return eq;
    }


    /**
     * Modified getDisplayItem to properly display the item
     * @return
     */
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

    /**
     * If this object has an Endparenthesis option
     * @return if eq has end paren
     */
    protected boolean hasEndParen(){
            if(eq.isEmpty())
                return false;

            return eq.get(eq.size()-1).getClass() == EndParenthesis.class;


    }




    /**
     * This class will solve the equation that is presented in the startParenthesis object
     * @return The end result number
     */
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
