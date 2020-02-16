package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.saypresent.adapter.AttendeeAdapter;
import com.example.saypresent.controller.AttendanceController;
import com.example.saypresent.controller.EventCheckpointController;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.model.EventCheckpoint;
import com.example.saypresent.utils.CustomEventClickListener;
import com.example.saypresent.utils.GetEventAttendeeInterface;
import com.example.saypresent.utils.GetEventCheckpoint;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Inisde Event checkpoint activity
 * List of all scanned attendee QR Code
 * Scan QR code button to trigger QR Scanner for attendance
 *
 */
public class CheckpointActivity extends AppCompatActivity {

    private String organizer_key;
    private String event_key;
    private String checkpoint_key;

    private TextView checkpointNameField;
    private TextView checkpointLocationField;
    private Button qr_button;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private EventCheckpointController eventCheckpointController = new EventCheckpointController();
    private AttendanceController attendanceController = new AttendanceController();

    private List<Attendee> attendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint);

        Intent intent = getIntent();
        event_key = intent.getStringExtra("event_key");
        checkpoint_key = intent.getStringExtra("checkpoint_key");

        checkpointNameField = (TextView) findViewById(R.id.checkpoint_name);
        checkpointLocationField = (TextView) findViewById(R.id.checkpoint_location);
        initialize(); // Initialize this activity contents

        recyclerView = (RecyclerView) findViewById(R.id.attendanceRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        qr_button = (Button) findViewById(R.id.btn_qrcode);


        qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent QR_Intent = new Intent(getApplicationContext(), QRScannerActivity.class);
                QR_Intent.putExtra("checkpoint_key", checkpoint_key);
                QR_Intent.putExtra("event_key", event_key);
                QR_Intent.putExtra("action","checkpoint");
                startActivity(QR_Intent);
            }
        });
    }

    private void InstantiateAdapter(final List<Attendee> attendees){
        mAdapter = new AttendeeAdapter(attendees, new CustomEventClickListener() {
            @Override
            public void onItemClick(View v, int i) {
                Log.i("attendee key", attendees.get(i).getAttendee_key());
            }
        });
        recyclerView.setAdapter(mAdapter);
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

       attendanceController.getAttendance(checkpoint_key, new GetEventAttendeeInterface() {
           @Override
           public void onGetEventAttendees(List<Attendee> attendees) {
                if (attendees != null){
                    InstantiateAdapter(attendees);
                }
           }
       });
   }
}
