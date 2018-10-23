package kylehoobler.agc;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.ArrayList;

import static kylehoobler.agc.EquationPart.MULT;


/**
 * This class holds the full equation
 */
public class Equation {

    private ArrayList<EquationPart> equation;
    private TextView text;
    private int decimalCount;
    private boolean isDecimal;



    protected Equation(){


        isDecimal = false;
        this.decimalCount = 0;
        equation = new ArrayList<EquationPart>();

    }

    protected Equation(TextView text){

        isDecimal = false;
        decimalCount = 0;
        equation = new ArrayList<EquationPart>();
        this.text = text;

    }

    protected Equation(ArrayList<EquationPart> e){
        isDecimal = false;
        decimalCount = 0;
        equation = e;
    }

    protected ArrayList<EquationPart> getEquation(){
        return equation;
    }

    protected boolean getIsDecimal(){
        return isDecimal;
    }

    protected void setEquation(ArrayList<EquationPart> e){
        equation = e;
    }

    protected int getDecimalCount(){
        return this.decimalCount;
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
        equation = new ArrayList<EquationPart>();
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
    protected Pair<Integer, Integer> getMaxPriority(){

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

    protected Equation solve(){




            int limit = 999;
            int i = 0;

            if(!equation.isEmpty()) {
                if (equation.size() == 1 && equation.get(0).getClass() == StartParenthesis.class){

                    equation.set(0,((StartParenthesis)equation.get(0)).ParenSolve());
                    updateTextView();
                }

                    while (equation.size() > 1) {

                        if (i > limit) {
                            Log.d("Test: ", "Num Iterations: " + i + " Equation size: " + equation.size() + " EQ: " + equation.toString());
                            break;

                        }
                        i++;
                        Log.d("test", "Before " + equation.size() + "");
                        Pair<Integer, Integer> tmp = getMaxPriority();
                        if (equation.get(tmp.second).getClass() == Operation.class) {

                            if (tmp.second != equation.size()) {

                                Log.d("test", "Content " + equation.toString()+"");
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

                                updateTextView();

                            }

                        } else if (equation.get(tmp.second).getClass() == EndParenthesis.class) {
                            equation.remove(equation.get(tmp.second));
                            updateTextView();
                        } else if (equation.get(tmp.second).getClass() == StartParenthesis.class) {
                            equation.set(tmp.second, ((StartParenthesis) equation.get(tmp.second)).ParenSolve());
                        }

                        Log.d("test", "After " + equation.size() + "");
                    }
            }






        return this;
    }

    protected void updateTextView(){

        if(text != null) {
            text.setText("");
            for (int i = 0; i < equation.size(); i++) {

                //Handles Number representation
                if (equation.get(i).getClass() == Number.class) {
                    if(decimalCount == 0)
                        text.setText(text.getText() + "" + ((Number) equation.get(i)).getValue().stripTrailingZeros().toPlainString());
                    else
                        text.setText(text.getText() + "" + ((Number) equation.get(i)).getValue());

                } else if (equation.get(i).getClass() == StartParenthesis.class) {
                    text.setText(text.getText() + equation.get(i).getDisplayItem());
                } else {
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
    }

    protected boolean add(Decimal e){


        if(!isDecimal) {
            if (equation.isEmpty()) {
                equation.add(new Number(new BigDecimal(0)));
                equation.add(e);
            }
            else if(equation.get(equation.size()-1).getClass() == Operation.class){
                equation.add(new Number(new BigDecimal(0)));
                equation.add(e);


            }
            else if (equation.get(equation.size() - 1).getClass() == Number.class) {
                if(!isDecimal)
                    equation.add(e);

            }
            else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                Equation tmp = ((StartParenthesis)equation.get(equation.size()-1)).getEq();
                if(!tmp.isEmpty()) {
                    if (tmp.get(tmp.size() - 1).getClass() != EndParenthesis.class) {
                        tmp.add(e);
                        ((StartParenthesis) equation.get(equation.size() - 1)).setEq(tmp);
                    }
                    else{
                        equation.add(new Operation(EquationPart.MULT));
                        equation.add(new Number(0.0));
                        equation.add(e);

                    }
                }
                else{
                    equation.add(new Number(0.0));
                    tmp.add(e);
                    ((StartParenthesis) equation.get(equation.size() - 1)).setEq(tmp);
                }

            }

            updateTextView();
        }
        return false;
    }


    protected boolean add(Number e){
        if(equation.isEmpty() || equation.size() == 0){
            equation.add(e);
            return true;
        }
        else if(equation.get(equation.size()-1).getClass() == Number.class && !isDecimal){
            //Complicated way to get the number and increment it's value
            equation.set(equation.size()-1, new Number(((Number)equation.get(equation.size()-1)).getValue().multiply(new BigDecimal(10)).add(e.getValue()) ));
            updateTextView();
        }
        else if(equation.get(equation.size()-1).getClass() == Decimal.class && !isDecimal){
            isDecimal = true;
            decimalCount = 1;
            equation.remove(equation.size()-1);

            if(e.getValue().intValue() != 0) {
                BigDecimal val = decimalGenerator(decimalCount, e.getValue());
                equation.set(equation.size() - 1, new Number(((Number) equation.get(equation.size() - 1)).getValue().add(val)));
                updateTextView();
            }
            else{

                if(text != null)
                //No way to add a empty value so just add it by setting text view. The increment of decimalCount will make this functional.
                text.setText(text.getText() +"0");
            }
        }
        else if(equation.get(equation.size()-1).getClass() == Number.class && isDecimal){
            decimalCount = decimalCount +1;

            if(e.getValue().intValue() != 0){
                BigDecimal val = decimalGenerator(decimalCount, e.getValue());
                equation.set(equation.size()-1, new Number(((Number)equation.get(equation.size()-1)).getValue().add( val )));
                updateTextView();
            }
            else{
                if(text != null)
                text.setText(text.getText() +"0");


            }


        }
        else if(equation.get(equation.size()-1).getClass() == SpecialNumber.class){

            equation.add(new Operation(MULT));
            equation.add(e);
            updateTextView();

        }
        else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

            Equation tmp = ((StartParenthesis)equation.get(equation.size()-1)).getEq();
            if(!tmp.isEmpty()) {
                if (tmp.get(tmp.size() - 1).getClass() != EndParenthesis.class) {
                    tmp.add(e);
                    ((StartParenthesis) equation.get(equation.size() - 1)).setEq(tmp);
                }
                else{
                    equation.add(new Operation(EquationPart.MULT));
                    equation.add(e);

                }
            }
            else{
                tmp.add(e);
                ((StartParenthesis) equation.get(equation.size() - 1)).setEq(tmp);
            }

        }


        else{
            isDecimal = false;
            decimalCount = 1;
            equation.add(e);
            updateTextView();
        }




        return false;
    }

    protected boolean add(StartParenthesis e){

        decimalCount = 0;
        isDecimal = false;

        if(equation.isEmpty()){
            equation.add(e);

        }
        else {

            if (equation.get(equation.size() - 1).getClass() == SpecialNumber.class || equation.get(equation.size() - 1).getClass() == Number.class) {
                equation.add(new Operation(EquationPart.MULT));
                equation.add(e);
            }
            else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                Equation tmp = ((StartParenthesis)equation.get(equation.size()-1)).getEq();
                if(!tmp.isEmpty()) {
                    if (tmp.get(tmp.size() - 1).getClass() != EndParenthesis.class) {
                        tmp.add(e);
                        ((StartParenthesis) equation.get(equation.size() - 1)).setEq(tmp);
                    }
                    else{
                        equation.add(new Operation(EquationPart.MULT));
                        equation.add(e);

                    }
                }
                else{
                    tmp.add(e);
                    ((StartParenthesis) equation.get(equation.size() - 1)).setEq(tmp);
                }

            }
            else{
                equation.add(e);
            }



            updateTextView();

        }



        return false;
    }


    /**
     * Determines if this can add a new end parenthesis
     * @param e
     * @return
     */
    protected boolean add(EndParenthesis e){

        decimalCount = 0;
        isDecimal = false;

        if(!(equation.isEmpty())){

            Log.d("test", equation.get(equation.size()-1).getClass().getSimpleName() + " Full EQ: " + equation.toString());
            if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                Log.d("test", equation.get(equation.size()-1).getClass().getSimpleName() + " Inner EQ: " + ((StartParenthesis)equation.get(equation.size()-1)).getEq());

                if(!((StartParenthesis)equation.get(equation.size()-1)).hasEndParen() && !((StartParenthesis)equation.get(equation.size()-1)).getEq().isEmpty())
                ((StartParenthesis)equation.get(equation.size()-1)).addToParen(e);


            }
        }
        return false;
    }


    protected boolean add(NumberOperation e){

        return false;
    }

    protected boolean add(Operation e){

        decimalCount = 0;
        isDecimal = false;

        if(!(equation.isEmpty())){

            if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                Equation tmp = ((StartParenthesis)equation.get(equation.size()-1)).getEq();
                if(!tmp.isEmpty()) {
                    if (tmp.get(tmp.size() - 1).getClass() != EndParenthesis.class) {
                        tmp.add(e);
                        ((StartParenthesis) equation.get(equation.size() - 1)).setEq(tmp);
                    }
                    else{
                        equation.add(e);

                    }
                }
                else{
                    tmp.add(e);
                    ((StartParenthesis) equation.get(equation.size() - 1)).setEq(tmp);
                }

            }

            else if(equation.get(equation.size()-1).getClass() != Operation.class )
                equation.add(e);

        }


        updateTextView();
        return false;
    }

