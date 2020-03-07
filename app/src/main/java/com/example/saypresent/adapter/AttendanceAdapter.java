package com.example.saypresent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.saypresent.R;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.utils.CustomEventClickListener;

import java.util.List;

public class AttendanceAdapter extends Adapter<AttendanceAdapter.ViewHolder> {

    private List<Attendee> attendees;
    private CustomEventClickListener customEventClickListener;

    public AttendanceAdapter(List<Attendee> attendees, CustomEventClickListener customEventClickListener){
        this.attendees = attendees;
        this.customEventClickListener = customEventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.checkpoint_attendee_list, parent, false);
        final AttendanceAdapter.ViewHolder viewHolder = new AttendanceAdapter.ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customEventClickListener.onItemClick(view, viewHolder.getPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attendee attendee = attendees.get(position);
        String attendee_name = attendee.getFirst_name() + " " + attendee.getLast_name();
        String timestamp = attendee.getTimestamp();
        holder.attendee_name.setText(attendee_name);
        holder.timestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return attendees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView attendee_name;
        private TextView timestamp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            attendee_name = (TextView) itemView.findViewById(R.id.attendee_name);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }
    }

}
