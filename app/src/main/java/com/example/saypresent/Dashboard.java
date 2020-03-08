package com.example.saypresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.saypresent.controller.OrganizerController;
import com.example.saypresent.database.Database;
import com.example.saypresent.model.Organizer;
import com.example.saypresent.utils.GetOrganizerInterface;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Dashboard Activity
 */
public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String organizer_key;
    private Organizer organizerAttribute;
    private OrganizerController organizerController = new OrganizerController();
    private TextView dashboard_name;
    private Button addEvent;
    private Button eventView;
    private GetOrganizerInterface getOrganizerInterface;
    private Database database = new Database();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        dashboard_name = (TextView) findViewById(R.id.dashboardFN);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        //side menu navigation
        navigationView.bringToFront();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.dashboard);
        //
        Intent intent = getIntent();
        organizer_key = intent.getStringExtra("organizer_key");
        System.out.println(organizer_key);

        eventView = findViewById(R.id.viewEvent);
        eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, EventActivity.class);
                intent.putExtra("organizer_key", organizer_key);
                startActivity(intent);
            }
        });
        addEvent = (Button) findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Dashboard.this,addEvent.class);
                intent.putExtra("organizer_key", organizer_key);
                startActivity(intent);
            }
        });
        setName(this.organizer_key);
    }
    //close the navigation menu
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        this.organizer_key = intent.getStringExtra("organizer_key");
        System.out.println("onResume " + organizer_key);
    }

    private void setName(final String organizer_key){
        organizerController.getOrganizer(organizer_key, new GetOrganizerInterface() {
            @Override
            public void onCallback(Organizer organizer) {
                if(organizer != null){
                    String name = organizer.getFirst_name() + " " + organizer.getLast_name();
                    dashboard_name.setText(name);
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.dashboard:
                break;
            case R.id.addEvent:
                Intent intent = new Intent (Dashboard.this,addEvent.class);
                intent.putExtra("organizer_key", organizer_key);
                drawerLayout.closeDrawers();
                startActivity(intent);
                break;
            case R.id.viewEvent:
                Intent newintent = new Intent (Dashboard.this,EventActivity.class);
                newintent.putExtra("organizer_key", organizer_key);
                drawerLayout.closeDrawers();
                startActivity(newintent);
                break;
        }
        return true;
    }
}

