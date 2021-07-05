package com.project.lepilulier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.project.lepilulier.fragments.SoirFragment;
import com.project.lepilulier.fragments.JournalDeBordFragment;
import com.project.lepilulier.fragments.MedicamentMomentFragment;
import com.project.lepilulier.fragments.MatinFragment;
import com.project.lepilulier.fragments.MidiFragment;
import com.project.lepilulier.fragments.OrdonnanceFragment;


public class FragmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout tiroir;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);

        Toolbar barre_outils = findViewById(R.id.barre_outils);
        setSupportActionBar(barre_outils);

        getSupportActionBar().setTitle("");

        tiroir = findViewById(R.id.tiroirLayout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, tiroir, barre_outils,
                R.string.tiroir_ouverture, R.string.tiroir_fermeture);

        tiroir.addDrawerListener(toggle);
        toggle.syncState();

        barre_outils.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tiroir.isDrawerOpen(GravityCompat.END)) {
                    tiroir.closeDrawer(GravityCompat.END);
                } else {
                    tiroir.openDrawer(GravityCompat.END);
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        String which = bundle.getString("which");

        if(which.equals("ordonnance")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                    new OrdonnanceFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_ordonnance);
        } else if(which.equals("journalDeBord")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                    new JournalDeBordFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_journalDeBord);
        } else if(which.equals("medicamentMoment")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                    new MedicamentMomentFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_medicament);
        } else if(which.equals("matin")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                    new MatinFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_matin);
        } else if(which.equals("midi")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                    new MidiFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_midi);
        } else if(which.equals("soir")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                    new SoirFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_soir);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                finish();
                break;
            case R.id.nav_matin:
                getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                        new MatinFragment()).commit();
                break;
            case R.id.nav_midi:
                getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                        new MidiFragment()).commit();
                break;
            case R.id.nav_soir:
                getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                        new SoirFragment()).commit();
                break;
            case R.id.nav_ordonnance:
                getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                        new OrdonnanceFragment()).commit();
                break;
            case R.id.nav_journalDeBord:
                getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                        new JournalDeBordFragment()).commit();
                break;
            case R.id.nav_medicament:
                getSupportFragmentManager().beginTransaction().replace(R.id.frament_container,
                        new MedicamentMomentFragment()).commit();
                break;
        }

        tiroir.closeDrawer(GravityCompat.END);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(tiroir.isDrawerOpen(GravityCompat.END)) {
            tiroir.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}