package kylehoobler.agc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.util.ArrayList;


public class Calculator extends AppCompatActivity {


    boolean disabled = false;
    private Equation equation = new Equation();
    private EditText text;


    private void initFormulaPage() {

        Intent intent = new Intent(this, FormulaIntent.class);

        startActivity(intent);

    }

    private void initGraphPage() {

        Intent gr = new Intent(this, GraphIntent.class);
        this.startActivity(gr);

    }


    @SuppressLint("NewApi")
    private void initCalculator() {
        setContentView(R.layout.activity_calculator);

        initCalcView();


    }

    protected void initAboutPage() {
        Intent gr = new Intent(this, AboutProject.class);
        this.startActivity(gr);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initCalcView() {


        final ImageButton menuButton = findViewById(R.id.menuButton);
        text = (EditText) findViewById(R.id.DisplayNum);
        text.setFocusable(false);
        text.setLongClickable(false);


        equation = new Equation();
        text.setMovementMethod(new ScrollingMovementMethod());
        text.setHorizontallyScrolling(true);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Calculator.this, menuButton);

                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals("Formulas")) {
                            initFormulaPage();
                        } else if (item.getTitle().equals("Graph")) {
                            initGraphPage();
                        } else if (item.getTitle().equals("About")) {
                            initAboutPage();
                        }

                        return false;
                    }
                });


                popupMenu.show();
            }
        });

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
        float density = getResources().getDisplayMetrics().density;

        for (Button x : base) {

            x.getLayoutParams().width = outMetrics.widthPixels / 4;

        }

        //Top Bar Buttons
        Button clear = findViewById(R.id.Clear);
        Button delete = findViewById(R.id.Back);

        //Operators Buttons
        Button divide = findViewById(R.id.Divide);
        Button multiply = findViewById(R.id.Multiply);
        Button subtract = findViewById(R.id.Subtract);
        Button addItem = findViewById(R.id.addItem);

        //Equals
        final Button equals = findViewById(R.id.equals);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.clearEQ();
                text.setText(null);
                text.setTextSize(55);
                text.setMovementMethod(new ScrollingMovementMethod());
                text.setHorizontallyScrolling(true);

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

        equals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!equation.isEmpty()) {
                    equation.solve();
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
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getTitle().toString()) {
            case "Calculator":
                initCalculator();
                return true;
            case "Graph":
                initGraphPage();
                return true;
            case "Formulas":
                initFormulaPage();
                return true;
            case "About":
                initAboutPage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initCalculator();

        if (savedInstanceState != null) {
            equation.setEquation((ArrayList) savedInstanceState.get("eq"));
            equation.decimalCount = (int) savedInstanceState.get("decCount");
            if (equation.decimalCount > 0)
                equation.setIsDecimal(true);

            updateTextView();
        }

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
                    equation.addThis(new ErrorItem());

                    return;
                }
                //Handles Number representation
                if (equation.get(i).getClass() == Number.class && equation.decimalCount == 0) {
                    text.setText(text.getText() + "" + ((Number) equation.get(i)).getValue().stripTrailingZeros().toPlainString());
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
                case 36:
                    text.setTextSize(30);

            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        outState.putSerializable("eq", equation.getEquation());
        outState.putSerializable("decCount", equation.decimalCount);
    }


}
