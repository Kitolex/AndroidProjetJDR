package ca.uqac.projetjdr;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

public class LancerDesActivity extends AppCompatActivity implements SensorEventListener {

    public static final int MAX_SIZE = 120;
    public static final int MIN_SIZE = 80;
    private ImageButton buttonDesSelectionne;
    private int valeurDesSelectionne;
    private Button buttonLancerDeSelectionne;
    private TextView resultatTextView;
    private TextView sommeTextView;
    private int actualSize;
    private static final int dureeTotaleAnimation = 100;
    private static final int dureeParTaille = dureeTotaleAnimation / (2*(MAX_SIZE - MIN_SIZE));

    private final Handler handler = new Handler();

    private final Runnable runnableSizeUp = new Runnable() {
        @Override
        public void run() {
            updateSizeUp();
        }
    };

    private final Runnable runnableSizeDown = new Runnable() {
        @Override
        public void run() {
            updateSizeDown();
        }
    };

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float speed;
    private boolean shakeReady;
    private static final float UPPER_SPEED_THRESHOLD = 35.0f;
    private static final float LOWER_SPEED_THRESHOLD = 20.0f;

    private boolean rolling;

    private int nbDesALancer;
    private int ajoutFixeAuResultat;

    private TextView nbDesTextView;
    private TextView ajoutFixeTextView;
    private Button buttonNbDesDown;
    private Button buttonNbDesUp;
    private Button buttonAjoutFixeDown;
    private Button buttonAjoutFixeUp;

