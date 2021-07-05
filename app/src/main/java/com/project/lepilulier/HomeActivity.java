package com.project.lepilulier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bMatin, bMidi, bSoir, bJournalDeBord, bMedicament, bOrdonnance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bMatin = findViewById(R.id.bMatin);
        bMidi = findViewById(R.id.bMidi);
        bSoir = findViewById(R.id.bSoir);
        bJournalDeBord = findViewById(R.id.bJournalDeBord);
        bMedicament = findViewById(R.id.bMedicament);
        bOrdonnance = findViewById(R.id.bOrdonnances);

        bMatin.setOnClickListener(this);
        bMidi.setOnClickListener(this);
        bSoir.setOnClickListener(this);
        bJournalDeBord.setOnClickListener(this);
        bMedicament.setOnClickListener(this);
        bOrdonnance.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.bMatin:
                i = new Intent(HomeActivity.this, FragmentActivity.class);
                i.putExtra("which", "matin");
                startActivity(i);
                break;
            case R.id.bMidi:
                i = new Intent(HomeActivity.this, FragmentActivity.class);
                i.putExtra("which", "midi");
                startActivity(i);
                break;
            case R.id.bSoir:
                i = new Intent(HomeActivity.this, FragmentActivity.class);
                i.putExtra("which", "soir");
                startActivity(i);
                break;
            case R.id.bJournalDeBord:
                i = new Intent(HomeActivity.this, FragmentActivity.class);
                i.putExtra("which", "journalDeBord");
                startActivity(i);
                break;
            case R.id.bMedicament:
                i = new Intent(HomeActivity.this, FragmentActivity.class);
                i.putExtra("which", "medicamentMoment");
                startActivity(i);
                break;
            case R.id.bOrdonnances:
                i = new Intent(HomeActivity.this, FragmentActivity.class);
                i.putExtra("which", "ordonnance");
                startActivity(i);
                break;
        }
    }
}