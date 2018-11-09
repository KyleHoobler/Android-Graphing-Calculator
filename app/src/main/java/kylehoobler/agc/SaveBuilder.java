package kylehoobler.agc;

import android.util.Log;

import java.math.BigDecimal;
import java.util.Scanner;

public class SaveBuilder {

    public SaveBuilder(){

    }

    public String convertToString(Equation e){
        String builder = "";

        for(int i = 0; i < e.size(); i++){

            builder += "[";
            builder += e.get(i).getClass().getSimpleName() + ":" + e.get(i).getDisplayItem() +":";
            switch (e.get(i).getClass().getSimpleName()){
                case "Number":
                    builder += ((Number)e.get(i)).getValue().stripTrailingZeros().toString(); break;
                case "Operation":
                    builder += ((Operation)e.get(i)).operation; break;
                case "StartParenthesis":
                    builder += '\n';
                    builder += convertToString(((StartParenthesis)e.get(i)).getEq()); break;
                case "NumberOperation":
                    builder += ((NumberOperation)e.get(i)).operation; break;
                case "SpecialNumber":
                    builder += ((SpecialNumber)e.get(i)).opcode; break;
                default: builder += "NA"; break;

            }

            builder += "]";
            builder += '\n';
        }


        return builder;
    }


    public Equation convertToEquation(String e){
        Equation equation = new Equation();
        Scanner str = new Scanner(e);
        str.useDelimiter("\n");

        while(str.hasNext()) {
            String item = str.next();

            if(item.length() > 2) {
                item = item.substring(1, item.length() - 1);
                String[] splitter = item.split(":");

                switch (splitter[0]) {
                    case "Number":
                        equation.addItem(new Number(new BigDecimal(splitter[2])));
                        break;
                    case "Operation":
                        equation.addItem(new Operation(splitter[2]));
                        break;
                    case "SpecialNumber":
                        equation.addItem(new SpecialNumber(splitter[2]));
                        break;
                    case "NumberOperation":
                        equation.addItem(new NumberOperation(splitter[2]));
                        str.next();
                    case "FactorialOperation":
                        equation.addItem(new FactorialOperation());
                        break;
                    case "EndParenthesis":
                        equation.addItem(new EndParenthesis());
                        break;
                    case "StartParenthesis":
                        equation.addItem(new StartParenthesis());
                        break;
                    case "Variable":
                        equation.addItem(new Variable());
                        break;


                }
            }




            Log.d("Item", item);
        }

        str.close();
        return equation;
    }


}
