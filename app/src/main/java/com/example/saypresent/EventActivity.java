package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.saypresent.adapter.EventsAdapter;
import com.example.saypresent.controller.EventController;
import com.example.saypresent.model.Event;
//import com.example.saypresent.utils.CustomEventClickListener;
import com.example.saypresent.utils.CustomEventClickListener;
import com.example.saypresent.utils.GetEventsInterface;
import com.example.saypresent.utils.LongClickListener;

import java.util.List;

/**
 * Displays all events created by the organizer
 */
public class EventActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Event> events;
    private GetEventsInterface getEventsInterface;
    private EventController eventController;
    private String organizer_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

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
}
