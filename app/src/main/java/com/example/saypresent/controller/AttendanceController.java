package com.example.saypresent.controller;

import androidx.annotation.NonNull;

import com.example.saypresent.database.Database;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.utils.GetAttendeeInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendanceController {
    private Database database = new Database();
    private DatabaseReference attendanceRef;
    private final String EVENT_NODE = "event";
    private final String CHECKPOINT_NODE = "checkpoint";
    private final String ATTENDEE_NODE = "attendee";

    private String organizer_key;
    private String event_key;
    private String checkpoint_key;
    private GetAttendeeInterface getAttendeeInterface;
    private AttendeeController attendeeController = new AttendeeController();


    public AttendanceController(String organizer_key, String event_key, String checkpoint_key){
        this.organizer_key = organizer_key;
        this.event_key = event_key;
        this.checkpoint_key = checkpoint_key;
        this.attendanceRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(CHECKPOINT_NODE).child(checkpoint_key);
    }

    /**
     * Function will be used by the QR code scanner
     * @param attendee_key
     */
    public void recordAttendance(final String attendee_key){
        //CHECK FIRST IF ATTENDEE IS ON EVENT ATTENDEES
        getAttendeeInterface = new GetAttendeeInterface() {
            @Override
            public void onGetAttendee(Attendee attendee) {
                if(attendee != null){
                    recordAttendee(attendee);
                }
            }
        };

        attendeeController = new AttendeeController();
        attendeeController.getAttendee(organizer_key, event_key, attendee_key,getAttendeeInterface);
    }


    private void recordAttendee(Attendee attendee){
        attendanceRef.child(ATTENDEE_NODE).child(attendee.getAttendee_key()).setValue(attendee)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //SUCCESSFULLY RECORDED ATTENDANCE
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void getAttendance(){
        final List<Attendee> attendees = new ArrayList<>();
        attendanceRef.child(ATTENDEE_NODE).addValueEventListener(new ValueEventListener() {
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
}
