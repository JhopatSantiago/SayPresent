package com.example.saypresent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.saypresent.controller.OrganizerController;
import com.example.saypresent.model.Organizer;
import com.example.saypresent.utils.RegistrationInterface;

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

    private OrganizerController organizerController;
    private RegistrationInterface registrationInterface;
    private ProgressBar spinner;

    Button cancelbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        first_name_field = (EditText) findViewById(R.id.first_name);
        middle_name_field = (EditText) findViewById(R.id.middle_name);
        last_name_field = (EditText) findViewById(R.id.last_name);
        email_field = (EditText) findViewById(R.id.email_address);
        password_field = (EditText) findViewById(R.id.password);
        confirm_field = (EditText) findViewById(R.id.confirm);
        spinner =  (ProgressBar) findViewById(R.id.progress_circular);
        sign_up_button = (Button) findViewById(R.id.sign_up);

        spinner.setVisibility(View.GONE);
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

        if(TextUtils.isEmpty(middle_name)){
            middle_name_field.setError("Your middle name is required.");
            formOk = false;
        }

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
            confirm_field.setError("Oops! Your password does not match.");
            password_field.setText("");
            confirm_field.setText("");
            return;
        }

        if (!formOk){
            return;
        }
        sign_up_button.setEnabled(false);

        Organizer organizer = new Organizer(first_name, middle_name, last_name, email, password);
        organizerController = new OrganizerController();
        registrationInterface = new RegistrationInterface() {
            @Override
            public void onCallback(Boolean success) {
                if (success){
                    clearFields();
                    final LoadingDialog loadingDialog = new LoadingDialog(SignUp.this);
                    loadingDialog.startLoadingDialog();
                    Log.i("spinnner", "hide");
                    showSuccess("Congratulations! Account was created Successfully.");
                    isRegSuccess = true;
                    sign_up_button.setEnabled(true);
                }else{
                    final LoadingDialog loadingDialog = new LoadingDialog(SignUp.this);
                    loadingDialog.startLoadingDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                            Log.i("spinnner", "hide");

                            sign_up_button.setEnabled(true);
                        }
                    },3000);
                }
            }
        }   ;
        //show loading spinner
        Log.i("spinner", "show");

        organizerController.CreateOrganizer(organizer, registrationInterface);
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showFailure(String message){
        Log.e("failed", message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
