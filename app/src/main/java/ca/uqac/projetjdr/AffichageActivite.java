package ca.uqac.projetjdr;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ca.uqac.projetjdr.jdr.Attribut;
import ca.uqac.projetjdr.jdr.FichePersonnage;

import static ca.uqac.projetjdr.ListeFichesActivity.EXTRA_ID_FICHE;

/**
 * Created by guillaume on 25/03/2018.
 */

public class AffichageActivite extends AppCompatActivity {

    public int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affichage_activity);

        number = 1;



        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        LinearLayout mylayout = findViewById(R.id.affichage);
        mylayout.removeAllViews();
        Intent intent = getIntent();

        int id = intent.getIntExtra(EXTRA_ID_FICHE, -1);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        FichePersonnage fiche = db.getFiche(id);

        Log.i("JDR_LOG", fiche.toStringDetails());


        CreerAffichage(fiche);
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
            textview.setText("\t\t\t\t" + attribut.getNom() + " : " + attribut.getValeur());
            textview.setTag("attribName_"+number);
            textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
//            TextView textviewValeur = new TextView(getApplicationContext());
//            textviewValeur.setTag("attribValue_"+number);
//            textviewValeur.setText();
            l.addView(textview);
//            l.addView(textviewValeur);
            number++;
        }

    }

    public void BoutonLancerDes(View view) {
        Intent intent = new Intent(AffichageActivite.this, LancerDesActivity.class);
        startActivity(intent);
    }

    public void BoutonModifier(View view) {
        Intent intent = getIntent();

        int id = intent.getIntExtra(EXTRA_ID_FICHE, -1);

        Intent i = new Intent(AffichageActivite.this, ModifierActivite.class);
        i.putExtra(EXTRA_ID_FICHE, id);

        startActivity(i);
    }
}
