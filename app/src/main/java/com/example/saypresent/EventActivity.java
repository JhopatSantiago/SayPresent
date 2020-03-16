package com.example.saypresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saypresent.adapter.EventsAdapter;
import com.example.saypresent.controller.EventController;
import com.example.saypresent.model.Event;
//import com.example.saypresent.utils.CustomEventClickListener;
import com.example.saypresent.utils.CustomEventClickListener;
import com.example.saypresent.utils.GetEventsInterface;
import com.example.saypresent.utils.LongClickListener;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

/**
 * Displays all events created by the organizer
 */
public class EventActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Event> events;
    private GetEventsInterface getEventsInterface;
    private EventController eventController;
    private String organizer_key;
    private DrawerLayout drawerLayoutEvents;
    private Toolbar toolbarEvents;
    private NavigationView navigationViewEvents;
    private ActionBarDrawerToggle actionBarDrawerToggleEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        drawerLayoutEvents = findViewById(R.id.drawerlayout_event);
        toolbarEvents = findViewById(R.id.toolbar_event);
        navigationViewEvents = findViewById(R.id.nav_view_events);

        //
        navigationViewEvents.bringToFront();
        actionBarDrawerToggleEvent = new ActionBarDrawerToggle(this,drawerLayoutEvents,toolbarEvents,R.string.open,R.string.close);
        drawerLayoutEvents.addDrawerListener(actionBarDrawerToggleEvent);
        actionBarDrawerToggleEvent.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggleEvent.syncState();
        navigationViewEvents.setNavigationItemSelectedListener(this);
        navigationViewEvents.setCheckedItem(R.id.viewEvent);
        //
        Intent intent = getIntent();
        organizer_key = intent.getStringExtra("organizer_key");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        eventController = new EventController();

        getEventsInterface = new GetEventsInterface() {
            @Override
            public void onGetEvents(List<Event> events) {
                instantiateAdapter(events);
            }
        };

        eventController.getEvents(organizer_key, getEventsInterface);
    }
    public void instantiateAdapter(final List<Event> events){
        CustomEventClickListener clickListener = new CustomEventClickListener() {
            @Override
            public void onItemClick(View v, int i) {
                String event_key = events.get(i).getEvent_key();
                Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
                intent.putExtra("organizer_key", organizer_key);
                intent.putExtra("event_key", event_key);
                startActivity(intent);
            }
        };

        LongClickListener longClickListener = new LongClickListener() {
            @Override
            public void onLongClick(int i, String action) {
                Log.i("event_name", events.get(i).getEvent_name());
                Log.i("action", action);
            }
        };

        mAdapter = new EventsAdapter(events, clickListener, longClickListener,EventActivity.this);
        recyclerView.setAdapter(mAdapter);
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.dashboard:
                Intent dashboardintent = new Intent (EventActivity.this,Dashboard.class);
                dashboardintent.putExtra("organizer_key", organizer_key);
                startActivity(dashboardintent);
                drawerLayoutEvents.closeDrawers();
                break;
            case R.id.addEvent:
                Intent intent = new Intent (EventActivity.this,addEvent.class);
                intent.putExtra("organizer_key", organizer_key);
                startActivity(intent);
                drawerLayoutEvents.closeDrawers();
                break;
            case R.id.viewEvent:
                break;
            case R.id.Logout:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Going Away?")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent (EventActivity.this,LoginPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                EventActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            case R.id.AboutUs:
                Intent intentAbout = new Intent (EventActivity.this,EventActivity.class);
                intentAbout.putExtra("organizer_key", organizer_key);
                drawerLayoutEvents.closeDrawers();
                startActivity(intentAbout);
                break;
        }
        return true;
    }
}
