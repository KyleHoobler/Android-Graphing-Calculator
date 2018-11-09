package kylehoobler.agc;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder>{

    private ArrayList<Equation> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<EditText> variableInput;


    public listAdapter(Context context, ArrayList<Equation> data){
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.context = context;
        variableInput = new ArrayList<>();
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



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageButton options;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.equation_row);
            options = itemView.findViewById(R.id.rowOptions);



            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(hasVariable(mData.get(getAdapterPosition())))
                        makeVarDialogBox();
                }
            });

            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("item", mData.get(getAdapterPosition()).get(0).toString());

                }
            });

        }

        private boolean hasVariable(Equation x){
            for(int i = 0; i < x.size(); i++){
                if(x.get(i).getDisplayItem().equals("x"))
                    return true;
            }

            return false;
        }


        protected void makeVarDialogBox(){

            variableInput = new ArrayList<>();

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Enter Variables");

            for(int i = 0; i < mData.get(getAdapterPosition()).getEquation().size(); i++){

                if(mData.get(getAdapterPosition()).getEquation().get(i).getDisplayItem().equals("x")) {
                    variableInput.add(new EditText(context));

                }
            }

            int tmp = 1;
            for(EditText x : variableInput){

                x.setHint("x" + tmp);
                x.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL| InputType.TYPE_NUMBER_FLAG_SIGNED);

                layout.addView(x);
                tmp++;
            }

            Button okay = new Button(context);
            okay.setText("Solve");
            layout.addView(okay);


            dialog.setView(layout);

            dialog.show();
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

