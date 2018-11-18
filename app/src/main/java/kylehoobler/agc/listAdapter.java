package kylehoobler.agc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

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



    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageButton options;
        protected final String EQUATIONLIST = "equations";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.equation_row);
            options = itemView.findViewById(R.id.rowOptions);



            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mData.get(getAdapterPosition()).numVars(0) != 0)
                        makeVarDialogBox();
                    else{
                        Equation vTemp = mData.get(getAdapterPosition()).clone();
                        displayAnswer(vTemp);
                    }
                }
            });

            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, options);
                    popupMenu.inflate(R.menu.formula_dropdown);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch (menuItem.getItemId()){
                                case R.id.Delete:
                                    mData.remove(getAdapterPosition());
                                    notifyDataSetChanged();


                                    SharedPreferences.Editor prefs = context.getSharedPreferences(EQUATIONLIST, MODE_PRIVATE).edit();
                                    Gson conv = new Gson();

                                    ArrayList<String> vals = new ArrayList<>();

                                    for(int i = 0; i < mData.size(); i++){
                                        vals.add(new SaveBuilder().convertToString(mData.get(i)));
                                    }

                                    String val = conv.toJson(vals, ArrayList.class);

                                    prefs.clear();
                                    prefs.putString(EQUATIONLIST, val);
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


        protected void makeVarDialogBox(){

            variableInput = new ArrayList<>();

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);


            for(int i = 0; i < mData.get(getAdapterPosition()).numVars(0); i++){

                variableInput.add(new EditText(context));

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


            final AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Enter Variables").setView(layout).show();


            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Equation vTemp = mData.get(getAdapterPosition()).clone();

                        for (EditText e : variableInput) {
                            Number num = new Number(1.0);

                            if (!e.getText().toString().equals(""))
                                num = new Number(new BigDecimal(e.getText().toString()));

                            vTemp.replaceVariable(num);
                        }



                        displayAnswer(vTemp);
                        dialog.cancel();
                        variableInput.clear();

                }
            });


        }

        protected void displayAnswer(Equation e){
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            try {
                e.solve();

                final AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Answer").setMessage(((Number) (e).get(0)).getValue().doubleValue() + "").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();

                variableInput.clear();

                TextView text = (TextView) dialog.findViewById(android.R.id.message);
                text.setTextSize(40);

            }
            catch (Exception m){
                final AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Error").setMessage("An error has occured.").setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
            }



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

