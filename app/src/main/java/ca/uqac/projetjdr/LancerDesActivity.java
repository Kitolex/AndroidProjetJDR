package ca.uqac.projetjdr;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancer_des);

        buttonLancerDeSelectionne = (Button)findViewById(R.id.lancerDesButton);

        resultatTextView = (TextView)findViewById(R.id.resultatTextView);

        actualSize = MIN_SIZE;

        resultatTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        valeurDesSelectionne = 0;

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        speed = 0.0f;
        shakeReady = true;
        rolling = false;

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
            rollDice();
        }

        if(!shakeReady && speed < LOWER_SPEED_THRESHOLD){
            shakeReady = true;
        }
    }

    public void lancerDeSelectionne(View v){

        rollDice();
    }

    private void rollDice(){
        if(!rolling && valeurDesSelectionne != 0){
            rolling = true;
            Random rand = new Random();

            String resultatDe = Integer.toString(rand.nextInt(valeurDesSelectionne) + 1);

            resultatTextView.setText(resultatDe);

            updateSizeUp();
        }
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

        resultatTextView.setText(R.string.appuyer_ou_secouer);
        resultatTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
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
