package kylehoobler.agc;

import android.support.annotation.NonNull;
import android.util.Pair;
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

            //If item is just a Start Parenthesis
            if (equation.size() == 1 && equation.get(0).getClass() == StartParenthesis.class) {

                equation.set(0, ((StartParenthesis) equation.get(0)).ParenSolve());
            }

            //While there is still items in the list aside from the end number continue
            while (equation.size() > 1) {

                //Gets the highest priority and it's location
                Pair<Integer, Integer> tmp = getMaxPriority();

                //If Highest is an operation object
                if (equation.get(tmp.second).getClass() == Operation.class) {

                    if (tmp.second != equation.size()) {

                        //Solve the operation
                        equation.set(tmp.second, ((Operation) equation.get(tmp.second)).solveOp((Number) equation.get(tmp.second - 1), (Number) equation.get(tmp.second + 1)));

                        //If the result is a decimal
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

                    //Solve number op
                    equation.set(tmp.second, ((NumberOperation) equation.get(tmp.second)).numOpSolve((Number) equation.get(tmp.second + 1)));
                    equation.remove(tmp.second+1);

                }
                else if (equation.get(tmp.second).getClass() == EndParenthesis.class) {
                    //Remove end parenthesis since equation will be properly sturctured without it
                    equation.remove(equation.get(tmp.second));

                } else if (equation.get(tmp.second).getClass() == StartParenthesis.class) {
                    //Solve the object in the parenthesis
                    equation.set(tmp.second, ((StartParenthesis) equation.get(tmp.second)).ParenSolve());
                }
                else if(equation.get(tmp.second).getClass() == FactorialOperation.class){
                    try {

                        //Try to solve for facotrial operation
                        Number item = ((FactorialOperation) equation.get(tmp.second)).getFactorial((Number) equation.get(tmp.second - 1));

                        //if number is too large item's value is null
                        if(item.getValue() == null)
                            throw new Exception();

                        //Replace item and remove factorial
                        equation.set(tmp.second - 1, item);
                        equation.remove(tmp.second.intValue());
                    }
                    catch(Exception e){

                        //Add error
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


        //If the number is a decimal value set the new decimal count value
        if(((Number)equation.get(0)).getValue().doubleValue() != ((Number)equation.get(0)).getValue().intValue()) {
            isDecimal = true;
            String x = ((Number) equation.get(0)).getValue().doubleValue()+"";
            int index = x.indexOf(".");

            decimalCount = index < 0 ? 0 : x.length() - index - 1;
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

                       if(((Number)equation.get(equation.size()-1)).getValue().doubleValue() >= 0)
                       equation.set(equation.size()-1, new Number(((Number)equation.get(equation.size()-1)).getValue().multiply(new BigDecimal(10)).add(val.getValue()) ));
                       else{
                           equation.set(equation.size()-1, new Number(((Number)equation.get(equation.size()-1)).getValue().multiply(new BigDecimal(10)).subtract(val.getValue()) ));
                       }

                   }
                   //Decimal but needs to be declared as a decimal
                   else if(equation.get(equation.size()-1).getClass() == Decimal.class && !isDecimal){
                       isDecimal = true;
                       decimalCount++;

                       //Remove the decimal object
                       equation.remove(equation.get(equation.size()-1));


                       if(val.getValue().intValue() != 0) {
                           BigDecimal dec = decimalGenerator(decimalCount, val.getValue());
                           if(((Number)equation.get(equation.size()-1)).getValue().doubleValue() >= 0)
                           equation.set(equation.size() - 1, new Number(((Number) equation.get(equation.size() - 1)).getValue().add(dec)));
                           else
                               equation.set(equation.size() - 1, new Number(((Number) equation.get(equation.size() - 1)).getValue().subtract(dec)));
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

                           if(((Number)equation.get(equation.size()-1)).getValue().doubleValue() >= 0)
                           equation.set(equation.size() - 1, new Number(((Number) equation.get(equation.size() - 1)).getValue().add(dec)));
                           else
                               equation.set(equation.size() - 1, new Number(((Number) equation.get(equation.size() - 1)).getValue().subtract(dec)));
                       }else{
                           equation.get(equation.size()-1).setDisplayItem(equation.get(equation.size()-1).getDisplayItem()+"0");
                       }
                   }

                   //Cases where item needs to be multiplied first
                   else if(equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == Variable.class || equation.get(equation.size()-1).getClass() == FactorialOperation.class || equation.get(equation.size()-1).getClass() == EndParenthesis.class){

                       equation.add(new Operation(MULT));
                       equation.add(e);

                   }

                   //If operation simply add
                   else if(equation.get(equation.size()-1).getClass() == Operation.class){
                       equation.add(val);
                   }

                   //If StartParen
                  else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                       //If parenthesis is over
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

              //Decimal class cases
               else if (e.getClass() == Decimal.class) {
                   Decimal val = (Decimal)e;

                   if(equation.get(equation.size()-1).getClass() == Number.class && !isDecimal){
                       equation.add(val);
                   }
                   else if(equation.get(equation.size()-1).getClass() == Operation.class){
                       equation.add(new Number(0.0));
                       equation.add(val);
                   }
                   else if(equation.get(equation.size()-1).getClass() == FactorialOperation.class|| equation.get(equation.size()-1).getClass() == EndParenthesis.class){
                       equation.add(new Operation(MULT));
                       equation.add(new Number(0.0));
                       equation.add(e);
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

               }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

              //Case if Special Number
               else if (e.getClass() == SpecialNumber.class) {
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
                      else if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == StartParenthesis.class || equation.get(equation.size()-1).getClass() == Variable.class || equation.get(equation.size()-1).getClass() == FactorialOperation.class|| equation.get(equation.size()-1).getClass() == FactorialOperation.class){
                          equation.add(new Operation(MULT));
                          equation.add(e);
                      }
                      else if(equation.get(equation.size()-1).getClass() == Operation.class){
                          equation.add(e);
                      }

                  }

               }
              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

              //Operation class type
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
                      else if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == Variable.class || equation.get(equation.size()-1).getClass() == FactorialOperation.class || equation.get(equation.size()-1).getClass() == EndParenthesis.class){
                          equation.add(e);
                      }

                   }

               }
              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

              //If Number Operation class
              else if (e.getClass() == NumberOperation.class) {
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
                      else if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == Variable.class || equation.get(equation.size()-1).getClass() == FactorialOperation.class|| equation.get(equation.size()-1).getClass() == EndParenthesis.class){
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


              //If start parenthesis case
               else if (e.getClass() == StartParenthesis.class) {
                   isDecimal = false;
                   decimalCount = 0;

                   if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class ||equation.get(equation.size()-1).getClass() == Variable.class || equation.get(equation.size()-1).getClass() == FactorialOperation.class|| equation.get(equation.size()-1).getClass() == EndParenthesis.class){
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

              //End parenthesis
               else if (e.getClass() == EndParenthesis.class) {
                   isDecimal = false;
                   decimalCount = 0;

                  //Parenthesis Check
                  if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                      ((StartParenthesis)equation.get(equation.size()-1)).eq.addItem(e);

                  }
                  else if(equation.get(equation.size()-1).getClass() == EndParenthesis.class){

                  }
                  else{
                      if(equation.get(equation.size()-1).getClass() != Operation.class && !equation.isEmpty())
                      equation.add(e);
                  }


               }

              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

              //Factorial operation
              else if (e.getClass() == FactorialOperation.class) {
                  isDecimal = false;
                  decimalCount = 0;

                  if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == Variable.class|| equation.get(equation.size()-1).getClass() == EndParenthesis.class){
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

              //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

              //If item is a variable
              else if(e.getClass() == Variable.class){

                  if(equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == Variable.class || equation.get(equation.size()-1).getClass() == FactorialOperation.class || equation.get(equation.size()-1).getClass() == EndParenthesis.class || equation.get(equation.size()-1).getClass() == EndParenthesis.class){

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
                  else if(equation.get(equation.size()-1).getClass() == Decimal.class){

                  }
                  else if(equation.get(equation.size()-1).getClass() != EndParenthesis.class){
                      equation.add(e);
                  }

              }
       }

       //If the equation is empty
       else{
           if(e.getClass() == Number.class || e.getClass() == StartParenthesis.class || e.getClass() == SpecialNumber.class || e.getClass() == Variable.class ){
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

            if(!((StartParenthesis)equation.get(equation.size()-1)).getEq().isEmpty()) {
                ((StartParenthesis) equation.get(equation.size() - 1)).getEq().deleteItem();

            } else{

                equation.remove(equation.size()-1);

                if(!equation.isEmpty()){
                    if(equation.get(equation.size()-1).getClass() == NumberOperation.class){
                        equation.remove(equation.size()-1);
                    }
                }
            }
        }
        else{

            if(equation.get(equation.size()-1).getClass() != Number.class) {
                equation.remove(equation.size() - 1);
            }
            else{

                Number tmp = (Number)equation.get(equation.size()-1);

                if(tmp.getValue().doubleValue() == tmp.getValue().intValue()){
                    if(tmp.getValue().intValue() < 10){
                        equation.remove(equation.size()-1);
                        return;
                    }
                }

                String value = tmp.getValue().toPlainString();
                value = value.substring(0, value.length()-1);

                equation.set(equation.size()-1, new Number(new BigDecimal(value)));

            }
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


    /**
     * Updates display item
     * @return new display item
     */
    @Override
    protected String getDisplayItem(){
        String builder = "";

        for(EquationPart e : equation){
            builder += e.getDisplayItem();
        }

        return builder;
    }

    /**
     * modified version of setdisplay item
     * @param item
     */
    @Override
    protected void setDisplayItem(String item) {
        super.setDisplayItem(this.getDisplayItem());
    }

    /**
     * Returns true if item ends in an operatoin
     * @return
     */
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

    /**
     * Replaces the first available variable in an equation with a number
     * @param x the number
     * @return
     */
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

    /**
     * Replace all variables in an equation
     * This is used for the graph
     * @param num
     */
    protected void replaceAllVariables(int num){

        for(int i = 0; i < equation.size(); i++){
            if(equation.get(i).getClass() == Variable.class) {
                equation.set(i, new Number(new BigDecimal(num)));
            }
            else if(equation.get(i).getClass() == StartParenthesis.class){
                ((StartParenthesis)equation.get(i)).getEq().replaceAllVariables(num);
            }
        }

    }


    /**
     * Gets the number of variables in an equation
     * @param x recursive way to go into parenthesis
     * @return
     */
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

    /**
     * Brute force add item to the end of an equation
     * @param e
     */
    protected void addThis(EquationPart e){
        equation.add(e);
    }
    

}
