package com.example.saypresent.controller;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.saypresent.LoginPage;
import com.example.saypresent.SignUp;
import com.example.saypresent.database.Database;
import com.example.saypresent.datastore.DataStore;
import com.example.saypresent.model.Organizer;
import com.example.saypresent.utils.AuthInterface;
import com.example.saypresent.utils.GetOrganizerInterface;
import com.example.saypresent.utils.RegistrationInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OrganizerController {
    private Database database = new Database();

//    CREATE ORGANIZER - REGISTER
    public void CreateOrganizer(final Organizer organizer, final RegistrationInterface registrationInterface){
        final String organizer_key = database.organizerRef.push().getKey();

        Query checkOrganizer = database.organizerRef.orderByChild("email").equalTo(organizer.getEmail());
        final Query checkAttendee = database.attendeeRef.orderByChild("email").equalTo(organizer.getEmail());

        checkOrganizer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    checkAttendee.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                database.organizerRef.child(organizer_key).setValue(organizer)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                registrationInterface.onCallback(true);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                            }else{
                                registrationInterface.onCallback(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("database error", databaseError.getMessage());
                        }
                    });
                }else{
                    registrationInterface.onCallback(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("database error", databaseError.getMessage());
            }
        });
    }

    //GET ORGANIZER - LOGIN
    public void OrganizerLogin(String email, final String password, final AuthInterface authInterface){
        Query organizerRef = database.organizerRef.orderByChild("email").equalTo(email);
        organizerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot organizerSnapshot : dataSnapshot.getChildren()){
                        Organizer organizer = organizerSnapshot.getValue(Organizer.class);
                        if(organizer.getPassword().equals(password)){
                            String organizer_key = organizerSnapshot.getKey();
                            authInterface.onCallback(organizer_key);
                            break;
                        }
                    }
                }else{
                    authInterface.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", databaseError.toString());
            }
        });
    }

    public void getOrganizer(final String organizer_key, final GetOrganizerInterface getOrganizerInterface){
        Query organizerReference = database.organizerRef;

        organizerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot organizerSnapshot : dataSnapshot.getChildren()){
                        Organizer organizer = organizerSnapshot.getValue(Organizer.class);
                        if(organizerSnapshot.getKey().equals(organizer_key)){
                            Log.i("organizer_key", organizerSnapshot.getKey());
                            getOrganizerInterface.onCallback(organizer);
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
