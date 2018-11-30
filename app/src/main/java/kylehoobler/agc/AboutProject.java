package kylehoobler.agc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This is a class that is dedicated to the information to this project
 */
public class AboutProject extends AppCompatActivity {

    private int buttonPress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_xml);

        Toolbar myToolBar = findViewById(R.id.aboutBar);
        setSupportActionBar(myToolBar);

        ImageView button = findViewById(R.id.Iconic);
        buttonPress = 0;

        //Just a fun thing :)
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonPress <= 5)
                    buttonPress++;
                else {
                    Toast.makeText(getApplicationContext(), "Thank you for downloading my app :)", Toast.LENGTH_LONG).show();
                    buttonPress = 0;
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup_menu, menu);
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
                Intent fr = new Intent(this, FormulaIntent.class);
                this.startActivity(fr);
            case "About":
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
