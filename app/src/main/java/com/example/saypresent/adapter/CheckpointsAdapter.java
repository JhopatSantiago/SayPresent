package com.example.saypresent.adapter;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saypresent.R;
import com.example.saypresent.model.Event;
import com.example.saypresent.model.EventCheckpoint;
import com.example.saypresent.utils.CustomEventClickListener;

import org.w3c.dom.Text;

import java.util.List;

public class CheckpointsAdapter  extends RecyclerView.Adapter<CheckpointsAdapter.ViewHolder> {
    private List<EventCheckpoint> eventCheckpoints;
    private CustomEventClickListener customEventClickListener;

    public CheckpointsAdapter(List<EventCheckpoint> eventCheckpoints, CustomEventClickListener customEventClickListener){
        this.eventCheckpoints = eventCheckpoints;
        this.customEventClickListener = customEventClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.checkpoint_list, parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               customEventClickListener.onItemClick(view, viewHolder.getPosition());
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final EventCheckpoint eventCheckpoint = eventCheckpoints.get(position);
        holder.checkpointLocationField.setText(eventCheckpoint.getCheckpoint_location());
        holder.checkpointNameField.setText(eventCheckpoint.getCheckpoint_name());
    }

    @Override
    public int getItemCount() {
        return eventCheckpoints.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView checkpointNameField;
        public TextView checkpointLocationField;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkpointNameField = (TextView) itemView.findViewById(R.id.checkpoint_name);
            checkpointLocationField = (TextView) itemView.findViewById(R.id.checkpoint_location);
        }
    }

}
