package com.example.saypresent.controller;

import androidx.annotation.NonNull;

import com.example.saypresent.database.Database;
import com.example.saypresent.model.EventCheckpoint;
import com.example.saypresent.utils.AddCheckpointInterface;
import com.example.saypresent.utils.GetEventCheckpoints;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventCheckpointController {
    private Database database = new Database();
    private final String EVENT_NODE = "event";
    private final String CHECKPOINT_NODE = "checkpoint";

    public void addEventCheckpoint(String organizer_key, String event_key, EventCheckpoint eventCheckpoint, final AddCheckpointInterface addCheckpointInterface){
        DatabaseReference checkpointRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(CHECKPOINT_NODE);
        String checkpoint_key = checkpointRef.push().getKey();

        eventCheckpoint.setCheckpoint_key(checkpoint_key);

        checkpointRef.child(checkpoint_key).setValue(eventCheckpoint)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       addCheckpointInterface.onAddCheckpoint(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        addCheckpointInterface.onAddCheckpoint(false);
                        e.printStackTrace();
                    }
                });
    }

    public void getEventCheckpoint(String organizer_key, String event_key, String checkpoint_key){
        DatabaseReference checkpointRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(CHECKPOINT_NODE).child(checkpoint_key);
        checkpointRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        EventCheckpoint eventCheckpoint = ds.getValue(EventCheckpoint.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getEventCheckpoints(String organizer_key, String event_key, final GetEventCheckpoints getEventCheckpoints){
        DatabaseReference checkpointRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(CHECKPOINT_NODE);
        final List<EventCheckpoint> eventCheckpoints = new ArrayList<>();
        checkpointRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        EventCheckpoint eventCheckpoint = ds.getValue(EventCheckpoint.class);
                        eventCheckpoints.add(eventCheckpoint);
                    }
                    getEventCheckpoints.onGetCheckpoint(eventCheckpoints);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateEventCheckpoint(String organizer_key, String event_key, String checkpoint_key, EventCheckpoint eventCheckpoint){
        DatabaseReference checkpointRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(CHECKPOINT_NODE).child(checkpoint_key);

        checkpointRef.setValue(eventCheckpoint)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //success updating the event checkpoint
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void deleteEventCheckpoint(String organizer_key, String event_key, String checkpoint_key){
        DatabaseReference checkpointRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(CHECKPOINT_NODE).child(checkpoint_key);

        checkpointRef.setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //success deleting event checkpoint
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
