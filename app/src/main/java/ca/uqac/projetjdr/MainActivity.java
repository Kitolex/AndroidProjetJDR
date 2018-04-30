package ca.uqac.projetjdr;

import android.app.Activity;
import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


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

    public void BoutonMesFiches(View view) {
        Intent intent = new Intent(MainActivity.this, ListeFichesActivity.class);
        startActivity(intent);
    }

    public void BoutonLancerDes(View view) {
        Intent intent = new Intent(MainActivity.this, LancerDesActivity.class);
        startActivity(intent);
    }
}
