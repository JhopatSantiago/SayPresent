package com.example.saypresent.model;

public class Organizer {
    private String first_name;
    private String middle_name;
    private String last_name;
    private String email;
    private String password;

    public Organizer(){

    }

    public Organizer(String first_name, String middle_name, String last_name, String email, String password){
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
