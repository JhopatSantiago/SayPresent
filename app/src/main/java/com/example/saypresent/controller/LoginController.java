package com.example.saypresent.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.saypresent.database.Database;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.model.Organizer;
import com.example.saypresent.utils.LoginInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Separate controller for login
 * Determine if user is an organizer or an attendee
 */

public class LoginController {
    private Database database = new Database();


    public void onLogin(String email, final String password, final LoginInterface loginInterface){
        Query organizerReference = database.organizerRef.orderByChild("email").equalTo(email);
        final Query attendeeReference = database.attendeeRef.orderByChild("email").equalTo(email);

        organizerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.i("found", "user found in organizers");
                    for (DataSnapshot loginSnapshot : dataSnapshot.getChildren()){
                        Organizer organizer = loginSnapshot.getValue(Organizer.class);
                        if (organizer.getPassword().equals(password)){
                            loginInterface.onLogin("organizer", loginSnapshot.getKey());
                        }else{
                            Log.i("login", "wrong password");
                            loginInterface.onLogin("Wrong Password", null);
                        }
                        break;
                    }
                }else{
                    Log.i("not found", "user with that email is not found on organizers");
                    attendeeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Log.i("found", "user found in attendees");
                                for (DataSnapshot attendeeSnap : dataSnapshot.getChildren()){
                                    Log.i("user found", "user with that email is found on attendees");
                                    Attendee attendee = attendeeSnap.getValue(Attendee.class);
                                    if (attendee.getPassword().equals(password)){
                                        loginInterface.onLogin("attendee", attendeeSnap.getKey());
                                    }else{
                                        Log.e("wrong password", "password is incorrect");
                                        loginInterface.onLogin("Wrong Password", null);
                                    }
                                    break;
                                }
                            }else{
                                Log.i("not found", "user not found");
                                loginInterface.onLogin("fail", null);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("database error", databaseError.getMessage());
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("database error", databaseError.getMessage());
            }
        });
    }

}
