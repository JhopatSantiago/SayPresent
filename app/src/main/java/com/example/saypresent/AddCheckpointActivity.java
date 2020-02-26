package com.example.saypresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.saypresent.controller.EventCheckpointController;
import com.example.saypresent.model.EventCheckpoint;
import com.example.saypresent.utils.AddCheckpointInterface;
import com.google.android.material.navigation.NavigationView;

import javax.xml.validation.Validator;

/**
 * Adding Checkpoints in an event
 */
public class AddCheckpointActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String event_key;
    private String organizer_key;

    private EditText checkpoint_name_field;
    private EditText checkpoint_location_field;
    private Button save_btn;

    private EventCheckpoint eventCheckpoint;
    private EventCheckpointController eventCheckpointController = new EventCheckpointController();
    private LoadingDialog loadingDialog = new LoadingDialog(this);

    private DrawerLayout drawerLayoutAddCheck;
    private ActionBarDrawerToggle actionBarDrawerToggleAddCheck;
    private Toolbar toolbarAddCheck;
    private NavigationView navigationViewAddCheck;

    public AddCheckpointActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_checkpoint);

        //Side Bar Menu
        toolbarAddCheck = findViewById(R.id.toolbarAddCheckpoint);
        drawerLayoutAddCheck = findViewById(R.id.drawer_layoutCheckpoint);
        navigationViewAddCheck = findViewById(R.id.nav_view_addcheckpoint);
        setSupportActionBar(toolbarAddCheck);
        getSupportActionBar().setTitle(null);

        navigationViewAddCheck.bringToFront();
        actionBarDrawerToggleAddCheck = new ActionBarDrawerToggle(this,drawerLayoutAddCheck,toolbarAddCheck,R.string.open,R.string.close);
        drawerLayoutAddCheck.addDrawerListener(actionBarDrawerToggleAddCheck);
        actionBarDrawerToggleAddCheck.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggleAddCheck.syncState();
        navigationViewAddCheck.setNavigationItemSelectedListener(this);
        //

        Intent newintent = getIntent();
        organizer_key = newintent.getStringExtra("organizer_key");

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
//            case R.id.dashboard:
//                Intent dashboardintent = new Intent (AddCheckpointActivity.this,Dashboard.class);
//                dashboardintent.putExtra("organizer_key", organizer_key);
//                startActivity(dashboardintent);
//                break;
            case R.id.addEvent:
                Intent intent = new Intent (AddCheckpointActivity.this,addEvent.class);
                intent.putExtra("organizer_key", organizer_key);
                startActivity(intent);
                break;
//            case R.id.viewEvent:
//                Intent newintent = new Intent (AddCheckpointActivity.this,EventActivity.class);
//                newintent.putExtra("organizer_key", organizer_key);
//                startActivity(newintent);
//                break;
        }
        return true;
    }
}
