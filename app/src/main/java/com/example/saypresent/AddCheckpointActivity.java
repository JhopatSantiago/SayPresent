package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saypresent.controller.EventCheckpointController;
import com.example.saypresent.model.EventCheckpoint;
import com.example.saypresent.utils.AddCheckpointInterface;

import javax.xml.validation.Validator;

/**
 * Adding Checkpoints in an event
 */
public class AddCheckpointActivity extends AppCompatActivity {
    private String event_key;

    private EditText checkpoint_name_field;
    private EditText checkpoint_location_field;
    private Button save_btn;

    private EventCheckpoint eventCheckpoint;
    private EventCheckpointController eventCheckpointController = new EventCheckpointController();
    private LoadingDialog loadingDialog = new LoadingDialog(this);

    public AddCheckpointActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_checkpoint);

        Intent intent = getIntent();

        event_key = intent.getStringExtra("event_key");

        checkpoint_name_field = (EditText) findViewById(R.id.checkpoint_name);
        checkpoint_location_field = (EditText) findViewById(R.id.checkpoint_location);
        save_btn = (Button) findViewById(R.id.save_checkpoint);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateInputsAndSubmit();
            }
        });
    }

    private void ValidateInputsAndSubmit(){
        String checkpoint_name = checkpoint_name_field.getText().toString();
        String checkpoint_location = checkpoint_location_field.getText().toString();

        if (TextUtils.isEmpty(checkpoint_name)){
            checkpoint_name_field.setError("Checkpoint name required");
            checkpoint_name_field.setText(null);
            return;
        }
        loadingDialog.startLoadingDialog();
        eventCheckpoint = new EventCheckpoint(checkpoint_name, checkpoint_location, "");
        eventCheckpointController.addEventCheckpoint(event_key, eventCheckpoint, new AddCheckpointInterface() {
            @Override
            public void onAddCheckpoint(boolean success) {
               if (success){
                   loadingDialog.dismissDialog();
                   clearFields();
                   Toast.makeText(getApplicationContext(), "Successfully Created an Event Checkpoint", Toast.LENGTH_SHORT).show();
               }else {
                   loadingDialog.dismissDialog();
                   Toast.makeText(getApplicationContext(), "Failure in creating an Event Checkpoint", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    private void clearFields(){
        checkpoint_location_field.setText(null);
        checkpoint_name_field.setText(null);
    }
}
