package kylehoobler.agc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;

public class FormulaView extends AppCompatActivity implements Serializable{

    protected final String EQUATIONLIST = "equations";

    protected SharedPreferences sharedPreference;

    private final String EQUATION = "EQ";
    private ArrayList<Equation> equations;
    private Toolbar myToolBar;
    private listAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    @Override
    protected void onResume() {
        super.onResume();

        if(recyclerView != null && layoutManager != null) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        equations = new ArrayList<>();

        SharedPreferences.Editor x = getSharedPreferences(EQUATIONLIST, MODE_PRIVATE).edit();
        x.clear();

        sharedPreference = getSharedPreferences(EQUATIONLIST, MODE_PRIVATE);
        String tmp = sharedPreference.getString(EQUATIONLIST, null);


        Log.d("test2", tmp);

        if(tmp != null){

            Gson gson = new Gson();
            equations = gson.fromJson(tmp, ArrayContainer.class).getList();



        }
        Log.d("test2", equations.toString());

        //Testing
        //addALotofItems();



        this.setContentView(R.layout.activity_equations);
        myToolBar = findViewById(R.id.my_toolbar);
        recyclerView = findViewById(R.id.list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new listAdapter(this, equations);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);


        this.setSupportActionBar(myToolBar);

        if(equations == null || equations.isEmpty()) {
            TextView emptyText = findViewById(R.id.empty);
            emptyText.setVisibility(View.VISIBLE);

        }


    }

    public void launchCalculator(){
        Intent tmp = new Intent(this, FormulaViewCalculator.class);
        this.startActivity(tmp);


    }


    /**
     * Test method
     *
     * TODO: Remove this
     */
    private void addALotofItems(){

        ArrayList x = new ArrayList();

        x.add(new Number(new BigDecimal(1)));
        for(int i =0; i< 1000; i++) {
            equations.add(new Equation(x));
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem tmp = menu.findItem(R.id.addItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getTitle().toString()) {
            case "Calculator":
                Intent ca = new Intent(this, Calculator.class);
                this.startActivity(ca);
                return true;
            case "Graph":
                Intent gr = new Intent(this, GraphView.class);
                this.startActivity(gr);
                return true;
            case "Formulas":
                return true;
            case "Add":
                launchCalculator();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
