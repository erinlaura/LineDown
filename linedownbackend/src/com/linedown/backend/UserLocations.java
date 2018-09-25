package com.linedown.backend;

public class UserLocations {

    private String ID;
    private int PeopleNearby;
    
    public UserLocations() {
    }
    
    public UserLocations (String placeID, int peopleNearby) {
        this.ID = placeID;
        this.PeopleNearby = peopleNearby; 
    }
    
    public void setID(String placeID) {
        this.ID = placeID;
    }
    
    public void setPeopleNearby(int peopleNearby) {
        this.PeopleNearby = peopleNearby;
    }
    
    public String getID() {
        return ID;
    }
    
    public int getPeopleNearby() {
        return PeopleNearby;
    }

}
