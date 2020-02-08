package com.example.saypresent.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saypresent.EventActivity;
import com.example.saypresent.R;
import com.example.saypresent.model.Event;
import com.example.saypresent.utils.CustomEventClickListener;
import com.example.saypresent.utils.LongClickListener;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private List<Event> events;
    private CustomEventClickListener listener;
    private Context context;
    private LongClickListener longClickListener;



    public EventsAdapter(List<Event> events, CustomEventClickListener listener, LongClickListener longClickListener, Context context) {
        this.events = events;
        this.listener = listener;
        this.context = context;
        this.longClickListener = longClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.events_list, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, vh.getPosition());
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] items = {"Delete", "Update"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String action = items[i];
                        longClickListener.onLongClick(vh.getPosition(), action);
                    }
                });
                builder.show();
                return true;
            }
        });

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
