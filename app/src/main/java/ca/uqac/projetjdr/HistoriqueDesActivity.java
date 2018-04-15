package ca.uqac.projetjdr;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HistoriqueDesActivity extends AppCompatActivity {

    private TextView historiqueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_des);

        Intent intent = getIntent();

        historiqueTextView = (TextView)findViewById(R.id.historiqueDes_historiqueTextView);
        historiqueTextView.setText(intent.getStringExtra(LancerDesActivity.EXTRA_HISTORIQUE));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void retour(View view) {
        finish();
    }
}
