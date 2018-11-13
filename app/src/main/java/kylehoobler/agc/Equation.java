package kylehoobler.agc;

import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * This class holds the full equation, and methods used to solve for the equation result
 */
class Equation extends EquationPart implements Serializable {

    @SerializedName("equation")
    private ArrayList<EquationPart> equation;

    @SerializedName("decCount")
    protected int decimalCount;

    @SerializedName("isDecimal")
    private boolean isDecimal;


    Equation(){

        isDecimal = false;
        this.decimalCount = 0;
        equation = new ArrayList<>();
    }


    protected ArrayList<EquationPart> getEquation(){
        return equation;
    }

    protected void setEquation(ArrayList<EquationPart> e){
        equation = e;
    }

    protected void setDecimalCount(int i){
         decimalCount = i;
    }

    protected void setIsDecimal(boolean i){
        isDecimal = i;
    }

    protected boolean isEmpty(){
        return equation.isEmpty();
    }

    @Override
    protected Equation clone() {
         Equation clone = new Equation();

         for(int i = 0; i < this.getEquation().size(); i++){

             if(this.getEquation().get(i).getClass() != StartParenthesis.class)
                clone.equation.add(this.getEquation().get(i));
             else {
                 StartParenthesis clon = new StartParenthesis();
                 clon.setEq(((StartParenthesis)this.getEquation().get(i)).getEq().clone());
                 clone.equation.add(clon);
             }
         }

        return clone;
    }

    protected EquationPart get(int i){
        return equation.get(i);
    }

    protected int size(){
        return equation.size();
    }

    protected void clearEQ(){
        isDecimal = false;
        this.decimalCount = 0;
        equation = new ArrayList<>();
        equation.clear();

    }

    /**
     * Gets the max priority and the position of that value
     * @return pair of highest priority and its value
     */
    @NonNull
    private Pair getMaxPriority(){

        int maxPriority = 0;
        int position = 0;

        for(int i = 0; i < equation.size(); i++){
            if(equation.get(i).getPriority() > maxPriority) {
                maxPriority = equation.get(i).getPriority();
                position = i;
            }
        }

        return new Pair(maxPriority, position);
    }


    /**
     * Logic behind the calculator, this will solve the equation.
     */
    protected void solve(){

    try {
        if (!equation.isEmpty()) {
            if (equation.size() == 1 && equation.get(0).getClass() == StartParenthesis.class) {

                equation.set(0, ((StartParenthesis) equation.get(0)).ParenSolve());
            }

            while (equation.size() > 1) {


                Pair<Integer, Integer> tmp = getMaxPriority();

                if (equation.get(tmp.second).getClass() == Operation.class) {

                    if (tmp.second != equation.size()) {

                        equation.set(tmp.second, ((Operation) equation.get(tmp.second)).solveOp((Number) equation.get(tmp.second - 1), (Number) equation.get(tmp.second + 1)));

                        if (((Number) equation.get(tmp.second)).getValue().doubleValue() != ((Number) equation.get(tmp.second)).getValue().intValue()) {
                            isDecimal = true;
                            String[] splitter = ((Number) equation.get(tmp.second)).getValue().stripTrailingZeros().toString().split("\\.");
                            if (splitter.length == 2)
                                decimalCount = splitter[1].length();

                        } else {
                            isDecimal = false;
                            decimalCount = 0;
                        }
                        equation.remove(tmp.second + 1);
                        equation.remove(tmp.second - 1);

                    }

                }
                else if(equation.get(tmp.second).getClass() == NumberOperation.class){

                    equation.set(tmp.second, ((NumberOperation) equation.get(tmp.second)).numOpSolve((Number) equation.get(tmp.second + 1)));
                    equation.remove(tmp.second+1);

                }
                else if (equation.get(tmp.second).getClass() == EndParenthesis.class) {
                    equation.remove(equation.get(tmp.second));

                } else if (equation.get(tmp.second).getClass() == StartParenthesis.class) {
                    equation.set(tmp.second, ((StartParenthesis) equation.get(tmp.second)).ParenSolve());
                }
                else if(equation.get(tmp.second).getClass() == FactorialOperation.class){
                    try {

                        Number item = ((FactorialOperation) equation.get(tmp.second)).getFactorial((Number) equation.get(tmp.second - 1));
                        if(item.getValue() == null)
                            throw new Exception();

                        equation.set(tmp.second - 1, item);
                        equation.remove(tmp.second.intValue());
                    }
                    catch(Exception e){

                        this.clearEQ();
                        equation.add(new ErrorItem("Error Number too large."));
                        break;
                    }
                }
                else if(equation.get(tmp.second.intValue()).getClass() == Decimal.class){
                    equation.remove(tmp.second.intValue());
                }
            }
        }
    }
    catch(Exception e){
        clearEQ();
        equation.add(new ErrorItem());
    }

    }

