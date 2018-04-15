package ca.uqac.projetjdr;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ca.uqac.projetjdr.jdr.Attribut;
import ca.uqac.projetjdr.jdr.FichePersonnage;

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
            l.addView(textview);
            l.addView(edittext);
            number++;
        }

    }
}
