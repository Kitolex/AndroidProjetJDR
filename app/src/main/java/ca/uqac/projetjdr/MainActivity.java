package ca.uqac.projetjdr;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import ca.uqac.projetjdr.util.XMLUtil;


public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void BoutonCreer(View view) {
        Intent intent = new Intent(MainActivity.this, CreationActivite.class);
        startActivity(intent);
    }

    public void BoutonModifier(View view) {
        Intent intent = new Intent(MainActivity.this, ModifierActivite.class);
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

    public void readXML(View v){
        try {
            XMLUtil xml = new XMLUtil((getAssets().open("Test.xml")));
            System.out.println(xml.lireXML().toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }





}
