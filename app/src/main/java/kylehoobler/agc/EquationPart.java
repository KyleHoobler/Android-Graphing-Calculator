package kylehoobler.agc;

import android.widget.TextView;

/**
 * Super class with priority system
 *
 * This will be the superclass for all items that go into an equation
 *
 * Made by Kyle Hoobler
 *
 * CPSC 498
 *
 */
public class EquationPart {

    private int priority;
    private String displayItem;
    protected int decimalLength;

    //Constants
    protected static final String ADD = "Add";
    protected static final String SUB = "Subtract";
    protected static final String MULT = "Multiply";
    protected static final String DIV = "Divide";
    protected static final String POW = "Power";
    protected static final String LOG = "Log";
    protected static final String NEG = "Negate";
    protected static final String SIN = "Sine";
    protected static final String COS = "Cosine";
    protected static final String TAN = "Tangent";
    protected static final String LN = "LN";
    protected static final String SQRT = "Square Root";
    protected static final String FAC = "Factorial";
    protected static final String PI = "Pi";
    protected static final String E = "e";
    protected static final String dispPlus = "+";
    protected static final String dispSub = "-";
    protected static final String dispTimes = "*";
    protected static final String dispDiv = "/";
    protected static final String dispSin = "sin";
    protected static final String dispCos = "cos";
    protected static final String dispTan = "tan";
    protected static final String dispLog = "log";
    protected static final String dispLn = "ln";
    protected static final String dispPow = "^";
    protected static final String dispFac = "!";
    protected static final String dispPi = "π";
    protected static final String dispSQRT = "√";


    protected EquationPart(){

        decimalLength = 10;
        displayItem = "";
        //default priority of 0
        priority = 0;

    }

    protected void setDisplayItem(String item){
        displayItem = item;
    }

    protected String getDisplayItem(){
        return displayItem;
    }


    public void setPriority(int val){

        priority = (val < 0) ? 0 : val;
    }

    public int getPriority(){
        return priority;
    }

    public boolean addPriority(int i){

        //Check if value is negative for case use subtractPriority
        if(i > 0) {
            this.setPriority(this.getPriority() + i);
            return true;
        }
        return false;
    }

    public boolean subtractPriority(int i){

        if(this.getPriority() - i < 0){
            this.setPriority(0);

            //Return false if value goes negative this would be an error of some sort
            return false;
        }
        else
            this.setPriority(this.getPriority() - i);

        return true;
    }




}
