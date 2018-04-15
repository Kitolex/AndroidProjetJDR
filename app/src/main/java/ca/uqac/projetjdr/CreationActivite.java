package ca.uqac.projetjdr;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import ca.uqac.projetjdr.jdr.Attribut;
import ca.uqac.projetjdr.jdr.FichePersonnage;
import ca.uqac.projetjdr.jdr.exception.ValeurImpossibleException;
import ca.uqac.projetjdr.util.NoeudXML;
import ca.uqac.projetjdr.util.XMLUtil;

/**
 * Created by guillaume on 25/03/2018.
 */

public class CreationActivite extends Activity {

    public int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_activity);

        number = 1;

        Button button = (Button)findViewById(R.id.Creer);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreerFiche(view);
            }
        });

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
                    CreerAffichage(res, mylayout);
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


    public void CreerAffichage(NoeudXML res, LinearLayout l) {
        System.out.println(res.getNom());
        if (res.haveSousElement()) {
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setId(15+1);
            layout.setOrientation(LinearLayout.VERTICAL);
            TextView textview = new TextView(getApplicationContext());
            textview.setText(res.getNom());
            textview.setTag("attribName_"+number);
            //textview.setTag(number, res.getNom());
            layout.addView(textview);
            l.addView(layout);
            ArrayList<NoeudXML> temp = res.getListNoeud();
            number++;
            for (NoeudXML test : temp) {
                CreerAffichage(test, layout);
            }
        }else{
            TextView textview = new TextView(getApplicationContext());
            textview.setText(res.getNom());
            //textview.setTag(number, res.getNom());
            textview.setTag("attribName_"+number);
            EditText edittext = new EditText(getApplicationContext());
            //edittext.setTag(number, res.getNom());
            edittext.setTag("attribValue_"+number);
            l.addView(textview);
            l.addView(edittext);
            number++;
        }

    }

    public void CreerFiche(View view){
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        LinearLayout mylayout = findViewById(R.id.affichage);
        ArrayList<View> listName, listAttribut;
        int count = 1;
        FichePersonnage fiche;
        EditText test;



        try{
            fiche = new FichePersonnage("Dummy");

            listName = getViewsByTag(mylayout, "attribName_" + count);
            listAttribut = getViewsByTag(mylayout, "attribValue_" + count);

            while(listName.size() != 0  ){
                EditText edit = null;
                TextView text = (TextView) listName.get(0);

                if( listAttribut.size() != 0)
                    edit = (EditText) listAttribut.get(0);
                if(listAttribut.size() != 0) {
                    fiche.listeAttributs.add(new Attribut(text.getText().toString(), edit.getText().toString()));
                }else{
                    fiche.listeAttributs.add(new Attribut(text.getText().toString(), "none"));
                }
                count ++;
                listName = getViewsByTag(mylayout, "attribName_" + count);
                listAttribut = getViewsByTag(mylayout, "attribValue_" + count);
            }

            db.createFiche(fiche);
        } catch(ValeurImpossibleException e) {
            e.printStackTrace();
        }

        db.close();
    }

    public ArrayList<View> getViewsByTag(ViewGroup root, String tag){
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }


}