    private String historiqueString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancer_des);

        buttonLancerDeSelectionne = (Button)findViewById(R.id.lancerDes_lancerDesButton);

        resultatTextView = (TextView)findViewById(R.id.lancerDes_resultatTextView);

        sommeTextView = (TextView)findViewById(R.id.lancerDes_sommeTextView);

        actualSize = MIN_SIZE;

        resultatTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        valeurDesSelectionne = 0;

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        speed = 0.0f;
        shakeReady = true;
        rolling = false;

        nbDesALancer = 1;
        ajoutFixeAuResultat = 0;

        nbDesTextView = (TextView)findViewById(R.id.lancerDes_nbDesTextView);
        ajoutFixeTextView = (TextView)findViewById(R.id.lancerDes_ajoutFixeTextView);

        nbDesTextView.setText("");
        ajoutFixeTextView.setText("");

        nbDesTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        ajoutFixeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

        buttonNbDesDown = (Button)findViewById(R.id.lancerDes_nbDesDown);
        buttonNbDesUp = (Button)findViewById(R.id.lancerDes_nbDesUp);
        buttonAjoutFixeDown = (Button)findViewById(R.id.lancerDes_ajoutFixeDown);
        buttonAjoutFixeUp = (Button)findViewById(R.id.lancerDes_ajoutFixeUp);

        buttonNbDesDown.setEnabled(false);
        buttonNbDesUp.setEnabled(false);
        buttonAjoutFixeDown.setEnabled(false);
        buttonAjoutFixeUp.setEnabled(false);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        speed = Math.abs(event.values[0]) + Math.abs(event.values[1]) + Math.abs(event.values[2]);

        if(shakeReady && speed >= UPPER_SPEED_THRESHOLD){
            shakeReady = false;
            rollDices();
        }

        if(!shakeReady && speed < LOWER_SPEED_THRESHOLD){
            shakeReady = true;
        }
    }

    public void lancerDeSelectionne(View v){

        rollDices();
    }

    private void rollDices(){
        if(!rolling && valeurDesSelectionne != 0){

            rolling = true;

            String resultatDeString = "";
            String somme = "";

            if(nbDesALancer == 1){

                int val = calculerValeurDes();

                resultatDeString = Integer.toString(val + ajoutFixeAuResultat);
                if(ajoutFixeAuResultat != 0){
                    somme = Integer.toString(val) + " + " + Integer.toString(ajoutFixeAuResultat);
                }
            }
            else {

                somme = "[";
                int valTotal = ajoutFixeAuResultat;

                for(int i=1; i<=nbDesALancer; i++){

                    int val = calculerValeurDes();
                    valTotal += val;

                    somme += Integer.toString(val);

                    if(i != nbDesALancer){
                        somme += " + ";
                    } else {
                        somme += "]";
                    }
                }

                if(ajoutFixeAuResultat != 0){
                    somme += " + " + Integer.toString(ajoutFixeAuResultat);
                }

                resultatDeString = Integer.toString(valTotal);
            }

            resultatTextView.setText(resultatDeString);
            sommeTextView.setText(somme);

            String nouveauLancer = resultatDeString;
            if(!somme.equals("")){
                nouveauLancer += " (" + somme + ")";
            }
            nouveauLancer += "\n" + makeTextNbDesTextView() + makeTextAjoutFixeTextView() + "\n\n";

            historiqueString = nouveauLancer + historiqueString;

            updateSizeUp();
        }
    }

    private int calculerValeurDes(){
        Random rand = new Random();

        return rand.nextInt(valeurDesSelectionne) + 1;
    }

    private void updateSizeUp(){

        actualSize++;

        resultatTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, actualSize);

        if(actualSize >= MAX_SIZE){
            handler.postDelayed(runnableSizeDown, dureeParTaille);
        } else {
            handler.postDelayed(runnableSizeUp, dureeParTaille);
        }
    }

    private void updateSizeDown(){

        actualSize--;

        resultatTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, actualSize);

        if(actualSize > MIN_SIZE){
            handler.postDelayed(runnableSizeDown, dureeParTaille);
        }
        else{
            rolling = false;
        }
    }

    private void updateSelectedDes(ImageButton buttonNewSelectedDice){

        if(buttonDesSelectionne != null){
            buttonDesSelectionne.setBackgroundColor(getResources().getColor(R.color.desClassique));
        }

        buttonDesSelectionne = buttonNewSelectedDice;

        buttonDesSelectionne.setBackgroundColor(getResources().getColor(R.color.desSelectionne));

        buttonLancerDeSelectionne.setEnabled(true);

        buttonNbDesDown.setEnabled(true);
        buttonNbDesUp.setEnabled(true);
        buttonAjoutFixeDown.setEnabled(true);
        buttonAjoutFixeUp.setEnabled(true);

        updateNbDesTextView();
        updateAjoutFixeTextView();

        resultatTextView.setText(R.string.appuyer_ou_secouer);
        resultatTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        sommeTextView.setText("");
    }

    public void retour(View v){
        finish();
    }

    public void buttonDes4(View v){
        valeurDesSelectionne = 4;
        updateSelectedDes((ImageButton)v);
    }

    public void buttonDes6(View v){
        valeurDesSelectionne = 6;
        updateSelectedDes((ImageButton)v);
    }

    public void buttonDes8(View v){
        valeurDesSelectionne = 8;
        updateSelectedDes((ImageButton)v);
    }

    public void buttonDes10(View v){
        valeurDesSelectionne = 10;
        updateSelectedDes((ImageButton)v);
    }

    public void buttonDes12(View v){
        valeurDesSelectionne = 12;
        updateSelectedDes((ImageButton)v);
    }

    public void buttonDes20(View v){
        valeurDesSelectionne = 20;
        updateSelectedDes((ImageButton)v);
    }

    public void buttonDes100(View v){
        valeurDesSelectionne = 100;
        updateSelectedDes((ImageButton)v);
    }

    public void nbDesMoins1(View v){
        nbDesALancer = Math.max(1, nbDesALancer - 1);
        updateNbDesTextView();
    }

    public void nbDesPlus1(View v){
        nbDesALancer++;
        updateNbDesTextView();
    }

    public void ajoutFixeMoins1(View v){
        ajoutFixeAuResultat--;
        updateAjoutFixeTextView();
    }

    public void ajoutFixePlus1(View v){
        ajoutFixeAuResultat++;
        updateAjoutFixeTextView();
    }

    private void updateNbDesTextView(){
        nbDesTextView.setText(makeTextNbDesTextView());
    }

    private void updateAjoutFixeTextView(){
        ajoutFixeTextView.setText(makeTextAjoutFixeTextView());
    }

    private String makeTextNbDesTextView(){
        return Integer.toString(nbDesALancer) + "D" + Integer.toString(valeurDesSelectionne);
    }

    private String makeTextAjoutFixeTextView(){
        if(ajoutFixeAuResultat < 0){
            return Integer.toString(ajoutFixeAuResultat);
        } else {
            return "+" + Integer.toString(ajoutFixeAuResultat);
        }
    }

    public void historique(View view) {

        Intent i = new Intent(this, HistoriqueDesActivity.class);

        i.putExtra("historique", historiqueString);

        startActivity(i);
    }
}
