package com.linedown.backend;

interface LineupUpdater {
    
    boolean addWaitTime (String ID, int waitTime);
    
    boolean updateUserLocation (String ID, int isNearby);   
    
    public void recalculateBaseDataSet(String ID);
    
}
