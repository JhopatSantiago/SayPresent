package com.example.saypresent.model;

public class Attendee {
    private String first_name;
    private String middle_name;
    private String last_name;
    private String contact_number;
    private String emergency_number;
    private String email;
    private String password;
    private String attendee_key;
    private String event_key;
    private String checkpoint_key;
    private String timestamp;


    public Attendee(String first_name, String middle_name, String last_name, String email, String password){
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public Attendee(String first_name, String middle_name, String last_name, String email, String password, String timestamp){
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.password = password;
        this.email = email;
        this.timestamp = timestamp;
    }

    public Attendee(){

    }

    public void removePassword(){
        this.password = null;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCheckpoint_key() {
        return checkpoint_key;
    }

    public void setCheckpoint_key(String checkpoint_key) {
        this.checkpoint_key = checkpoint_key;
    }

    public String getEvent_key() {
        return event_key;
    }

    public void setEvent_key(String event_key) {
        this.event_key = event_key;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getEmergency_number() {
        return emergency_number;
    }

    public void setEmergency_number(String emergency_number) {
        this.emergency_number = emergency_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAttendee_key() {
        return attendee_key;
    }

    public void setAttendee_key(String attendee_key) {
        this.attendee_key = attendee_key;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
