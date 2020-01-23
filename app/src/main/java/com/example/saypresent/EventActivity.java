package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.saypresent.adapter.EventsAdapter;
import com.example.saypresent.controller.EventController;
import com.example.saypresent.model.Event;
import com.example.saypresent.utils.CustomEventClickListener;
import com.example.saypresent.utils.GetEventsInterface;

import java.util.List;

public class EventActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<EventActivity> events;
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
        mAdapter = new EventsAdapter(events);
        recyclerView.setAdapter(mAdapter);
    }
}
