package ca.uqac.projetjdr;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.projetjdr.jdr.Attribut;
import ca.uqac.projetjdr.jdr.FichePersonnage;
import ca.uqac.projetjdr.jdr.exception.ValeurImpossibleException;

import static ca.uqac.projetjdr.ListeFichesActivity.EXTRA_ID_FICHE;

/**
 * Created by guillaume on 25/03/2018.
 */

public class ModifierActivite extends AppCompatActivity {

    public int number;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifier_activity);

        number = 1;

        Intent intent = getIntent();

        int id = intent.getIntExtra(ListeFichesActivity.EXTRA_ID_FICHE, -1);
       Log.i("JDR_LOG", ""+ id);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        FichePersonnage fiche = db.getFiche(id);

        Log.i("JDR_LOG", fiche.toStringDetails());

        CreerAffichage(fiche);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void CreerAffichage(FichePersonnage fiche){
        TextView textNomFichePerso = (TextView)findViewById(R.id.textNomFichePerso);

        textNomFichePerso.setText(fiche.getNomPersonnage());

        for(Attribut a : fiche.getListeAttributs()){
            CreerAttribut(a, (LinearLayout)findViewById(R.id.affichage));
        }
    }

    private void CreerAttribut(Attribut attribut, LinearLayout l) {

        if (!attribut.getListeSousAttributs().isEmpty()) {
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setId(15+1);
            layout.setOrientation(LinearLayout.VERTICAL);
            TextView textview = new TextView(getApplicationContext());
            textview.setText(attribut.getNom());
            textview.setTag("attribName_"+number);
            textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            textview.setTypeface(null, Typeface.BOLD);
            layout.addView(textview);
            l.addView(layout);
            List<Attribut> listeSousAttributs = attribut.getListeSousAttributs();
            number++;
            for (Attribut a : listeSousAttributs) {
                CreerAttribut(a, layout);
            }
        }else{
            TextView textview = new TextView(getApplicationContext());
            textview.setText(attribut.getNom());
            textview.setTag("attribName_"+number);
            EditText edittext = new EditText(getApplicationContext());
            edittext.setTag("attribValue_"+number);
            edittext.setText(attribut.getValeur());
            l.addView(textview);
            l.addView(edittext);
            number++;
        }

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
                //db.updateAttribut(attr);
                retour.add(attr);

                count ++;
                listName = getViewsByTag(mylayout, baseTag + "attribName_" + count);
                listAttribut = getViewsByTag(mylayout, baseTag + "attribValue_" + count);
            }
        } catch(ValeurImpossibleException e) {
            e.printStackTrace();
        }
       // db.close();
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

    public void BoutonModifier(View view) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Attribut> list = getAttribs("");

        for (Attribut attri: list) {

            db.updateAttribut(attri);
            Log.i("JDR", attri.getId() + " " +attri.getNom() + "  " + attri.getValeur());
           Log.i("JDR", "" + db.getAttribut(attri.getId()));

        }

        db.close();
        this.finish();
    }

}
