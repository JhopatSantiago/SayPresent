package com.example.saypresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.saypresent.controller.OrganizerController;
import com.example.saypresent.database.Database;
import com.example.saypresent.model.Organizer;
import com.example.saypresent.utils.GetOrganizerInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {
    private String organizer_key;
    private Organizer organizerAttribute;
    private OrganizerController organizerController;
    private TextView dashboard_name;
    private GetOrganizerInterface getOrganizerInterface;
    private Database database = new Database();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Button addEvent;

        dashboard_name = (TextView) findViewById(R.id.dashboardFN);

        Intent intent = getIntent();
        this.organizer_key = intent.getStringExtra("organizer_key");

        addEvent = findViewById(R.id.addEvent);
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

        DatabaseReference organizerReference = database.organizerRef;
        organizerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot organizerSnapshot : dataSnapshot.getChildren()){
                        Organizer organizer = organizerSnapshot.getValue(Organizer.class);
                        if(organizerSnapshot.getKey().equals(organizer_key)){
                            dashboard_name.setText(organizer.getFirst_name());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

}
