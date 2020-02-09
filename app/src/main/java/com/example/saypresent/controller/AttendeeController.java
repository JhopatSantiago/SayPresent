package com.example.saypresent.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.saypresent.database.Database;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.model.Event;
import com.example.saypresent.utils.AddAttendeeInterface;
import com.example.saypresent.utils.GetAttendeeInterface;
import com.example.saypresent.utils.GetEventAttendeeInterface;
import com.example.saypresent.utils.GetRegisteredAttendeeInterface;
import com.example.saypresent.utils.RemoveAttendeeInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendeeController {
    private Database database = new Database();
    private final String ATTENDEE_NODE = "attendee";
    private final  String EVENT_NODE = "event";

    /**
     * This function will be used by the QR Code Scanner
     * @param organizer_key
     * @param event_key
     * @param attendee
     * @param addAttendeeInterface
     */
    public void addAttendeeOnEvent(String organizer_key, String event_key, Attendee attendee, final AddAttendeeInterface addAttendeeInterface){
//        DatabaseReference attendeeRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key).child(ATTENDEE_NODE);
        DatabaseReference attendeeRef = database.event_attendeeRef;
        String attendee_key = attendee.getAttendee_key();
        attendee.setEvent_key(event_key);
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


    /**
     * Adding attendee on a list of attendees
     * @param attendee
     * @param addAttendeeInterface
     */
    public void createAttendee(final Attendee attendee, final AddAttendeeInterface addAttendeeInterface){
        final String attendee_key = database.attendeeRef.push().getKey();
        attendee.setAttendee_key(attendee_key);
        String email = attendee.getEmail();
        Query checkAttendee = database.attendeeRef.orderByChild("email").equalTo(email);
        final Query checkOrganizer = database.organizerRef.orderByChild("email").equalTo(email);
        final DatabaseReference attendeeRef = database.attendeeRef;

        checkAttendee.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    checkOrganizer.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if(!dataSnapshot.exists()){
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
                                               e.printStackTrace();
                                               addAttendeeInterface.onAddAttendee(false);
                                           }
                                       });
                           }else{
                               // organizer with that email already exists
                               addAttendeeInterface.onAddAttendee(false);
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                           Log.e("database error", databaseError.getMessage());
                       }
                   });
                }else{
                    // attendee with that email already exists.
                    addAttendeeInterface.onAddAttendee(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("database error", databaseError.getMessage());
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

   /**
     * GET ALL ATTENDEES OF A SPECIFIC EVENT
     * @param event_key
     */
    public void getEventAtteendees(String event_key, final GetEventAttendeeInterface getEventAttendeeInterface){
        Query attendeeRef = database.event_attendeeRef.orderByChild("event_key").equalTo(event_key);
//        DatabaseReference attendeeRef = database.event_attendeeRef;
        final List<Attendee> attendees = new ArrayList<>();

        attendeeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Attendee attendee = ds.getValue(Attendee.class);
                        attendees.add(attendee);
                    }
                    getEventAttendeeInterface.onGetEventAttendees(attendees);
                }else {
                    getEventAttendeeInterface.onGetEventAttendees(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", "error in attendeeController.getEventAttendees");
                Log.e("database error", databaseError.getMessage());
            }
        });
    }

    /**
     * GET ALL REGISTERED ATTENDEES
     * @param getRegisteredAttendeeInterface
     */

    public void getAllRegisteredAttendees(final GetRegisteredAttendeeInterface getRegisteredAttendeeInterface){
        DatabaseReference registeredAttendeesRef = database.attendeeRef;

        registeredAttendeesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Attendee> attendees = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()){
                        Attendee attendee = attendeeSnapshot.getValue(Attendee.class);
                        attendees.add(attendee);
                    }
                    getRegisteredAttendeeInterface.onGetRegisteredAttendees(attendees);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(" database error", databaseError.getMessage());
            }
        });
    }

    /**
     * Get Attendee
     * @param attendee_key
     * @param getAttendeeInterface
     */
    public void getAttendee(final String attendee_key, final GetAttendeeInterface getAttendeeInterface){
        DatabaseReference attendeeQuery = database.attendeeRef.child(attendee_key);

        attendeeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Attendee attendee = dataSnapshot.getValue(Attendee.class);
                    getAttendeeInterface.onGetAttendee(attendee);
                }else{
                    getAttendeeInterface.onGetAttendee(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("database error", databaseError.getMessage());
            }
        });
    }
}
