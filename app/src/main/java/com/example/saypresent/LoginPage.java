package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.saypresent.controller.OrganizerController;
import com.example.saypresent.datastore.DataStore;
import com.example.saypresent.model.Organizer;
import com.example.saypresent.utils.AuthInterface;

public class LoginPage extends AppCompatActivity {
    private DataStore dataStore = new DataStore();
    private OrganizerController organizerController;
    private AuthInterface authInterface;

    private EditText email_field;
    private EditText password_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void SignIn(View v){

        organizerController = new OrganizerController();
//        Organizer organizer = new Organizer("Chris John", "Bico", "Mulingbayan", "cj@test.com", "password");

//        organizerController.CreateOrganizer(organizer, this);

        email_field = (EditText) findViewById(R.id.email);
        password_field = (EditText) findViewById(R.id.password);
        organizerController = new OrganizerController();

        String email = email_field.getText().toString();
        String password = password_field.getText().toString();

        boolean formOk = true;

        if(TextUtils.isEmpty(email)){
            email_field.setError("Email is required!");
            if(formOk){
                formOk = false;
            }
        }

        if (TextUtils.isEmpty(password)){
            password_field.setError("Password is required!");
            if (formOk){
                formOk = false;
            }
        }

        if(!formOk){
            return;
        }

        authInterface = new AuthInterface() {
           @Override
           public void onCallback(String organizer_key) {
               if (organizer_key == null){
                   //DITO PRE YUNG PAG ERROR di ko alam pano ipapakita yung error message e.
                   Log.e("failed", "no user found!");
               }else{ // There is a fetched user! hooray!
                   // DITO PRE IKAW NA BAHALA KUNG SAN PUPUNTA
                   //redirect to landing page
                   Log.i("organizer_key", organizer_key);
               }
           }
       };
        organizerController.OrganizerLogin(email, password, authInterface);
    }

    //PAPALITAN AKO NG MANGYAYARI DITO SA SUCCESS FUNCTION PRE
    public void showSuccess(String message){
        Log.i("success", message);
    }

    //DITO DIN PAPALITAN AKO PRE.
    public void showFailure(String message){
        Log.i("failure", message);
    }


}
