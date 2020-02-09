package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.saypresent.controller.EventCheckpointController;
import com.example.saypresent.model.EventCheckpoint;
import com.example.saypresent.utils.GetEventCheckpoint;

public class CheckpointActivity extends AppCompatActivity {

    private String organizer_key;
    private String event_key;
    private String checkpoint_key;

    private TextView checkpointNameField;
    private TextView checkpointLocationField;

    private EventCheckpointController eventCheckpointController = new EventCheckpointController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint);

        Intent intent = getIntent();
        checkpoint_key = intent.getStringExtra("checkpoint_key");

        checkpointNameField = (TextView) findViewById(R.id.checkpoint_name);
        checkpointLocationField = (TextView) findViewById(R.id.checkpoint_location);

        initialize(); // Initialize this activity contents
    }

   private void initialize(){
       eventCheckpointController.getEventCheckpoint(checkpoint_key, new GetEventCheckpoint() {
           @Override
           public void onGetEventCheckpoint(EventCheckpoint eventCheckpoint) {
               String checkpoint_name = eventCheckpoint.getCheckpoint_name();
               String checkpoint_location = eventCheckpoint.getCheckpoint_location();

               checkpointNameField.setText(checkpoint_name);
               checkpointLocationField.setText(checkpoint_location);
           }
       });
   }
}
