package com.example.saypresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saypresent.controller.OrganizerController;
import com.example.saypresent.database.Database;
import com.example.saypresent.model.Organizer;
import com.example.saypresent.utils.GetOrganizerInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.view.View;
import android.widget.Button;

/**
 * Dashboard Activity
 */
public class Dashboard extends AppCompatActivity {
    private String organizer_key;
    private Organizer organizerAttribute;
    private OrganizerController organizerController = new OrganizerController();
    private TextView dashboard_name;
    ImageView addEvent;
    ImageView eventView;
    private GetOrganizerInterface getOrganizerInterface;
    private Database database = new Database();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dashboard_name = (TextView) findViewById(R.id.dashboardFN);

        Intent intent = getIntent();
        this.organizer_key = intent.getStringExtra("organizer_key");

        eventView = findViewById(R.id.viewEvent);
        eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, EventActivity.class);
                intent.putExtra("organizer_key", organizer_key);
                startActivity(intent);
            }
        });
        addEvent = (ImageView) findViewById(R.id.addEvent);
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
}

