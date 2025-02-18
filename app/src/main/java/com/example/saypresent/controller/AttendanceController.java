package com.example.saypresent.controller;

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
     *
     * Status:
     *  -1 => Attendee not found
     *  0  => failed
     *  1  => success
     */

    public void recordAttendance(final String event_key, final String checkpoint_key, final Attendee attendee, final RecordAttendance recordAttendance){
        final DatabaseReference attendanceRef = database.attendanceRef.child(checkpoint_key);
        Query eventAttendeeRef = database.event_attendeeRef.child(event_key).child(attendee.getAttendee_key());

        final String attendance_key = attendee.getAttendee_key();
        final String date = formatter.format(new Date());

        String attendee_key = attendee.getAttendee_key();
        String first_name = attendee.getFirst_name();
        String last_name = attendee.getLast_name();
        final Attendee attendance = new Attendee(attendance_key,first_name, last_name, date);
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
                List<Attendee> attendees = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (final DataSnapshot attendanceRef : dataSnapshot.getChildren()){
                        final Attendee attendeeDB = attendanceRef.getValue(Attendee.class);
                        attendees.add(attendeeDB);
//                        attendeeController.getAttendee(attendeeDB.getAttendee_key(), new GetAttendeeInterface() {
//                            @Override
//                            public void onGetAttendee(Attendee attendee) {
//                                if (attendee != null){
//                                    attendee.setTimestamp(attendeeDB.getTimestamp());
//                                    ReturnAttendance(attendee, getEventAttendeeInterface);
//                                }
//                            }
//                        });
                    }
                    getEventAttendeeInterface.onGetEventAttendees(attendees);

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
        if (attendee!= null){
            attendees.add(attendee);
            if (isAttendanceDone){
                getEventAttendeeInterface.onGetEventAttendees(attendees);
            }
        }
    }

}
