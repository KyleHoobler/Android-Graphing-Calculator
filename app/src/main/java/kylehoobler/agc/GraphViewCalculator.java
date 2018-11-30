package kylehoobler.agc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;


public class GraphViewCalculator extends AppCompatActivity {

    protected final String EQUATION = "EQ";
    protected final String GRAPHLIST = "GraphEquations";
    Equation equation = null;
    Button save;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_formula_calculator);
        text = (EditText) findViewById(R.id.DisplayNum);
        text.setFocusable(false);
        equation = new Equation();

        if (savedInstanceState != null) {
            equation.setEquation(((ArrayList) savedInstanceState.get("eq")));
            equation.decimalCount = (int) savedInstanceState.get("decCount");
            if (equation.decimalCount > 0)
                equation.setIsDecimal(true);

        }

        initCalculator();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putSerializable(EQUATION, equation.getEquation());
        outState.putSerializable("eq", equation.getEquation());
        outState.putSerializable("decCount", equation.decimalCount);

        super.onSaveInstanceState(outState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    protected void exit() {
        Intent x = new Intent(this, GraphIntent.class);
        x.putExtra(EQUATION, equation.getEquation());

        startActivity(x);
    }

    private void initCalculator() {

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


        Button[] base = {one, two, three, four, five, six, seven, eight, nine, zero, negate, decimal, sine, cosine, tangent, ln, log, factorial, pi, E, power, rParen, lParen, sqrt};

        //Dims of screen
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        for (Button x : base) {
            x.getLayoutParams().width = outMetrics.widthPixels / 4;
        }

        //Cancel or save buttons
        Button cancel = findViewById(R.id.cancel);
        save = findViewById(R.id.Save);

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
        disableSaveButton();

        text.setTextSize(60);
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setHorizontallyScrolling(true);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation = new Equation();
                updateTextView();
            }
        });

        pi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new SpecialNumber(EquationPart.PI));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new SpecialNumber(EquationPart.E));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(0.0));
                updateTextView();
                text.setSelection(text.getText().length());

            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(1.0));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });


        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(2.0));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(3.0));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(4.0));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(5.0));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(6.0));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(7.0));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(8.0));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Number(9.0));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        decimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Decimal());
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (equation != null || !equation.isEmpty()) {
                    equation.addItem(new Operation(EquationPart.ADD));
                    updateTextView();
                    text.setSelection(text.getText().length());
                }
            }
        });

        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (equation != null || !equation.isEmpty()) {
                    equation.addItem(new Operation(EquationPart.MULT));
                    updateTextView();
                    text.setSelection(text.getText().length());
                }
            }
        });

        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (equation != null || !equation.isEmpty()) {
                    equation.addItem(new Operation(EquationPart.DIV));
                    updateTextView();
                    text.setSelection(text.getText().length());
                }

            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (equation != null || !equation.isEmpty()) {
                    equation.addItem(new Operation(EquationPart.SUB));
                    updateTextView();
                    text.setSelection(text.getText().length());
                }
            }
        });

        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (equation != null || !equation.isEmpty()) {
                    equation.addItem(new Operation(EquationPart.POW));
                    updateTextView();
                    text.setSelection(text.getText().length());
                }
            }
        });


        lParen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new StartParenthesis());
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        rParen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new EndParenthesis());
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        sine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.SIN));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        cosine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.COS));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        tangent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.TAN));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.LOG));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.LN));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        sqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.SQRT));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        negate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new NumberOperation(EquationPart.NEG));
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        var.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new Variable());
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        factorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.addItem(new FactorialOperation());
                updateTextView();
                text.setSelection(text.getText().length());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!equation.isEmpty()) {
                    equation.deleteItem();
                    updateTextView();
                    equation.setIsDecimal(false);
                    equation.setDecimalCount(0);
                    text.setSelection(text.getText().length());
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGraphView();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> items = new ArrayList<>();

                SharedPreferences prefs = getSharedPreferences(GRAPHLIST, MODE_PRIVATE);


                String eqListGSON = prefs.getString(GRAPHLIST, null);

                final Gson conv = new Gson();


                if (eqListGSON != null) {
                    items = conv.fromJson(eqListGSON, ArrayList.class);
                }

                String saved = new SaveBuilder().convertToString(equation);

                items.add(saved);

                eqListGSON = conv.toJson(items);

                SharedPreferences.Editor edit = getSharedPreferences(GRAPHLIST, MODE_PRIVATE).edit();
                edit.clear();
                edit.putString(GRAPHLIST, eqListGSON);
                edit.apply();

                exit();


            }
        });

    }

    /**
     * Used to update a textview that is associated with the equation.
     */
    protected void updateTextView() {

        if (text != null) {

            text.setText("");
            for (int i = 0; i < equation.size(); i++) {

                if (equation.get(i) == null) {
                    equation.clearEQ();
                    text.setText("Invalid Expression entered.");

                    return;
                }

                //Handles Number representation
                if (equation.get(i).getClass() == Number.class) {
                    if (equation.decimalCount == 0) {
                        text.setText(text.getText() + "" + ((Number) equation.get(i)).getValue().stripTrailingZeros().toPlainString());
                    } else
                        text.setText(text.getText() + "" + ((Number) equation.get(i)).getDisplayItem());

                } else
                    text.setText(text.getText() + equation.get(i).getDisplayItem());
            }


            disableSaveButton();

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

    private void disableSaveButton() {
        if (!equation.isEmpty()) {
            if (equation.endsInOperation()) {
                save.setAlpha(.5f);
                save.setEnabled(false);
            } else {
                save.setEnabled(true);
                save.setAlpha(1);
            }

        } else {
            save.setAlpha(.5f);
            save.setEnabled(false);
        }
    }

    private void launchGraphView() {
        Intent tmp = new Intent(this, GraphIntent.class);
        this.startActivity(tmp);
    }

}
