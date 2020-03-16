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

import com.example.saypresent.adapter.AttendeeAdapter;
import com.example.saypresent.controller.AttendeeController;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.utils.CustomEventClickListener;
import com.example.saypresent.utils.GetEventAttendeeInterface;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

/**
 * Displays all attendees of an Event
 * Event => Attendee
 */
public class EventAttendeeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String event_key;
    private String organizer_key;
    private AttendeeController attendeeController = new AttendeeController();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private DrawerLayout drawerLayoutViewAttendee;
    private ActionBarDrawerToggle actionBarDrawerToggleViewAttendee;
    private Toolbar toolbarViewAttendee;
    private NavigationView navigationViewAttendee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attendee);

        toolbarViewAttendee = findViewById(R.id.toolbar_viewAttendee);
        drawerLayoutViewAttendee = findViewById(R.id.drawerlayout_viewAttendee);
        navigationViewAttendee = findViewById(R.id.nav_view_viewAttendee);
        setSupportActionBar(toolbarViewAttendee);
        getSupportActionBar().setTitle(null);

        //side menu navigation
        navigationViewAttendee.bringToFront();
        actionBarDrawerToggleViewAttendee = new ActionBarDrawerToggle(this,drawerLayoutViewAttendee,toolbarViewAttendee,R.string.open,R.string.close);
        drawerLayoutViewAttendee.addDrawerListener(actionBarDrawerToggleViewAttendee);
        actionBarDrawerToggleViewAttendee.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggleViewAttendee.syncState();
        navigationViewAttendee.setNavigationItemSelectedListener(this);
        //

        Intent intent = getIntent();

        organizer_key = intent.getStringExtra("organizer_key");
        event_key = intent.getStringExtra("event_key");

        recyclerView = (RecyclerView) findViewById(R.id.eventAttendeeRecycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        attendeeController.getEventAtteendees(event_key, new GetEventAttendeeInterface() {
            @Override
            public void onGetEventAttendees(List<Attendee> attendees) {
                if(attendees != null){
                    InstantiateAdapter(attendees);
                }
            }
        });
    }

    private void InstantiateAdapter(final List<Attendee> attendees){
        if (attendees != null) {
            mAdapter = new AttendeeAdapter(attendees, new CustomEventClickListener() {
                @Override
                public void onItemClick(View v, int i) {
                    Log.i("attendee first name", attendees.get(i).getFirst_name());
                }
            });
        }

        recyclerView.setAdapter(mAdapter);
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.dashboard:
                Intent dashboardintent = new Intent (EventAttendeeActivity.this,Dashboard.class);
                dashboardintent.putExtra("organizer_key", organizer_key);
                startActivity(dashboardintent);
                drawerLayoutViewAttendee.closeDrawers();
                break;
            case R.id.addEvent:
                Intent intent = new Intent (EventAttendeeActivity.this,addEvent.class);
                intent.putExtra("organizer_key", organizer_key);
                startActivity(intent);
                drawerLayoutViewAttendee.closeDrawers();
                break;
            case R.id.viewEvent:
                Intent ViewEventIntent = new Intent (EventAttendeeActivity.this,EventActivity.class);
                ViewEventIntent.putExtra("organizer_key", organizer_key);
                startActivity(ViewEventIntent);
                drawerLayoutViewAttendee.closeDrawers();
                break;
            case R.id.Logout:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Going Away?")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent (EventAttendeeActivity.this,LoginPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                EventAttendeeActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            case R.id.AboutUs:
                Intent intentAbout = new Intent (EventAttendeeActivity.this,EventActivity.class);
                intentAbout.putExtra("organizer_key", organizer_key);
                drawerLayoutViewAttendee.closeDrawers();
                startActivity(intentAbout);
                break;
        }
        return true;
    }
}
