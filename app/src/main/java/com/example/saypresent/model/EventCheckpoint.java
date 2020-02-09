package com.example.saypresent.model;

import java.util.List;

public class EventCheckpoint {

    private String checkpoint_name;
    private String checkpoint_location;
    private String checkpoint_details;
    private String checkpoint_key;
    private String event_key;

    public EventCheckpoint(){}

    public EventCheckpoint(String checkpoint_name, String checkpoint_location, String checkpoint_details){
        this.checkpoint_name = checkpoint_name;
        this.checkpoint_location = checkpoint_location;
        this.checkpoint_details = checkpoint_details;
    }

    public EventCheckpoint(String checkpoint_name, String checkpoint_location, String checkpoint_details, String checkpoint_key){
        this.checkpoint_name = checkpoint_name;
        this.checkpoint_location = checkpoint_location;
        this.checkpoint_details = checkpoint_details;
        this.checkpoint_key = checkpoint_key;
    }

    public void setCheckpoint_name(String checkpoint_name) {
        this.checkpoint_name = checkpoint_name;
    }

    public void setCheckpoint_location(String checkpoint_location) {
        this.checkpoint_location = checkpoint_location;
    }

    public void setCheckpoint_details(String checkpoint_details) {
        this.checkpoint_details = checkpoint_details;
    }

    public String getEvent_key() {
        return event_key;
    }

    public void setEvent_key(String event_key) {
        this.event_key = event_key;
    }

    public String getCheckpoint_name() {
        return checkpoint_name;
    }

    public String getCheckpoint_location() {
        return checkpoint_location;
    }

    public String getCheckpoint_details() {
        return checkpoint_details;
    }

    public String getCheckpoint_key() {
        return checkpoint_key;
    }

    public void setCheckpoint_key(String checkpoint_key) {
        this.checkpoint_key = checkpoint_key;
    }
}
