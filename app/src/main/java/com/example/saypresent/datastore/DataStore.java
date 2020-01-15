package com.example.saypresent.datastore;

import java.io.Serializable;

public class DataStore implements Serializable {
    private String organizer_key = "";

    public void setOrganizer_key(String organizer_key){
        this.organizer_key = organizer_key;
    }

    public String getOrganizer_key() {
        return organizer_key;
    }
}
