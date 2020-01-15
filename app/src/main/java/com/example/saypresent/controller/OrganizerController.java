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
    public void CreateOrganizer(final Organizer organizer, final SignUp signUp){
        final String organizer_key = database.organizerRef.push().getKey();

        Query checkOrganizer = database.organizerRef.orderByChild("email").equalTo(organizer.getEmail());

        checkOrganizer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    database.organizerRef.child(organizer_key).setValue(organizer)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    signUp.clearFields();
                                    signUp.showSuccess("Successfully Created a user");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });
                }else{
                    signUp.showFailure("Email already exists!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("database error", databaseError.getMessage());
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
}
