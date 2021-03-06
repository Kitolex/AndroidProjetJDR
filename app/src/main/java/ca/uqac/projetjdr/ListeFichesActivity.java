package ca.uqac.projetjdr;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import ca.uqac.projetjdr.jdr.FichePersonnage;

public class ListeFichesActivity extends ListActivity {

    public static final String EXTRA_ID_FICHE = "idFiche";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_fiches);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        List<FichePersonnage> listeFiches = db.getAllFiches();

        setListAdapter(new ArrayAdapter<FichePersonnage>(this, R.layout.liste_fiche_item, listeFiches));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        FichePersonnage fiche = (FichePersonnage)l.getItemAtPosition(position);

        Log.i("JDR_LOG", Integer.toString(fiche.getId()));

        Intent i = new Intent(this, AffichageActivite.class);

        i.putExtra(EXTRA_ID_FICHE, fiche.getId());

        startActivity(i);
    }
}
