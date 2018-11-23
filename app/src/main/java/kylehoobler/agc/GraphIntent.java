package kylehoobler.agc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * This is the class that handles graphing
 */
public class GraphIntent extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GraphAdapter adapter;
    private ArrayList<Equation> equations;
    protected SharedPreferences sharedPreference;
    public final String GRAPHLIST = "GraphEquations";
    protected GraphView graph;

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


        graph = (GraphView)findViewById(R.id.graph);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(-50);
        graph.getViewport().setMaxX(50);



        recyclerView = findViewById(R.id.graphList);
        equations = new ArrayList<Equation>();

        ArrayList<String> items = new ArrayList<>();


        sharedPreference = getSharedPreferences(GRAPHLIST, MODE_PRIVATE);
        String tmp = sharedPreference.getString(GRAPHLIST, null);

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


        adapter = new GraphAdapter(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);

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
                Intent fv = new Intent(this, FormulaIntent.class);
                this.startActivity(fv);
                return true;
            case "Add":
                Intent calc = new Intent(this, GraphViewCalculator.class);
                this.startActivity(calc);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class GraphAdapter extends RecyclerView.Adapter<GraphHolder>{


        private LayoutInflater mInflater;
        public Context context;


        public GraphAdapter(Context context){
            mInflater = LayoutInflater.from(context);
            this.context = context;
        }


        @NonNull
        @Override
        public GraphHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.graph_recycler_row, viewGroup, false);
            return new GraphHolder(view, this.context, this);
        }

        @Override
        public void onBindViewHolder(@NonNull final GraphHolder graphHolder, final int i) {
            graphHolder.textView.setText(graphHolder.getItem(i).getDisplayItem());
            graphHolder.colorItem.setBackgroundColor(graphHolder.getColor());
            graphHolder.enabled.setChecked(false);

            graphHolder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu;
                    popupMenu = new PopupMenu(context, graphHolder.options);
                    popupMenu.inflate(R.menu.formula_dropdown);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch (menuItem.getItemId()){
                                case R.id.Delete:

                                    equations.remove(i);

                                    notifyDataSetChanged();

                                    graph.removeAllSeries();



                                    SharedPreferences.Editor prefs = context.getSharedPreferences(GRAPHLIST, MODE_PRIVATE).edit();
                                    Gson conv = new Gson();

                                    ArrayList<String> vals = new ArrayList<>();

                                    for(int i = 0; i < equations.size(); i++){
                                        vals.add(new SaveBuilder().convertToString(equations.get(i)));
                                    }

                                    String val = conv.toJson(vals, ArrayList.class);

                                    prefs.clear();
                                    prefs.putString(GRAPHLIST, val);
                                    prefs.apply();


                                    return true;

                            }

                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });


        }



        @Override
        public int getItemCount() {
            return equations.size();
        }



    }

    public class GraphHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageButton options;
        private CheckBox enabled;
        public LineGraphSeries series;
        private ImageView colorItem;
        private Context context;
        private GraphAdapter curr;
        private int color;


        public GraphHolder(@NonNull View itemView, final Context context, GraphAdapter item) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.equation_row);
            this.options = itemView.findViewById(R.id.rowOptions);
            this.enabled= itemView.findViewById(R.id.checkbox);
            this.colorItem = itemView.findViewById(R.id.color);
            curr = item;
            this.color = 0;

            this.context = context;


            enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    plotEQ();
                }
            });


        }

        public int getColor(){
            return color;
        }


        protected Equation getItem(int i){

            try {
                return (Equation)equations.get(i);
            }
            catch(Exception e){

            }
            return new Equation();
        }

        protected void removeEQ(){
            colorItem.setBackgroundColor(Color.TRANSPARENT);
            graph.removeSeries(series);
        }

        protected void plotEQ(){


                    if(enabled.isChecked()) {
                        DataPoint[] values = new DataPoint[100];


                            for (int i = -50; i < values.length-50; i++) {

                                Equation tmp = equations.get(getAdapterPosition()).clone();
                                tmp.replaceAllVariables(i);

                                try {
                                    tmp.solve();

                                    values[i + 50] = new DataPoint(i, ((Number) tmp.get(0)).getValue().doubleValue());

                                }
                                catch (Exception e){
                                   values[i+50] = new DataPoint(i, 0);
                                }

                        }

                        series = new LineGraphSeries(values);

                        Random rnd = new Random();
                         this.color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));


                        series.setColor(color);
                        colorItem.setBackgroundColor(color);

                        graph.addSeries(series);


                    }
                    else{
                        removeEQ();

                    }
            }
    }


}
