package kylehoobler.agc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;


public class FormulaViewCalculator extends AppCompatActivity {

    private boolean isSaved = false;
    private final String EQUATION = "EQ";
    protected final String EQUATIONLIST = "equations";
    Equation equation = null;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_formula_calculator);
        text = (EditText)findViewById(R.id.DisplayNum);
        equation = new Equation(text);

        initCalculator();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putSerializable(EQUATION, equation.getEquation());

        super.onSaveInstanceState(outState);

    }

    protected void exit() {
        Intent x = new Intent(this, FormulaView.class);
        x.putExtra(EQUATION, equation.getEquation());

        startActivity(x);
    }

    private void initCalculator(){



        //Buttons in Scrolling view
        Button one = findViewById(R.id.One);
        Button two = findViewById(R.id.Two);
        Button three = findViewById(R.id.Three);
        Button four = findViewById(R.id.Four);
        Button five = findViewById(R.id.Five);
        Button six = findViewById(R.id.Six);
        Button seven = findViewById(R.id.Seven);
        Button eight = findViewById(R.id.Eight);
        Button nine = findViewById(R.id.Nine);
        Button zero = findViewById(R.id.Zero);
        Button negate = findViewById(R.id.negate);
        Button decimal = findViewById(R.id.Decimal);
        Button sine = findViewById(R.id.Sine);
        Button cosine = findViewById(R.id.cosine);
        Button tangent = findViewById(R.id.tangent);
        Button ln = findViewById(R.id.NaturalLog);
        Button log = findViewById(R.id.Log);
        Button factorial = findViewById(R.id.factorial);
        Button pi = findViewById(R.id.PI);
        Button E = findViewById(R.id.E);
        Button power = findViewById(R.id.Power);
        Button rParen = findViewById(R.id.RightParen);
        Button lParen = findViewById(R.id.LeftParen);
        Button sqrt = findViewById(R.id.SquareRoot);


        Button [] base = {one, two, three, four, five, six, seven, eight, nine, zero, negate, decimal, sine, cosine, tangent, ln, log, factorial, pi, E, power, rParen, lParen, sqrt};

        //Dims of screen
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;

        for (Button x : base){

            x.getLayoutParams().width = outMetrics.widthPixels / 4;


        }

        //Cancel or save buttons
        Button cancel = findViewById(R.id.cancel);
        Button save = findViewById(R.id.Save);

        //Top Bar Buttons
        Button clear = findViewById(R.id.Clear);
        Button delete = findViewById(R.id.Back);

        //Operators Buttons
        Button divide = findViewById(R.id.Divide);
        Button multiply = findViewById(R.id.Multiply);
        Button subtract = findViewById(R.id.Subtract);
        Button addItem = findViewById(R.id.addItem);

        //Variable
        Button var = findViewById(R.id.variable);



        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.clearEQ();

            }
        });

        pi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new SpecialNumber(EquationPart.PI));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new SpecialNumber(EquationPart.E));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(0.0));
                equation.updateTextView();
                text.setSelection(text.getText().length());

            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(1.0));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(2.0));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(3.0));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(4.0));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(5.0));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(6.0));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(7.0));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(8.0));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(9.0));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        decimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Decimal());
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equation != null || !equation.isEmpty()) {
                    equation.addItem(new Operation(EquationPart.ADD));
                    equation.updateTextView();
                    text.setSelection(text.getText().length());
                }
            }
        });

        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equation != null || !equation.isEmpty()) {
                    equation.addItem(new Operation(EquationPart.MULT));
                    equation.updateTextView();
                    text.setSelection(text.getText().length());
                }
            }
        });

        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equation != null || !equation.isEmpty()) {
                    equation.addItem(new Operation(EquationPart.DIV));
                    equation.updateTextView();
                    text.setSelection(text.getText().length());
                }

            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equation != null || !equation.isEmpty()) {
                    equation.addItem(new Operation(EquationPart.SUB));
                    equation.updateTextView();
                    text.setSelection(text.getText().length());
                }
            }
        });

        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equation != null || !equation.isEmpty()) {
                    equation.addItem(new Operation(EquationPart.POW));
                    equation.updateTextView();
                    text.setSelection(text.getText().length());
                }
            }
        });


        lParen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new StartParenthesis());
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        rParen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new EndParenthesis());
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        sine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.SIN));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        cosine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.COS));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        tangent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.TAN));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.LOG));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.LN));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        sqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.SQRT));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        negate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.NEG));
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        var.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Variable());
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        factorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new FactorialOperation());
                equation.updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!equation.isEmpty()) {
                    equation.deleteItem();
                    equation.updateTextView();
                    equation.setIsDecimal(false);
                    equation.setDecimalCount(0);
                    text.setSelection(text.getText().length());
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFormulaView();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSaved = true;

                Gson gson = new Gson();
                String item = gson.toJson(equation.getEquation());



                SharedPreferences.Editor editor = getSharedPreferences(EQUATIONLIST, MODE_PRIVATE).edit();

                editor.putString(EQUATION, item);

                editor.apply();

                exit();



            }
        });

    }

    private void launchFormulaView(){
        Intent tmp = new Intent(this, FormulaView.class);
        this.startActivity(tmp);

    }


}
