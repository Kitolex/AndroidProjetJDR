package ca.uqac.projetjdr;

import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import ca.uqac.projetjdr.util.NoeudXML;
import ca.uqac.projetjdr.util.XMLUtil;


public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void BoutonCreer(View view) {
        Intent intent = new Intent(MainActivity.this, CreationActivite.class);
        startActivity(intent);
    }

    public void BoutonModifier(View view) {
        Intent intent = new Intent(MainActivity.this, ListeFichesActivity.class);
        startActivity(intent);
    }

    public void BoutonSupprimer(View view) {
        Intent intent = new Intent(MainActivity.this, SupprimerActivite.class);
        startActivity(intent);
    }

    public void BoutonImporter(View view) {
        Intent intent = new Intent(MainActivity.this, ImporterActivite.class);
        startActivity(intent);
    }

    public void BoutonLancerDes(View view) {
        Intent intent = new Intent(MainActivity.this, LancerDesActivity.class);
        startActivity(intent);
    }

    public void readXML(View v){
        try {
            XMLUtil xml = new XMLUtil((getAssets().open("Test.xml")));
            NoeudXML res = xml.lireXML();
            System.out.println(res.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

      /*  public void readXML(View v){
        try {
            XMLUtil xml = new XMLUtil((getAssets().open("Test.xml")));
            NoeudXML res = xml.lireXML();
            System.out.println(res.getNom());
            if(res.haveSousElement()){
                ArrayList<NoeudXML> temp = res.getListNoeud();
                for (NoeudXML test: temp) {
                    System.out.println(test.getNom());
                    if(res.haveSousElement()){
                        ArrayList<NoeudXML> temp2 = test.getListNoeud();
                        for (NoeudXML test2: temp2) {
                            System.out.println(test2.getNom());
                        }
                    }
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
*/





}
