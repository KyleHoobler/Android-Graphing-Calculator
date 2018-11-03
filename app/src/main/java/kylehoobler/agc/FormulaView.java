package kylehoobler.agc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FormulaView extends AppCompatActivity {

    private ArrayList<String> tmp;
    private Toolbar myToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_equations);
        myToolBar = findViewById(R.id.my_toolbar);


        ListView listView = findViewById(R.id.ListView);
        this.setSupportActionBar(myToolBar);

        tmp = new ArrayList<>();

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, tmp);
        listView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


        if(tmp == null || tmp.isEmpty()) {
            TextView emptyText = findViewById(R.id.empty);
            emptyText.setVisibility(View.VISIBLE);
            listView.setEmptyView(emptyText);
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
