package com.example.saypresent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saypresent.EventActivity;
import com.example.saypresent.R;
import com.example.saypresent.model.Event;
//import com.example.saypresent.utils.CustomEventClickListener;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private List<Event> events;
//    private CustomEventClickListener listener;



    public EventsAdapter(List<Event> events) {
        this.events = events;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.events_list, parent, false);
        final ViewHolder vh = new ViewHolder(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(v, vh.getPosition());
//            }
//        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Event event = events.get(position);
        holder.event_name.setText(event.getEvent_name());
        holder.event_location.setText(event.getLocation());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView event_name;
        public TextView event_location;
        public View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            event_name = (TextView) itemView.findViewById(R.id.event_name);
            event_location = (TextView) itemView.findViewById(R.id.event_location);
        }
    }
}
