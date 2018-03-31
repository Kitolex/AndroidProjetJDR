package ca.uqac.projetjdr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by guillaume on 25/03/2018.
 */

public class CreationActivite extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_activity);


        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                if(selectedItem.equals("Fiche1"))
                {
                    LinearLayout mylayout = findViewById(R.id.affichage);
                    mylayout.removeAllViews();
                    TextView test = new TextView(getApplicationContext());
                    test.setText("ceci est la fiche numéro 1");
                    mylayout.addView(test);
                }

                if(selectedItem.equals("Fiche2 States"))
                {
                    LinearLayout mylayout = findViewById(R.id.affichage);
                    mylayout.removeAllViews();
                    TextView test = new TextView(getApplicationContext());
                    test.setText("ceci est la fiche numéro 2");
                    mylayout.addView(test);
                    Button button = new Button(getApplicationContext());
                    button.setText("J ai réussi a faire le truc que je voulais");
                    mylayout.addView(button);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }






}
