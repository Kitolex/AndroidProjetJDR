package ca.uqac.projetjdr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

        ArrayList<String> listeFichier = new ArrayList<>();

        try {
            String[] ss = getAssets().list("");

            for(String s : ss){
                if(s.matches(".+[.]xml$")){
                    listeFichier.add(s.substring(0, s.length() - 4));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeFichier);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();

                try {
                    XMLUtil xml = new XMLUtil((getAssets().open(selectedItem + ".xml")));
                    NoeudXML res = xml.lireXML();
                    LinearLayout mylayout = findViewById(R.id.affichage);
                    mylayout.removeAllViews();
                    CreerAffichage(res, mylayout, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }


    public void CreerAffichage(NoeudXML res, LinearLayout l, int number) {
        System.out.println(res.getNom());
        if (res.haveSousElement()) {
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setId(15+1);
            layout.setOrientation(LinearLayout.VERTICAL);
            TextView textview = new TextView(getApplicationContext());
            textview.setText(res.getNom());
            textview.setTag(res.getNom()+"_"+number);
            //textview.setTag(number, res.getNom());
            layout.addView(textview);
            l.addView(layout);
            ArrayList<NoeudXML> temp = res.getListNoeud();
            for (NoeudXML test : temp) {
                CreerAffichage(test, layout, number+1);
            }
        }else{
            TextView textview = new TextView(getApplicationContext());
            textview.setText(res.getNom());
            //textview.setTag(number, res.getNom());
            textview.setTag(res.getNom()+"_"+number);
            EditText edittext = new EditText(getApplicationContext());
            //edittext.setTag(number, res.getNom());
            edittext.setTag(res.getNom()+"_"+number);
            l.addView(textview);
            l.addView(edittext);
        }

    }


}
