package com.example.saypresent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    private Button LoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        //background animation
        ConstraintLayout constraintLayout = findViewById(R.id.Layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        /*LoginButton = findViewById(R.id.Button);*/
        final LoadingDialog loadingDialog = new LoadingDialog(LoginPage.this);

        /*LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                },5000);
            }
        });*/
    }
    public void SignIn(View v){

        organizerController = new OrganizerController();
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
               }else{
                   Log.i("organizer_key", organizer_key);
                   //declartion of loading
                   Intent intent = new Intent(LoginPage.this,Dashboard.class);
                   startActivity(intent);
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
