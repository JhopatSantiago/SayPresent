package com.example.saypresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saypresent.adapter.CheckpointsAdapter;
import com.example.saypresent.controller.EventCheckpointController;
import com.example.saypresent.controller.EventController;
import com.example.saypresent.model.Event;
import com.example.saypresent.model.EventCheckpoint;
import com.example.saypresent.utils.AddCheckpointInterface;
import com.example.saypresent.utils.CustomEventClickListener;
import com.example.saypresent.utils.GetEventCheckpoints;
import com.example.saypresent.utils.GetEventHandler;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

/**
 * Displays Details of an Event
 * trigger for QR Scanner to ADD an ATTENDEE
 * trigger for Event's Registered ATTENDEES
 */
public class EventDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String organizer_key;
    private String event_key;

    private TextView event_name;
    private TextView event_location;
//    private Button qr_button;
//    private Button eventAttendeeBtn;
//    private Button addCheckpointBtn;

    private EventController eventController = new EventController();
    private GetEventHandler getEventHandler;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<EventCheckpoint> eventCheckpoints;
    private EventCheckpointController eventCheckpointController = new EventCheckpointController();

    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton addCheckpoint,scanQR,addAttendee;

    private DrawerLayout drawerLayoutEvent;
    private ActionBarDrawerToggle actionBarDrawerToggleEvent;
    private Toolbar toolbarEvent;
    private NavigationView navigationViewEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        toolbarEvent = findViewById(R.id.toolbar_eventdetail);
        drawerLayoutEvent = findViewById(R.id.drawer_layoutEventDetail);
        navigationViewEvent = findViewById(R.id.nav_view_eventdetail);
        setSupportActionBar(toolbarEvent);
        getSupportActionBar().setTitle(null);

        //side menu navigation
        navigationViewEvent.bringToFront();
        actionBarDrawerToggleEvent = new ActionBarDrawerToggle(this,drawerLayoutEvent,toolbarEvent,R.string.open,R.string.close);
        drawerLayoutEvent.addDrawerListener(actionBarDrawerToggleEvent);
        actionBarDrawerToggleEvent.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggleEvent.syncState();
        navigationViewEvent.setNavigationItemSelectedListener(this);
        navigationViewEvent.setCheckedItem(R.id.dashboard);
        //
        final Intent intent = getIntent();
        organizer_key  = intent.getStringExtra("organizer_key");
        event_key = intent.getStringExtra("event_key");
        initialize();
        event_name = (TextView) findViewById(R.id.event_name);
        event_location = (TextView) findViewById(R.id.event_location);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floatingActionLabel);
        addCheckpoint = (FloatingActionButton) findViewById(R.id.addCheckpointBtn);
        scanQR = (FloatingActionButton) findViewById(R.id.btn_qrcode);
        addAttendee = (FloatingActionButton) findViewById(R.id.eventAttendeeBtn);

        //this button will trigger qr_code scanner of an event
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scannerIntent = new Intent(getApplicationContext(), QRScannerActivity.class);
                scannerIntent.putExtra("event_key", event_key);
                scannerIntent.putExtra("action", "event");
                startActivity(scannerIntent);
            }
        });

        //this button will trigger event attendee(EventAttendeeActivity.java) list activity
        addAttendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventAttendeeIntent = new Intent(getApplicationContext(), EventAttendeeActivity.class);
                eventAttendeeIntent.putExtra("event_key", event_key);
                eventAttendeeIntent.putExtra("organizer_key", organizer_key);
                startActivity(eventAttendeeIntent);
            }
        });

        addCheckpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent addCheckpointIntent = new Intent(getApplicationContext(), AddCheckpointActivity.class);
               addCheckpointIntent.putExtra("event_key", event_key);
               addCheckpointIntent.putExtra("organizer_key", organizer_key);
               startActivity(addCheckpointIntent);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.checkpointRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize(){
        getEventHandler = new GetEventHandler() {
            @Override
            public void onGetEvent(Event event) {
                String name = event.getEvent_name();
                String location = event.getLocation();

                event_name.setText(name);
                event_location.setText(location);
            }
        };
        eventController.getEvent(organizer_key, event_key, getEventHandler);

        eventCheckpointController.getEventCheckpoints(event_key, new GetEventCheckpoints() {
            @Override
            public void onGetCheckpoint(List<EventCheckpoint> eventCheckpoints) {
                if(eventCheckpoints != null){
                    instantiateAdapter(eventCheckpoints);
                }else{
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void instantiateAdapter(final List<EventCheckpoint> eventCheckpoints){
        CustomEventClickListener customEventClickListener = new CustomEventClickListener() {
            @Override
            public void onItemClick(View v, int i) {
                EventCheckpoint eventCheckpoint = eventCheckpoints.get(i);
                String checkpoint_key = eventCheckpoint.getCheckpoint_key();

                Intent intent = new Intent(getApplicationContext(), CheckpointActivity.class);
                intent.putExtra("checkpoint_key", checkpoint_key);
                intent.putExtra("event_key", event_key);
                intent.putExtra("organizer_key", organizer_key);
                startActivity(intent);
            }
        };

        mAdapter = new CheckpointsAdapter(eventCheckpoints, customEventClickListener);
        recyclerView.setAdapter(mAdapter);
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.dashboard:
                Intent dashboardintent = new Intent (EventDetailActivity.this,Dashboard.class);
                dashboardintent.putExtra("organizer_key", organizer_key);
                startActivity(dashboardintent);
                drawerLayoutEvent.closeDrawers();
                break;
            case R.id.addEvent:
                Intent intent = new Intent (EventDetailActivity.this,addEvent.class);
                intent.putExtra("organizer_key", organizer_key);
                startActivity(intent);
                drawerLayoutEvent.closeDrawers();
                break;
            case R.id.viewEvent:
                Intent ViewEventIntent = new Intent (EventDetailActivity.this,EventActivity.class);
                ViewEventIntent.putExtra("organizer_key", organizer_key);
                startActivity(ViewEventIntent);
                drawerLayoutEvent.closeDrawers();
                break;
            case R.id.Logout:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Going Away?")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent (EventDetailActivity.this,LoginPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                EventDetailActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            case R.id.AboutUs:
                Intent intentAbout = new Intent (EventDetailActivity.this,EventActivity.class);
                intentAbout.putExtra("organizer_key", organizer_key);
                drawerLayoutEvent.closeDrawers();
                startActivity(intentAbout);
                break;
        }
        return true;
    }
}
