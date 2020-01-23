package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.saypresent.controller.OrganizerController;
import com.example.saypresent.model.Organizer;
import com.example.saypresent.utils.GetOrganizerInterface;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {
    private String organizer_key;
    private Organizer organizerAttribute;
    private OrganizerController organizerController;
    private TextView dashboard_name;
    Button addEvent;
    Button eventView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        this.organizer_key = intent.getStringExtra("organizer_key");
        setOrganizer(organizer_key);

        eventView = findViewById(R.id.viewEvent);
        eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this,Event.class);
                startActivity(intent);
            }
        });
        addEvent = findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Dashboard.this,addEvent.class);
                startActivity(intent);
            }
        });
    }

    private void setOrganizer(String organizer_key){
        dashboard_name = (TextView) findViewById(R.id.dashboardFN);
        GetOrganizerInterface getOrganizerInterface = new GetOrganizerInterface() {
            @Override
            public void onGetOrganizer(Organizer organizer) {
                organizerAttribute = organizer;
                dashboard_name.setText(organizer.getFirst_name());
            }
        };
//        organizerController = new OrganizerController();
//        organizerController.getOrganizer(organizer_key, getOrganizerInterface);
    }
}
