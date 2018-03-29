package ca.uqac.projetjdr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class LancerDesActivity extends AppCompatActivity {

    private ImageButton buttonDesSelectionne;
    private int valeurDesSelectionne;
    private Button buttonLancerDeSelectionne;
    private TextView resultatTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancer_des);

        buttonLancerDeSelectionne = (Button)findViewById(R.id.lancerDesButton);

        resultatTextView = (TextView)findViewById(R.id.resultatTextView);
    }

    public void lancerDeSelectionne(View v){

        Random rand = new Random();

        String resultatDe = Integer.toString(rand.nextInt(valeurDesSelectionne) + 1);

        resultatTextView.setText(resultatDe);
    }

    private void updateSelectedDes(ImageButton buttonNewSelectedDice){

        if(buttonDesSelectionne != null){
            buttonDesSelectionne.setBackgroundColor(getResources().getColor(R.color.desClassique));
        }

        buttonDesSelectionne = buttonNewSelectedDice;

        buttonDesSelectionne.setBackgroundColor(getResources().getColor(R.color.desSelectionne));

        buttonLancerDeSelectionne.setEnabled(true);

        resultatTextView.setText(R.string.appuyer_ou_secouer);
    }

    public void retour(View v){
        finish();
    }

    public void buttonDes4(View v){
        updateSelectedDes((ImageButton)v);

        valeurDesSelectionne = 4;
    }

    public void buttonDes6(View v){
        updateSelectedDes((ImageButton)v);

        valeurDesSelectionne = 6;
    }

    public void buttonDes8(View v){
        updateSelectedDes((ImageButton)v);

        valeurDesSelectionne = 8;
    }

    public void buttonDes12(View v){
        updateSelectedDes((ImageButton)v);

        valeurDesSelectionne = 12;
    }

    public void buttonDes20(View v){
        updateSelectedDes((ImageButton)v);

        valeurDesSelectionne = 20;
    }
}
