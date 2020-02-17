package com.example.saypresent.model;

public class Attendance {
    private String checkpoint_key;
    private String attendee_key;
    private String timestamp;

    public Attendance(String checkpoint_key, String attendee_key, String timestamp){
        this.checkpoint_key = checkpoint_key;
        this.attendee_key = attendee_key;
        this.timestamp = timestamp;
    }

    public Attendance(String attendee_key, String timestamp){
        this.attendee_key = attendee_key;
        this.timestamp = timestamp;
    }

    public String getCheckpoint_key() {
        return checkpoint_key;
    }

    public void setCheckpoint_key(String checkpoint_key) {
        this.checkpoint_key = checkpoint_key;
    }

    public String getAttendee_key() {
        return attendee_key;
    }

    public void setAttendee_key(String attendee_key) {
        this.attendee_key = attendee_key;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
