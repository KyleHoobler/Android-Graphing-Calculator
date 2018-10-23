package kylehoobler.agc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class Calculator extends AppCompatActivity {


    private ArrayList<String> tmp;
    private int layoutID;

    private static final int CALCVIEW = 0;
    private static final int GRAPHVIEW = 1;
    private static final int FORMULAVIEW = 2;

    private Equation equation = new Equation();
    private char tmpVar;

    private TextView textView;


    private void initFormulaPage(){


        layoutID = FORMULAVIEW;
        setContentView(R.layout.activity_equations);
        Toolbar myToolBar = (Toolbar) findViewById(R.id.my_toolbar);


        ListView listView = (ListView)findViewById(R.id.ListView);
        setSupportActionBar(myToolBar);

        tmp = new ArrayList<>();

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, tmp);
        listView.setAdapter(listAdapter);

       // testList();

        listAdapter.notifyDataSetChanged();


        if(tmp == null || tmp.isEmpty()) {
            TextView emptyText = (TextView) findViewById(R.id.empty);
            emptyText.setVisibility(View.VISIBLE);
            listView.setEmptyView(emptyText);
        }

    }

    private void initGraphPage(){

        layoutID = GRAPHVIEW;
        setContentView(R.layout.activity_graph);
        Toolbar myToolBar = (Toolbar) findViewById(R.id.GraphBar);
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


        final ImageButton menuButton = (ImageButton) findViewById(R.id.menuButton);
        textView = (TextView) findViewById(R.id.DisplayNum);
        equation = new Equation(textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setHorizontallyScrolling(true);

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
        Button one = (Button)findViewById(R.id.One);
        Button two = (Button)findViewById(R.id.Two);
        Button three = (Button)findViewById(R.id.Three);
        Button four = (Button)findViewById(R.id.Four);
        Button five = (Button)findViewById(R.id.Five);
        Button six = (Button)findViewById(R.id.Six);
        Button seven = (Button)findViewById(R.id.Seven);
        Button eight = (Button)findViewById(R.id.Eight);
        Button nine = (Button)findViewById(R.id.Nine);
        Button zero = (Button)findViewById(R.id.Zero);
        Button negate = (Button)findViewById(R.id.negate);
        Button decimal = (Button)findViewById(R.id.Decimal);
        Button sine = (Button)findViewById(R.id.Sine);
        Button cosine = (Button)findViewById(R.id.cosine);
        Button tangent = (Button)findViewById(R.id.tangent);
        Button ln = (Button)findViewById(R.id.NaturalLog);
        Button log = (Button)findViewById(R.id.Log);
        Button factorial = (Button)findViewById(R.id.factorial);
        Button pi = (Button)findViewById(R.id.PI);
        Button E = (Button)findViewById(R.id.E);
        Button power = (Button)findViewById(R.id.Power);
        Button rParen = (Button)findViewById(R.id.RightParen);
        Button lParen = (Button)findViewById(R.id.LeftParen);
        Button sqrt = (Button)findViewById(R.id.SquareRoot);

        Button [] base = {one, two, three, four, five, six, seven, eight, nine, zero, negate, decimal, sine, cosine, tangent, ln, log, factorial, pi, E, power, rParen, lParen, sqrt};

        //Dims of screen
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;

        for (Button x : base){

            x.getLayoutParams().width = (int)(outMetrics.widthPixels / 4);


        }

        //Top Bar Buttons
        Button clear = (Button)findViewById(R.id.Clear);
        Button clearAll = (Button)findViewById(R.id.ClearEvery);
        Button delete = (Button)findViewById(R.id.Back);

        //Operators Buttons
        Button divide = (Button)findViewById(R.id.Divide);
        Button multiply = (Button)findViewById(R.id.Multiply);
        Button subtract = (Button)findViewById(R.id.Subtract);
        Button add = (Button)findViewById(R.id.Add);

        //Equals
        Button equals = (Button)findViewById(R.id.equals);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.clearEQ();

            }
        });

        pi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new SpecialNumber(EquationPart.PI));
                equation.updateTextView();
            }
        });

        E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new SpecialNumber(EquationPart.E));
                equation.updateTextView();
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                equation.add(new Number(0.0));

                if(equation.get(equation.size() -1).getClass() == StartParenthesis.class) {
                    equation.updateTextView();

                    if(((StartParenthesis)equation.get(equation.size()-1)).getEq().getDecimalCount() == 1)
                    textView.setText(textView.getText() + ".0");
                    else if(((StartParenthesis)equation.get(equation.size()-1)).getEq().getDecimalCount() > 1){
                        textView.setText(textView.getText()+"0");
                    }
                }

            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new Number(1.0));
                equation.updateTextView();
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new Number(2.0));
                equation.updateTextView();
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new Number(3.0));
                equation.updateTextView();
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new Number(4.0));
                equation.updateTextView();
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new Number(5.0));
                equation.updateTextView();
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new Number(6.0));
                equation.updateTextView();
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new Number(7.0));
                equation.updateTextView();
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new Number(8.0));
                equation.updateTextView();
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new Number(9.0));
                equation.updateTextView();
            }
        });

        decimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new Decimal());
                equation.updateTextView();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equation != null || !equation.isEmpty()) {
                    equation.add(new Operation(EquationPart.ADD));
                    equation.updateTextView();
                }
            }
        });

        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equation != null || !equation.isEmpty()) {
                    equation.add(new Operation(EquationPart.MULT));
                    equation.updateTextView();
                }
            }
        });

        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equation != null || !equation.isEmpty()) {
                    equation.add(new Operation(EquationPart.DIV));
                    equation.updateTextView();
                }

            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equation != null || !equation.isEmpty()) {
                    equation.add(new Operation(EquationPart.SUB));
                    equation.updateTextView();
                }
            }
        });

        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equation != null || !equation.isEmpty()) {
                    equation.add(new Operation(EquationPart.POW));
                    equation.updateTextView();
                }
            }
        });

        equals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.solve();
            }
        });

        lParen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new StartParenthesis(textView));
                equation.updateTextView();
            }
        });

        rParen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equation.add(new EndParenthesis());
                equation.updateTextView();
            }
        });


    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void solveEquation(){

    }

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

    }

    @Override
    public void onDestroy() {



        super.onDestroy();

    }


    public void testList(){
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");
        tmp.add("test");


    }

    public void makeToast(String text){
        Toast.makeText(this.getBaseContext(),text, Toast.LENGTH_SHORT).show();
    }










}
