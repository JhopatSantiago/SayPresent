package com.example.saypresent.controller;

import androidx.annotation.NonNull;

import com.example.saypresent.database.Database;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.model.Event;
import com.example.saypresent.utils.AddAttendeeInterface;
import com.example.saypresent.utils.GetAttendeeInterface;
import com.example.saypresent.utils.RemoveAttendeeInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendeeController {
    private Database database = new Database();
    private final String ATTENDEE_NODE = "attendee";
    private final  String EVENT_NODE = "event";

    public void addAttendee(String organizer_key, String event_key, Attendee attendee, final AddAttendeeInterface addAttendeeInterface){

        DatabaseReference attendeeRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(ATTENDEE_NODE);
        String attendee_key = attendeeRef.push().getKey();
        attendee.setAttendee_key(attendee_key);
        attendeeRef.child(attendee_key).setValue(attendee)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addAttendeeInterface.onAddAttendee(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        addAttendeeInterface.onAddAttendee(false);
                    }
                });
    }

    public void removeAttendee(String organizer_key, String event_key, String attendee_key, final RemoveAttendeeInterface removeAttendeeInterface){
        DatabaseReference attendeeRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(attendee_key);

        attendeeRef.setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        removeAttendeeInterface.onRemoveAttendee(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        removeAttendeeInterface.onRemoveAttendee(false);
                    }
                });
    }

    public void getAtteendees(String organizer_key, String event_key){
        DatabaseReference attendeeRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(ATTENDEE_NODE);
        final List<Attendee> attendees = new ArrayList<>();

        attendeeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Attendee attendee = ds.getValue(Attendee.class);
                        attendees.add(attendee);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAttendee(String organizer_key, String event_key,String attendee_key, final GetAttendeeInterface getAttendeeInterface){
        DatabaseReference attendeeRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(ATTENDEE_NODE).child(attendee_key);

        attendeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Attendee attendee = ds.getValue(Attendee.class);
                        getAttendeeInterface.onGetAttendee(attendee);
                    }
                }else{
                    getAttendeeInterface.onGetAttendee(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
