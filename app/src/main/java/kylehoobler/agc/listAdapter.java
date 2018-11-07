package kylehoobler.agc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder>{

    private ArrayList<Equation> mData;
    private LayoutInflater mInflater;

    public listAdapter(Context context, ArrayList<Equation> data){
        mInflater = LayoutInflater.from(context);
        mData = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recycler_row, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.textView.setText(holder.getItem(i).getDisplayItem());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textView;
        private ImageButton options;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.equation_row);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        protected Equation getItem(int i){

            try {
                return (Equation)mData.get(i);
            }
            catch(Exception e){
                Log.d("Error", mData.get(i)+" " + e.getMessage());
            }
            return new Equation();
        }
    }

}

