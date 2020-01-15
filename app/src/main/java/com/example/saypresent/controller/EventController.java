package com.example.saypresent.controller;

import androidx.annotation.NonNull;

import com.example.saypresent.LoginPage;
import com.example.saypresent.database.Database;
import com.example.saypresent.model.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class EventController {
    private Database database = new Database();
    private final String EVENT_NODE = "event";
    private List<Event> events;

    public void CreateEvent(String organizer_key, Event event){
        DatabaseReference eventRef = database.organizerRef.child(organizer_key).child(EVENT_NODE);

        String event_key = eventRef.push().getKey();

        eventRef.child(event_key).setValue(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }


}
