package com.example.saypresent.model;

public class Event {
    public String event_name;
    public String location;
    public String details;

    public Event(String event_name, String location, String details){
        this.event_name = event_name;
        this.location = location;
        this.details = details;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getLocation() {
        return location;
    }

    public String getDetails() {
        return details;
    }
}
