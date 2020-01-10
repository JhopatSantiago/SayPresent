package com.example.saypresent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

public class SignUp extends AppCompatActivity {

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
}
