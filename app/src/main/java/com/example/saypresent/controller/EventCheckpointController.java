package com.example.saypresent.controller;

import androidx.annotation.NonNull;

import com.example.saypresent.database.Database;
import com.example.saypresent.model.EventCheckpoint;
import com.example.saypresent.utils.AddCheckpointInterface;
import com.example.saypresent.utils.GetEventCheckpoint;
import com.example.saypresent.utils.GetEventCheckpoints;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventCheckpointController {
    private Database database = new Database();
    private final String EVENT_NODE = "event";
    private final String CHECKPOINT_NODE = "checkpoint";

    /**
     * Adding event checkpoint on an event
     * @param event_key
     * @param eventCheckpoint
     * @param addCheckpointInterface
     */
    public void addEventCheckpoint(String event_key, EventCheckpoint eventCheckpoint, final AddCheckpointInterface addCheckpointInterface){
//        DatabaseReference checkpointRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(CHECKPOINT_NODE);
        DatabaseReference checkpointRef = database.checkpointRef;
        String checkpoint_key = checkpointRef.push().getKey();

        eventCheckpoint.setCheckpoint_key(checkpoint_key);
        eventCheckpoint.setEvent_key(event_key);

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

    /**
     *
     * @param checkpoint_key
     */
    public void getEventCheckpoint(String checkpoint_key, final GetEventCheckpoint getEventCheckpoint){
//        DatabaseReference checkpointRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(CHECKPOINT_NODE).child(checkpoint_key);
        DatabaseReference checkpointRef = database.checkpointRef.child(checkpoint_key);
        checkpointRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    EventCheckpoint eventCheckpoint = dataSnapshot.getValue(EventCheckpoint.class);
                    getEventCheckpoint.onGetEventCheckpoint(eventCheckpoint);
                }else{
                   getEventCheckpoint.onGetEventCheckpoint(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     *
     * @param event_key
     * @param getEventCheckpoints
     */
    public void getEventCheckpoints(String event_key, final GetEventCheckpoints getEventCheckpoints){
//        DatabaseReference checkpointRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(CHECKPOINT_NODE);
        Query checkpointRef = database.checkpointRef.orderByChild("event_key").equalTo(event_key);
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
                }else{
                    getEventCheckpoints.onGetCheckpoint(null);
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
