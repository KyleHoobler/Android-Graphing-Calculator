package kylehoobler.agc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.text.InputType.TYPE_CLASS_TEXT;


public class Calculator extends AppCompatActivity {


    private ArrayList<String> tmp;
    private int layoutID;
    boolean disabled = false;

    private static final int CALCVIEW = 0;
    private static final int GRAPHVIEW = 1;
    private static final int FORMULAVIEW = 2;

    private Equation equation = new Equation();
    private char tmpVar;

    private EditText text;


    private void initFormulaPage(){


        layoutID = FORMULAVIEW;
        setContentView(R.layout.activity_equations);
        Toolbar myToolBar = findViewById(R.id.my_toolbar);


        ListView listView = findViewById(R.id.ListView);
        setSupportActionBar(myToolBar);

        tmp = new ArrayList<>();

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, tmp);
        listView.setAdapter(listAdapter);

       // testList();

        listAdapter.notifyDataSetChanged();


        if(tmp == null || tmp.isEmpty()) {
            TextView emptyText = findViewById(R.id.empty);
            emptyText.setVisibility(View.VISIBLE);
            listView.setEmptyView(emptyText);
        }

    }

    private void initGraphPage(){

        layoutID = GRAPHVIEW;
        setContentView(R.layout.activity_graph);
        Toolbar myToolBar = findViewById(R.id.GraphBar);
        setSupportActionBar(myToolBar);


    }


    @SuppressLint("NewApi")
    private void initCalculator(){
        setContentView(R.layout.activity_calculator);
        layoutID = CALCVIEW;

        initCalcView();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initCalcView(){


        final ImageButton menuButton = findViewById(R.id.menuButton);
        text = (EditText) findViewById(R.id.DisplayNum);



        equation = new Equation(text);
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

                        if(item.getTitle().equals("Formulas")){
                            initFormulaPage();
                        }
                        else if(item.getTitle().equals("Graph")){
                            initGraphPage();
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

        Button [] base = {one, two, three, four, five, six, seven, eight, nine, zero, negate, decimal, sine, cosine, tangent, ln, log, factorial, pi, E, power, rParen, lParen, sqrt};

        //Dims of screen
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;

        for (Button x : base){

            x.getLayoutParams().width = outMetrics.widthPixels / 4;


        }

        //Top Bar Buttons
        Button clear = findViewById(R.id.Clear);
        Button delete = findViewById(R.id.Back);

        //Operators Buttons
        Button divide = findViewById(R.id.Divide);
        Button multiply = findViewById(R.id.Multiply);
        Button subtract = findViewById(R.id.Subtract);
        Button addItem = findViewById(R.id.Add);

        //Equals
        final Button equals = findViewById(R.id.equals);


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

        equals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.solve();
                equation.updateTextView();
                text.setSelection(text.getText().length());
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

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem tmp = menu.findItem(R.id.Add);
        return super.onCreateOptionsMenu(menu);
    }

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(layoutID == CALCVIEW){
            initCalcView();
        }
    }



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initCalculator();

            if(savedInstanceState != null){
                equation.setEquation((ArrayList)savedInstanceState.get("eq"));
                equation.updateTextView();
            }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        // Save our own state now
        outState.putSerializable("eq", equation.getEquation());
    }




    public void makeToast(String text){
        Toast.makeText(this.getBaseContext(),text, Toast.LENGTH_SHORT).show();
    }










}
