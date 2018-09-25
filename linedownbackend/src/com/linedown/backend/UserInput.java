package com.linedown.backend;


public class UserInput {

    private String ID;
    private String PlaceID;
    private int WaitTime;
    private int TimeInput;

    public UserInput () {
    }
    
    public UserInput (String placeID, int waitTime, int timeInput) {
        this.ID = placeID + timeInput + waitTime;
        this.PlaceID = placeID;
        this.WaitTime = waitTime;
        this.TimeInput = timeInput;
    }

    public void setID(String placeID) {
        this.PlaceID = placeID;
        this.ID = this.PlaceID + this.TimeInput + this.WaitTime;
    }
    
    public void setWaitTime(int waitTime) {
        this.WaitTime = waitTime;
    }
    
    public void setTimeInput(int timeInput){
        this.TimeInput = timeInput;
        this.ID = this.PlaceID + this.TimeInput + this.WaitTime;
    }
    
    public String getID() {
        return this.PlaceID;
    }
    
    public int getWaitTime() {
        return this.WaitTime;
    }
    
    public int getTimeInput(){
        return this.TimeInput;
    }
    
}
