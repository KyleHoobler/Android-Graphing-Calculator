package kylehoobler.agc;

import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.widget.EditText;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * This class holds the full equation
 */
public class Equation extends EquationPart{

    private ArrayList<EquationPart> equation;
    private TextView text;
    private int decimalCount;
    private boolean isDecimal;



    Equation(){


        isDecimal = false;
        this.decimalCount = 0;
        equation = new ArrayList<>();

    }

    Equation(EditText text){

        isDecimal = false;
        decimalCount = 0;
        equation = new ArrayList<>();
        this.text = text;

    }

    /**
     * @param e add a custome equation in here
     */
     Equation(ArrayList<EquationPart> e){
        isDecimal = false;
        decimalCount = 0;
        equation = e;
    }

    protected ArrayList<EquationPart> getEquation(){
        return equation;
    }

    protected void setEquation(ArrayList<EquationPart> e){
        equation = e;
    }

    protected int getDecimalCount(){
        return this.decimalCount;
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
        text.setText(null);
        text.setTextSize(60);
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setHorizontallyScrolling(true);
    }

    /**
     * Gets the max priority and the position of that value
     * @return
     */
    @NonNull
    private Pair<Integer, Integer> getMaxPriority(){

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

    protected void solve(){

    try {
        if (!equation.isEmpty()) {
            if (equation.size() == 1 && equation.get(0).getClass() == StartParenthesis.class) {

                equation.set(0, ((StartParenthesis) equation.get(0)).ParenSolve());
            }

            while (equation.size() > 1) {


                Pair<Integer, Integer> tmp = getMaxPriority();
                Log.d("test2", equation.toString() + " " + tmp.second);


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

                    equation.set(tmp.second.intValue()-1, ((FactorialOperation)equation.get(tmp.second.intValue())).getFactorial((Number)equation.get(tmp.second.intValue()-1)));
                    equation.remove(tmp.second.intValue());
                }
                else if(equation.get(tmp.second.intValue()).getClass() == Decimal.class){
                    equation.remove(tmp.second.intValue());
                }
            }
        }
    }
    catch(Exception e){
        clearEQ();
        text.setText("An Error has occured.");
    }

    }

    /**
     * Handles the adding of items to the equation. Takes recursion into account and will add to a start parenthesis if it does not have an end parenthesis
     * @param e
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

                   else if(equation.get(equation.size()-1).getClass() == SpecialNumber.class){

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

                      else if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class || equation.get(equation.size()-1).getClass() == StartParenthesis.class){
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

                      else if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class){
                          equation.add(e);
                      }

                   }

                   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

               } else if (e.getClass() == NumberOperation.class) {
                   isDecimal = false;
                   decimalCount = 0;
                   NumberOperation val = (NumberOperation)e;

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

                      else if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class){
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
                   StartParenthesis val = (StartParenthesis)e;

                   if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class){
                       equation.add(new Operation(MULT));
                       equation.add(e);
                   }
                   else if(equation.get(equation.size()-1).getClass() == Operation.class){
                       equation.add(e);
                   }
                  else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                      if(((StartParenthesis)equation.get(equation.size()-1)).hasEndParen()){

                          equation.add(e);
                      }
                      else {
                          ((StartParenthesis) equation.get(equation.size() - 1)).getEq().addItem(e);
                      }

                  }

               }

              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

               else if (e.getClass() == EndParenthesis.class) {
                   isDecimal = false;
                   decimalCount = 0;
                   EndParenthesis val = (EndParenthesis)e;

                  //Parenthesis Check
                  if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                      ((StartParenthesis)equation.get(equation.size()-1)).getEq().addItem(e);
                  }
                  else{
                      equation.add(e);
                  }
               }

              ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

               else if (e.getClass() == FactorialOperation.class) {
                  isDecimal = false;
                  decimalCount = 0;
                  FactorialOperation val = (FactorialOperation) e;

                  if(equation.get(equation.size()-1).getClass() == Number.class || equation.get(equation.size()-1).getClass() == SpecialNumber.class){
                      equation.add(e);
                  }
                  else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                      if(((StartParenthesis)equation.get(equation.size()-1)).hasEndParen()){

                          equation.add(e);
                      }
                      else {
                          ((StartParenthesis) equation.get(equation.size() - 1)).getEq().addItem(e);
                      }
                  }
              }
       }
       else{
           if(e.getClass() == Number.class || e.getClass() == StartParenthesis.class || e.getClass() == SpecialNumber.class){
               equation.add(e);
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
    }

    protected void deleteItem(){
        if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

            if(!((StartParenthesis)equation.get(equation.size()-1)).getEq().isEmpty())
            ((StartParenthesis)equation.get(equation.size()-1)).getEq().deleteItem();
            else{
                equation.remove(equation.size()-1);
            }
        }
        else{
            equation.remove(equation.size()-1);
        }


    }


    protected void updateTextView(){

        if(text != null) {


            text.setText("");
            for (int i = 0; i < equation.size(); i++) {

                if(equation.get(i) == null){
                    clearEQ();
                    text.setText("Invalid Expression entered.");

                    return;
                }
                //Handles Number representation
                if (equation.get(i).getClass() == Number.class) {
                    if(decimalCount == 0)
                        text.setText(text.getText() + "" + ((Number) equation.get(i)).getValue().stripTrailingZeros().toPlainString());
                    else
                        text.setText(text.getText() + "" + ((Number) equation.get(i)).getDisplayItem());

                } else
                    text.setText(text.getText() + equation.get(i).getDisplayItem());
                }


                switch (text.getText().length()) {

                    case 12:
                        text.setTextSize(55);
                        break;
                    case 15:
                        text.setTextSize(50);
                        break;
                    case 18:
                        text.setTextSize(45);
                        break;
                    case 21:
                        text.setTextSize(40);
                        break;
                    case 24:
                        text.setTextSize(35);
                        break;

                }
            }
        }


    public BigDecimal decimalGenerator(int count, BigDecimal e){

        for(int i = 0; i < count; i++){
            e = e .divide(new BigDecimal(10));
        }

        return e;
    }





}
