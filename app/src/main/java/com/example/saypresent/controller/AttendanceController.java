package com.example.saypresent.controller;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.saypresent.database.Database;
import com.example.saypresent.model.Attendance;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.utils.GetAttendeeInterface;
import com.example.saypresent.utils.GetEventAttendeeInterface;
import com.example.saypresent.utils.RecordAttendance;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceController {
    private Database database = new Database();
    private DatabaseReference attendanceRef;
    private final String EVENT_NODE = "event";
    private final String CHECKPOINT_NODE = "checkpoint";
    private final String ATTENDEE_NODE = "attendee";

    private GetAttendeeInterface getAttendeeInterface;
    private AttendeeController attendeeController = new AttendeeController();
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


    /**
     * FUNCTION TO BE CALLED BY QR SCANNER
     * RECORDS ATTENDANCE with attendee key
     * @param checkpoint_key
     * @param attendee_key
     *
     * Status:
     *  -1 => Attendee not found
     *  0  => failed
     *  1  => success
     */

    public void recordAttendance(final String event_key, final String checkpoint_key, final String attendee_key, final RecordAttendance recordAttendance){
        final DatabaseReference attendanceRef = database.attendanceRef.child(checkpoint_key);
        Query eventAttendeeRef = database.event_attendeeRef.child(event_key).child(attendee_key);

        final String attendance_key = attendanceRef.push().getKey();


        final String date = formatter.format(new Date());

        final Attendance attendance = new Attendance(attendee_key, date);
        eventAttendeeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    attendanceRef.child(attendance_key).setValue(attendance)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    recordAttendance.onRecord(false, 0);
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    recordAttendance.onRecord(true, 1);
                                }
                            });
                }else{
                    recordAttendance.onRecord(false, -1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("database error", databaseError.getMessage());
            }
        });
    }

    private boolean isAttendanceDone = false;
    private List<Attendee> attendees = new ArrayList<>();

    public void getAttendance(String checkpoint_key, final GetEventAttendeeInterface getEventAttendeeInterface){
        Query attendanceRef = database.attendanceRef.child(checkpoint_key);
        attendanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<Attendee> attendees = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot attendanceRef : dataSnapshot.getChildren()){
                        final Attendee attendeeDB = attendanceRef.getValue(Attendee.class);
                        attendeeController.getAttendee(attendeeDB.getAttendee_key(), new GetAttendeeInterface() {
                            @Override
                            public void onGetAttendee(Attendee attendee) {
                                attendee.setTimestamp(attendeeDB.getTimestamp());
                                ReturnAttendance(attendee, getEventAttendeeInterface);
                            }
                        });
                    }
                    isAttendanceDone = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("database error", databaseError.getMessage());
            }
        });
    }

    private void ReturnAttendance(Attendee attendee, GetEventAttendeeInterface getEventAttendeeInterface){
        attendees.add(attendee);
        if (isAttendanceDone){
            getEventAttendeeInterface.onGetEventAttendees(attendees);
        }
    }

}
