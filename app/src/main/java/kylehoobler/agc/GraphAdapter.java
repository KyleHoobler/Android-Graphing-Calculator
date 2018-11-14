package kylehoobler.agc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.GraphHolder>{

    private ArrayList<Equation> mData;
    private LayoutInflater mInflater;
    private Context context;


    public GraphAdapter(Context context, ArrayList<Equation> data){
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.context = context;
    }


    @NonNull
    @Override
    public GraphHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.graph_recycler_row, viewGroup, false);
        return new GraphHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GraphHolder graphHolder, int i) {
        graphHolder.textView.setText(graphHolder.getItem(i).getDisplayItem());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class GraphHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageButton options;
        private CheckBox enabled;

        public GraphHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.equation_row);
            this.options = itemView.findViewById(R.id.rowOptions);
            this.enabled= itemView.findViewById(R.id.checkbox);
        }

        protected Equation getItem(int i){

            try {
                return (Equation)mData.get(i);
            }
            catch(Exception e){

            }
            return new Equation();
        }
    }

}
