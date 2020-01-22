package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.saypresent.controller.OrganizerController;
import com.example.saypresent.model.Organizer;
import com.example.saypresent.utils.GetOrganizerInterface;

public class Dashboard extends AppCompatActivity {
    private String organizer_key;
    private Organizer organizerAttribute;
    private OrganizerController organizerController;
    private TextView dashboard_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        this.organizer_key = intent.getStringExtra("organizer_key");
        setOrganizer(organizer_key);

    }

    private void setOrganizer(String organizer_key){
//        dashboard_name = (TextView) findViewById(R.id.dashboardFN);
        GetOrganizerInterface getOrganizerInterface = new GetOrganizerInterface() {
            @Override
            public void onGetOrganizer(Organizer organizer) {
                organizerAttribute = organizer;
//                dashboard_name.setText(organizer.getFirst_name());
            }
        };
        organizerController = new OrganizerController();
        organizerController.getOrganizer(organizer_key, getOrganizerInterface);
    }
}
