package ca.uqac.projetjdr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import ca.uqac.projetjdr.util.NoeudXML;
import ca.uqac.projetjdr.util.XMLUtil;

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
                if (selectedItem.equals("Fiche1")) {
                    try {
                        XMLUtil xml = new XMLUtil((getAssets().open("Test.xml")));
                        NoeudXML res = xml.lireXML();
                        LinearLayout mylayout = findViewById(R.id.affichage);
                        mylayout.removeAllViews();
                        CreerAffichage(res, mylayout);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }


                }

                if (selectedItem.equals("Fiche2 States")) {
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


    public void CreerAffichage(NoeudXML res, LinearLayout l) {
        System.out.println(res.getNom());
        if (res.haveSousElement()) {
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setId(15+1);
            layout.setOrientation(LinearLayout.VERTICAL);
            TextView textview = new TextView(getApplicationContext());
            textview.setText(res.getNom());
            layout.addView(textview);
            l.addView(layout);
            ArrayList<NoeudXML> temp = res.getListNoeud();
            for (NoeudXML test : temp) {
                CreerAffichage(test, layout);
            }
        }else{
            TextView textview = new TextView(getApplicationContext());
            textview.setText(res.getNom());
            EditText edittext = new EditText(getApplicationContext());
            l.addView(textview);
            l.addView(edittext);
        }

    }


}
