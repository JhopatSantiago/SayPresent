package com.example.saypresent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.saypresent.controller.OrganizerController;
import com.example.saypresent.model.Organizer;

public class SignUp extends AppCompatActivity {

    private EditText first_name_field;
    private EditText middle_name_field;
    private EditText last_name_field;
    private EditText email_field;
    private EditText password_field;
    private EditText confirm_field;

    private final String REQUIRED = "Required";

    private OrganizerController organizerController;

    Button cancelbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onBackPressed(){
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
             .setNegativeButton("No",null)
             .show();
    }

    public void sign_up(View v){
        first_name_field = (EditText) findViewById(R.id.first_name);
        middle_name_field = (EditText) findViewById(R.id.middle_name);
        last_name_field = (EditText) findViewById(R.id.last_name);
        email_field = (EditText) findViewById(R.id.email_address);
        password_field = (EditText) findViewById(R.id.password);
        confirm_field = (EditText) findViewById(R.id.confirm);

        String first_name = first_name_field.getText().toString();
        String middle_name = middle_name_field.getText().toString();
        String last_name = last_name_field.getText().toString();
        String email = email_field.getText().toString();
        String password = password_field.getText().toString();
        String confirm = confirm_field.getText().toString();

        if(TextUtils.isEmpty(first_name)){
            first_name_field.setError(REQUIRED);
            return;
        }

        if(TextUtils.isEmpty(middle_name)){
            middle_name_field.setError(REQUIRED);
            return;
        }

        if(TextUtils.isEmpty(last_name)){
            last_name_field.setError(REQUIRED);
            return;
        }

        if(TextUtils.isEmpty(email)){
            email_field.setError(REQUIRED);
            return;
        }

        if(TextUtils.isEmpty(password)){
            password_field.setError(REQUIRED);
            return;
        }

        if(TextUtils.isEmpty(confirm)){
            confirm_field.setError(REQUIRED);
            return;
        }

        if(!confirm.equals(password)){
            confirm_field.setError("Password does not match!");
            password_field.setText("");
            confirm_field.setText("");
            return;
        }

        Organizer organizer = new Organizer(first_name, middle_name, last_name, email, password);
        organizerController = new OrganizerController();

        organizerController.CreateOrganizer(organizer, this);
    }

    public void clearFields(){
        this.first_name_field.setText("");
        this.middle_name_field.setText("");
        this.last_name_field.setText("");
        this.email_field.setText("");
        this.password_field.setText("");
        this.confirm_field.setText("");
    }

    public void showSuccess(String message){
        Log.i("success", message); // Create Success dialog
    }

    public void showFailure(String message){
        Log.e("failed", message);
    }

}
