package kylehoobler.agc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class GraphView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GraphAdapter adapter;
    private ArrayList equations;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem tmp = menu.findItem(R.id.addItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar myToolBar = findViewById(R.id.GraphBar);
        setSupportActionBar(myToolBar);

        recyclerView = findViewById(R.id.graphList);
        equations = new ArrayList<Equation>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        Equation test = new Equation();
        addABunch();

        adapter = new GraphAdapter(this, equations);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);



    }

    private void addABunch(){
        for(int i = 0; i < 100; i++){
            equations.add(new Equation());
        }
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
                return true;
            case "Formulas":
                Intent fv = new Intent(this, FormulaView.class);
                this.startActivity(fv);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