    /**
     * Handles the adding of items to the equation. Takes recursion into account and will add to a start parenthesis if it does not have an end parenthesis
     * @param e item to be added
     */
    protected void addItem(EquationPart e){

       //Empty Check
       if(!equation.isEmpty()) {

           ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

              if (e.getClass() == Number.class) {
                    Number val = (Number)e;

                    //Number not declared as a decimal
                   if(equation.get(equation.size()-1).getClass() == Number.class && !isDecimal){
                       //Complicated way to get the number and increment it's value
                       equation.set(equation.size()-1, new Number(((Number)equation.get(equation.size()-1)).getValue().multiply(new BigDecimal(10)).add(val.getValue()) ));

                   }
                   //Decimal but needs to be declared as a decimal
                   else if(equation.get(equation.size()-1).getClass() == Decimal.class && !isDecimal){
                       isDecimal = true;
                       decimalCount++;

                       //Remove the decimal object
                       equation.remove(equation.get(equation.size()-1));


                       if(val.getValue().intValue() != 0) {
                           BigDecimal dec = decimalGenerator(decimalCount, val.getValue());
                           equation.set(equation.size() - 1, new Number(((Number) equation.get(equation.size() - 1)).getValue().add(dec)));
                       }
                       else{
                           equation.get(equation.size()-1).setDisplayItem(equation.get(equation.size()-1).getDisplayItem());
                       }
                   }
                   //Number that is a decimal already
                   else if(equation.get(equation.size()-1).getClass() == Number.class && isDecimal){
                       decimalCount++;

                       if(val.getValue().intValue() != 0) {
                           BigDecimal dec = decimalGenerator(decimalCount, val.getValue());
                           equation.set(equation.size() - 1, new Number(((Number) equation.get(equation.size() - 1)).getValue().add(dec)));
                       }else{
                           equation.get(equation.size()-1).setDisplayItem(equation.get(equation.size()-1).getDisplayItem()+"0");
                       }
                   }

                   else if(equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == Variable.class){

                       equation.add(new Operation(MULT));
                       equation.add(e);

                   }

                   else if(equation.get(equation.size()-1).getClass() == Operation.class){
                       equation.add(val);
                   }

                  else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                       if(((StartParenthesis)equation.get(equation.size()-1)).hasEndParen()){

                           equation.add(new Operation(MULT));
                           equation.add(e);

                       }
                       else {
                           ((StartParenthesis) equation.get(equation.size() - 1)).getEq().addItem(e);
                           ((StartParenthesis) equation.get(equation.size() - 1)).setDisplayItem(((StartParenthesis) equation.get(equation.size() - 1)).getDisplayItem());
                       }
                  }
               }

               /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

               else if (e.getClass() == Decimal.class) {
                   Decimal val = (Decimal)e;

                   if(equation.get(equation.size()-1).getClass() == Number.class && !isDecimal){
                       equation.add(val);
                   }
                   else if(equation.get(equation.size()-1).getClass() == Operation.class){
                       equation.add(new Number(0.0));
                       equation.add(val);
                   }
                   else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){
                       if(((StartParenthesis)equation.get(equation.size()-1)).hasEndParen()){
                           equation.add(new Operation(MULT));
                           equation.add(new Number(0.0));
                           equation.add(e);
                       }
                       else {
                           ((StartParenthesis) equation.get(equation.size() - 1)).getEq().addItem(e);
                       }
                   }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

               } else if (e.getClass() == SpecialNumber.class) {
                   isDecimal = false;
                   decimalCount = 0;

                  if(!equation.isEmpty()) {
                      if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                          if(((StartParenthesis)equation.get(equation.size()-1)).hasEndParen()){
                              equation.add(new Operation(MULT));
                              equation.add(e);
                              equation.add(new StartParenthesis());
                          }
                          else {
                              ((StartParenthesis) equation.get(equation.size() - 1)).getEq().addItem(e);
                          }

                      }

                      else if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == StartParenthesis.class || equation.get(equation.size()-1).getClass() == Variable.class){
                          equation.add(new Operation(MULT));
                          equation.add(e);
                      }
                      else if(equation.get(equation.size()-1).getClass() == Operation.class){
                          equation.add(e);
                      }

                  }

               }
              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

               else if (e.getClass() == Operation.class) {
                   isDecimal = false;
                   decimalCount = 0;

                  if(!equation.isEmpty()) {
                      if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                          if(((StartParenthesis)equation.get(equation.size()-1)).hasEndParen()){

                              equation.add(e);
                          }
                          else {
                              ((StartParenthesis) equation.get(equation.size() - 1)).getEq().addItem(e);
                          }

                      }
                      else if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == Variable.class){
                          equation.add(e);
                      }

                   }

                   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

               } else if (e.getClass() == NumberOperation.class) {
                   isDecimal = false;
                   decimalCount = 0;

                  if(!equation.isEmpty()) {
                      if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                          if(((StartParenthesis)equation.get(equation.size()-1)).hasEndParen()){
                              equation.add(new Operation(MULT));
                              equation.add(e);
                              equation.add(new StartParenthesis());
                          }
                          else {
                              ((StartParenthesis) equation.get(equation.size() - 1)).getEq().addItem(e);
                          }
                      }
                      else if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == Variable.class){
                          equation.add(new Operation(MULT));
                          equation.add(e);
                          equation.add(new StartParenthesis());
                      }
                      else if(equation.get(equation.size()-1).getClass() == Operation.class ){
                          equation.add(e);
                          equation.add(new StartParenthesis());
                      }
                  }
               }

              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

               else if (e.getClass() == StartParenthesis.class) {
                   isDecimal = false;
                   decimalCount = 0;

                   if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class ||equation.get(equation.size()-1).getClass() == Variable.class){
                       equation.add(new Operation(MULT));
                       equation.add(e);
                   }
                   else if(equation.get(equation.size()-1).getClass() == Operation.class){
                       equation.add(e);
                   }
                  else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                      if(((StartParenthesis)equation.get(equation.size()-1)).hasEndParen()){
                          equation.add(new Operation(MULT));
                          equation.add(e);
                      }
                      else {
                          ((StartParenthesis) equation.get(equation.size() - 1)).getEq().addItem(e);
                          ((StartParenthesis) equation.get(equation.size() - 1)).setDisplayItem(((StartParenthesis) equation.get(equation.size() - 1)).getDisplayItem());
                      }
                  }

               }

              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

               else if (e.getClass() == EndParenthesis.class) {
                   isDecimal = false;
                   decimalCount = 0;

                  //Parenthesis Check
                  if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){


                          ((StartParenthesis) equation.get(equation.size() - 1)).getEq().addItem(e);
                          ((StartParenthesis) equation.get(equation.size() - 1)).setDisplayItem(((StartParenthesis) equation.get(equation.size() - 1)).getDisplayItem());
                  }
                  else{

                          equation.add(e);

                  }
               }

              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

               else if (e.getClass() == FactorialOperation.class) {
                  isDecimal = false;
                  decimalCount = 0;

                  if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == Variable.class){
                      equation.add(e);
                  }
                  else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                      if(((StartParenthesis)equation.get(equation.size()-1)).hasEndParen()){

                          equation.add(e);
                      }
                      else {
                          ((StartParenthesis) equation.get(equation.size() - 1)).getEq().addItem(e);
                          ((StartParenthesis) equation.get(equation.size() - 1)).setDisplayItem(((StartParenthesis) equation.get(equation.size() - 1)).getDisplayItem());
                      }
                  }
              }

              else if(e.getClass() == Variable.class){

                  if(equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == Variable.class){

                      equation.add(new Operation(MULT));
                      equation.add(e);

                  }
                  else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                      if(((StartParenthesis)equation.get(equation.size()-1)).hasEndParen()){
                          equation.add(new Operation(MULT));
                          equation.add(e);
                      }
                      else {
                          ((StartParenthesis) equation.get(equation.size() - 1)).getEq().addItem(e);
                          ((StartParenthesis) equation.get(equation.size() - 1)).setDisplayItem(((StartParenthesis) equation.get(equation.size() - 1)).getDisplayItem());
                      }
                  }
                  else if(equation.get(equation.size()-1).getClass() != EndParenthesis.class){
                      equation.add(e);
                  }

              }
       }
       else{
           if(e.getClass() == Number.class || e.getClass() == StartParenthesis.class || e.getClass() == SpecialNumber.class || e.getClass() == Variable.class){
               equation.add(e);

               if(e.getClass() == StartParenthesis.class){
                   ((StartParenthesis) equation.get(equation.size() - 1)).setDisplayItem(((StartParenthesis) equation.get(equation.size() - 1)).getDisplayItem());
               }

           }
           else if(e.getClass() == Decimal.class){
               equation.add(new Number(0.0));
               equation.add(e);
           }
           else if(e.getClass() == NumberOperation.class){
               equation.add(e);
               equation.add(new StartParenthesis());
           }
       }

        this.setDisplayItem(this.getDisplayItem());
    }

    /**
     * Delete item method
     */
    protected void deleteItem(){
        if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

            if(!((StartParenthesis)equation.get(equation.size()-1)).getEq().isEmpty())
                ((StartParenthesis)equation.get(equation.size()-1)).getEq().deleteItem();

            else{
                equation.remove(equation.size()-1);

                if(!equation.isEmpty()){
                    if(equation.get(equation.size()-1).getClass() == NumberOperation.class){
                        equation.remove(equation.size()-1);
                    }
                }
            }
        }
        else{
            equation.remove(equation.size()-1);
        }

    }


    /**
     * Used to generate the next decimal value.
     * @param count Number of times needed to iterate
     * @param e The number to be moved downward
     * @return the decimal value
     */
    private BigDecimal decimalGenerator(int count, BigDecimal e){

        for(int i = 0; i < count; i++){
            e = e .divide(new BigDecimal(10));
        }

        return e;
    }



    @Override
    protected String getDisplayItem(){
        String builder = "";

        for(EquationPart e : equation){
            builder += e.getDisplayItem();
        }

        return builder;
    }

    @Override
    protected void setDisplayItem(String item) {
        super.setDisplayItem(this.getDisplayItem());
    }

    protected boolean endsInOperation(){
        if(equation.isEmpty())
            return false;
        else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){
            return ((StartParenthesis)equation.get(equation.size()-1)).getEq().endsInOperation();
        }
        else{
            return (equation.get(equation.size()-1).getClass() == Operation.class);
        }

    }

    protected boolean replaceVariable(Number x){

        for(int i =0; i < equation.size(); i++){
            if(equation.get(i).getClass() == Variable.class) {
                equation.set(i, x);
                return true;
            }
            if(equation.get(i).getClass() == StartParenthesis.class){
                if(((StartParenthesis)equation.get(i)).getEq().replaceVariable(x))
                    return true;
                else
                    continue;
            }

        }

        return false;
    }

    protected int numVars(int x){


        for(int i =0; i < equation.size(); i++){
            if(equation.get(i).getClass() == Variable.class){
                x++;
            }
            else if(equation.get(i).getClass() == StartParenthesis.class){
               x+= ((StartParenthesis)equation.get(i)).getEq().numVars(0);
            }

        }

        return x;
    }

    protected boolean hasEndParen(){

        return equation.get(equation.size()-1).getClass() == EndParenthesis.class;

    }

}