    protected boolean add(SpecialNumber e){

        decimalCount = 0;
        isDecimal = false;

        if(equation.isEmpty() || equation.size() == 0){
            equation.add(e);
            updateTextView();
            return true;
        }
        else{
            if(equation.get(equation.size()-1).getClass() == Number.class){
                Operation multSpec = new Operation(MULT);


                equation.add(multSpec);
                equation.add(e);
                updateTextView();
            }
            else if(equation.get(equation.size()-1).getClass() == Operation.class){
                equation.add(e);
                updateTextView();
            }
            else if(equation.get(equation.size()-1).getClass() == StartParenthesis.class){

                Equation tmp = ((StartParenthesis)equation.get(equation.size()-1)).getEq();
                if(!tmp.isEmpty()) {
                    if (tmp.get(tmp.size() - 1).getClass() != EndParenthesis.class) {
                        tmp.add(e);
                        ((StartParenthesis) equation.get(equation.size() - 1)).setEq(tmp);
                    }
                    else{
                        equation.add(new Operation(EquationPart.MULT));
                        equation.add(e);

                    }
                }
                else{
                    tmp.add(e);
                    ((StartParenthesis) equation.get(equation.size() - 1)).setEq(tmp);
                }

            }




        }

        return false;
    }

    public BigDecimal decimalGenerator(int count, BigDecimal e){


        for(int i = 0; i < count; i++){
            e = e .divide(new BigDecimal(10));
        }

        return e;
    }


}
