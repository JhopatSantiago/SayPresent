package com.example.saypresent.controller;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.saypresent.LoginPage;
import com.example.saypresent.database.Database;
import com.example.saypresent.model.Event;
import com.example.saypresent.utils.CreateEventInterface;
import com.example.saypresent.utils.DeleteEventInterface;
import com.example.saypresent.utils.GetEventHandler;
import com.example.saypresent.utils.GetEventsInterface;
import com.example.saypresent.utils.UpdateEventInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventController {
    private Database database = new Database();
    private final String EVENT_NODE = "event";
    private List<Event> events;

    //POST event
    public void CreateEvent(String organizer_key, Event event, final CreateEventInterface createEventInterface){
        DatabaseReference eventRef = database.organizerRef.child(organizer_key).child(EVENT_NODE);

        String event_key = eventRef.push().getKey();
        event.setEvent_key(event_key);
        eventRef.child(event_key).setValue(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        createEventInterface.onCreateEvent(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        createEventInterface.onCreateEvent(false);
                    }
                });
    }

    //GET events
    public void getEvents(String organizer_key, final GetEventsInterface getEventsInterface) {
        final Query eventsRef = database.organizerRef.child(organizer_key).child(EVENT_NODE);

        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    events = new ArrayList<>();
                    for (DataSnapshot eventSnaphot : dataSnapshot.getChildren() ) {
                        Event event = eventSnaphot.getValue(Event.class);
                        events.add(event);
                    }
                    getEventsInterface.onGetEvents(events);
                }
                eventsRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("database error", databaseError.getMessage());
            }
        });
    }

    public void getEvent(String organizer_key, String event_key, final GetEventHandler getEventHandler){
        DatabaseReference eventRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Event event = dataSnapshot.getValue(Event.class);
                    getEventHandler.onGetEvent(event);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error im get event", databaseError.getMessage());
            }
        });
    }

    public void updateEvent(String organizer_key, final String event_key, Event newEvent, final UpdateEventInterface updateEventInterface){
        DatabaseReference organizerRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key);
        organizerRef.setValue(newEvent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateEventInterface.onUpdateCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                updateEventInterface.onUpdateCallback(false);
            }
        });
    }

    public void deleteEvent(String organizer_key, String event_key, final DeleteEventInterface deleteEventInterface){
        DatabaseReference eventRef = database.organizerRef.child(organizer_key).child(EVENT_NODE).child(event_key);
        eventRef.setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deleteEventInterface.onDeleteEvent(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                deleteEventInterface.onDeleteEvent(false);
                e.printStackTrace();
            }
        });
    }

}
