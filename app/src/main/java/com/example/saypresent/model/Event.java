package com.example.saypresent.model;

public class Event {
    public String event_name;
    public String location;
    public String details;
    public String event_key;
    public String event_date;
    public String organizer_key;

    public Event(){

    }
    public Event(String event_name, String location, String details, String event_key){
        this.event_name = event_name;
        this.location = location;
        this.details = details;
        this.event_key = event_key;
    }

    public Event(String event_name, String location, String details, String event_date, String event_key){
        this.event_name = event_name;
        this.location = location;
        this.details = details;
        this.event_key = event_key;
        this.event_date = event_date;
    }

    public String getOrganizer_key() {
        return organizer_key;
    }

    public void setOrganizer_key(String organizer_key) {
        this.organizer_key = organizer_key;
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

    public String getEvent_key(){
        return  event_key;
    }

    public void setEvent_key(String event_key){
        this.event_key = event_key;
    }
}
