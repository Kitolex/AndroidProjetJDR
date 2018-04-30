package ca.uqac.projetjdr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Attr;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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

    private String state = Environment.getExternalStorageState();
    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_activity);

        Button button = (Button)findViewById(R.id.Creer);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreerFiche(view);
            }
        });

        Uri selectedUri = Uri.parse(Environment.DIRECTORY_DOWNLOADS);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setDataAndType(selectedUri,"text/xml");

        startActivityForResult(intent, READ_REQUEST_CODE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i("LOG_JDR", "Uri: " + uri.toString());
                try {
                    XMLUtil xml = new XMLUtil(getContentResolver().openInputStream(uri));
                    NoeudXML res = xml.lireXML();
                    LinearLayout mylayout = findViewById(R.id.affichage);
                    mylayout.removeAllViews();
                    CreerAffichage(res, mylayout, "", 1);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isMediaAvailable() {

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }
    }

    public void CreerAffichage(NoeudXML res, LinearLayout l, String baseTag, int number) {
        System.out.println(res.getNom());
        int compteur;
        if (res.haveSousElement()) {
            if(!res.getNom().equals("fichePersoType") && !res.getNom().equals("Attributs")){
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setId(15+1);
                layout.setOrientation(LinearLayout.VERTICAL);
                TextView textview = new TextView(getApplicationContext());
                textview.setText(res.getNom());
                textview.setTag(baseTag + "attribName_"+number); 
                textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                textview.setTypeface(null, Typeface.BOLD);
                layout.addView(textview);
                l.addView(layout);
                ArrayList<NoeudXML> temp = res.getListNoeud();
                compteur = 1; 
                for (NoeudXML test : temp) {
                    CreerAffichage(test, layout, baseTag + "attribName_"+number, compteur); 
					compteur++; 
                }
            } else {
                ArrayList<NoeudXML> temp = res.getListNoeud();
				compteur = 1;
                for (NoeudXML test : temp) {
                     CreerAffichage(test, l, "", compteur);
					compteur++;
                }
            }

        }else{
            TextView textview = new TextView(getApplicationContext());
            textview.setText(res.getNom());
            textview.setTag(baseTag + "attribName_"+number); 
            EditText edittext = new EditText(getApplicationContext());
            edittext.setTag(baseTag + "attribValue_"+number); 
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
		
		 EditText editNomFichePerso = (EditText)findViewById(R.id.editNomFichePerso);
 
        try{
            fiche = new FichePersonnage(editNomFichePerso.getText().toString());
 
            fiche.listeAttributs = getAttribs("");
 
            db.createFiche(fiche);
            Log.i("JDR_LOG", fiche.toStringDetails());
			Toast.makeText(getApplicationContext(), "fiche " + fiche.getNomPersonnage() + " créée", Toast.LENGTH_SHORT).show();
        } catch(ValeurImpossibleException e) {
            e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Echec lors de la création de la fiche", Toast.LENGTH_SHORT).show();
        }
 
        db.close();
		this.finish();
    }

    public List<Attribut> getAttribs(String baseTag){
         ArrayList<View> listName, listAttribut;
        List<Attribut> retour = new ArrayList<Attribut>();
        LinearLayout mylayout = findViewById(R.id.affichage);
 
        int count = 1;
 
        listName = getViewsByTag(mylayout, baseTag + "attribName_" + count);
        listAttribut = getViewsByTag(mylayout, baseTag + "attribValue_" + count);

        try{
            while(listName.size() != 0  ){
                Attribut attr;
                EditText edit = null;
                TextView text = (TextView) listName.get(0);

                if( listAttribut.size() != 0)
                    edit = (EditText) listAttribut.get(0);
                if(listAttribut.size() != 0) {
                    attr = new Attribut(text.getText().toString(), edit.getText().toString());
                }else{
                    attr =new Attribut(text.getText().toString(), "none");
                }
                attr.setListeSousAttributs(getAttribs(baseTag + "attribName_" + count));
                retour.add(attr);

                count ++;
                listName = getViewsByTag(mylayout, baseTag + "attribName_" + count);
                listAttribut = getViewsByTag(mylayout, baseTag + "attribValue_" + count);
            }
        } catch(ValeurImpossibleException e) {
            e.printStackTrace();            
        }

        return retour;
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
