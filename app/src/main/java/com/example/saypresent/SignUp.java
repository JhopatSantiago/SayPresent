package com.example.saypresent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.saypresent.controller.AttendeeController;
import com.example.saypresent.controller.OrganizerController;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.model.Organizer;
import com.example.saypresent.utils.AddAttendeeInterface;
import com.example.saypresent.utils.RegistrationInterface;

/**
 * Activity for Sign Up
 */
public class SignUp extends AppCompatActivity {

    private EditText first_name_field;
    private EditText middle_name_field;
    private EditText last_name_field;
    private EditText email_field;
    private EditText password_field;
    private EditText confirm_field;
    private Button sign_up_button;
    private boolean formOk;
    private boolean isRegSuccess = false;

    private final String REQUIRED = "Required";
    private String user_type;

    private OrganizerController organizerController;
    private RegistrationInterface registrationInterface;
    private ProgressBar spinner;
    private AttendeeController attendeeController;
    private AddAttendeeInterface addAttendeeInterface;

    final LoadingDialog loadingDialog = new LoadingDialog(SignUp.this);

    Button cancelbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();

        this.user_type = intent.getStringExtra("user_type");

        first_name_field = (EditText) findViewById(R.id.first_name);
        middle_name_field = (EditText) findViewById(R.id.middle_name);
        last_name_field = (EditText) findViewById(R.id.last_name);
        email_field = (EditText) findViewById(R.id.email_address);
        password_field = (EditText) findViewById(R.id.password);
        confirm_field = (EditText) findViewById(R.id.confirm);
        spinner =  (ProgressBar) findViewById(R.id.progress_circular);
        sign_up_button = (Button) findViewById(R.id.sign_up);

        spinner.setVisibility(View.GONE);

        ConstraintLayout constraintLayout = findViewById(R.id.Layout2);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }


    public void onBackPressed() {
        if (!isRegSuccess) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Going Away?")
                    .setMessage("Are you sure you want to cancel?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else{
            finish();
        }
    }


    public void sign_up(View v){
        formOk = true;

        String first_name = first_name_field.getText().toString();
        String middle_name = middle_name_field.getText().toString();
        String last_name = last_name_field.getText().toString();
        String email = email_field.getText().toString();
        String password = password_field.getText().toString();
        String confirm = confirm_field.getText().toString();

        if(TextUtils.isEmpty(first_name)){
            first_name_field.setError("Your first Name is required.");
            formOk = false;
        }

//        if(TextUtils.isEmpty(middle_name)){
//            middle_name_field.setError("Your middle name is required.");
//            formOk = false;
//        }

        if(TextUtils.isEmpty(last_name)){
            last_name_field.setError("Your last name is required.");
            formOk = false;
        }

        if(TextUtils.isEmpty(email)){
            email_field.setError("Your email address is required.");
            formOk = false;
        }

        if(TextUtils.isEmpty(password)){
            password_field.setError("Password is required.");
            formOk = false;
        }

        if(TextUtils.isEmpty(confirm)){
            confirm_field.setError("Field cannot be left blank");
            formOk = false;
        }

        if(!confirm.equals(password)){
            AlertDialog.Builder Alert = new AlertDialog.Builder(SignUp.this);
            Alert.setTitle("Oops!");
            Alert.setMessage("Password does not match.");
            Alert.setPositiveButton("OK",null);
            Alert.show();
            password_field.setText("");
            confirm_field.setText("");
            return;
        }

        if (!formOk){
            return;
        }

        sign_up_button.setEnabled(false);
        loadingDialog.startLoadingDialog();

        if(user_type.equals("organizer")){
            Organizer organizer = new Organizer(first_name, middle_name, last_name, email, password);
            organizerController = new OrganizerController();
            registrationInterface = new RegistrationInterface() {
                @Override
                public void onCallback(Boolean success) {
                    showSuccess(success);
                }
            };
            //show loading spinner
            Log.i("spinner", "show");
            organizerController.CreateOrganizer(organizer, registrationInterface);

        }else if (user_type.equals("attendee")){
            attendeeController = new AttendeeController();
            addAttendeeInterface = new AddAttendeeInterface() {
                @Override
                public void onAddAttendee(boolean success) {
                    showSuccess(success);
                }
            };
            Attendee attendee = new Attendee(first_name, middle_name, last_name, email, password);
            attendeeController.createAttendee(attendee, addAttendeeInterface);
        }
    }



    private void clearFields(){
        this.first_name_field.setText("");
        this.middle_name_field.setText("");
        this.last_name_field.setText("");
        this.email_field.setText("");
        this.password_field.setText("");
        this.confirm_field.setText("");
    }

    public void showSuccess(boolean success){
        if (success){
            clearFields();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("spinnner", "hide");
                    Handler Redirect = new Handler();
                    Redirect.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                            AlertDialog.Builder Alert = new AlertDialog.Builder(SignUp.this);
                            Alert.setTitle("Success!");
                            Alert.setMessage("Your account was created.");
                            Alert.setPositiveButton(null,null);
                            Alert.show();
                        }
                    },1000);
                    Handler toLogin = new Handler();
                    toLogin.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Prevent the back button to go back again
                            Intent intent = new Intent (SignUp.this,LoginPage.class);
                            intent.putExtra("finish",true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    },3000);
                    isRegSuccess = true;
                    sign_up_button.setEnabled(true);
                }
            },3000);
        }else{
//            final LoadingDialog loadingDialog = new LoadingDialog(SignUp.this);
//                    loadingDialog.startLoadingDialog();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismissDialog();
                    Log.i("spinnner", "hide");
                    AlertDialog.Builder Alert = new AlertDialog.Builder(SignUp.this);
                    Alert.setTitle("Oops!");
                    Alert.setMessage("Email address is already existed");
                    Alert.setPositiveButton("OK",null);
                    Alert.show();
                    sign_up_button.setEnabled(true);
                }
            },3000);
        }
    }

    public void showFailure(String message){
        Log.e("failed", message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
