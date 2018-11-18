package kylehoobler.agc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;

public class FormulaIntent extends AppCompatActivity implements Serializable{

    protected final String EQUATIONLIST = "equations";

    protected SharedPreferences sharedPreference;

    private ArrayList<Equation> equations;
    private ArrayList<String> items;
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
        items = new ArrayList<>();


        sharedPreference = getSharedPreferences(EQUATIONLIST, MODE_PRIVATE);
        String tmp = sharedPreference.getString(EQUATIONLIST, null);

        this.setContentView(R.layout.activity_equations);
        myToolBar = findViewById(R.id.my_toolbar);
        recyclerView = findViewById(R.id.list);

        if(tmp != null){

            Gson gson = new Gson();
            items = gson.fromJson(tmp, ArrayList.class);
            for(String x : items){
                equations.add(new SaveBuilder().convertToEquation(x));
            }
        }


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
                Intent gr = new Intent(this, GraphIntent.class);
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
