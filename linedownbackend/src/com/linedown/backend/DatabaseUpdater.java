package com.linedown.backend;

import java.util.*;
import java.text.SimpleDateFormat;

public class DatabaseUpdater implements LineupUpdater {
    
    public DatabaseUpdater() {
    }
    
    
    public boolean addWaitTime (String ID, int waitTime) {
        Date date = new Date();
        int currentTimestamp = (int) (date.getTime()/60000);    // in minutes

        UserInputDAO dao = new UserInputDAO();        
        List<UserInput> inputs = dao.getUserInputFor(ID);
        
        boolean success = false;
        
        if (inputs.size() != 0) {
            success = dao.updateUserInput(inputs.get(0), waitTime, currentTimestamp);
        }
        
        if (success) {
            recalculateEstWaitTime(ID);
        }
        
        return success;
        
    }
    
    public boolean updateUserLocation (String ID, int isNearby) {
        UserLocationsDAO dao = new UserLocationsDAO();
        UserLocations locations = dao.getUserLocations(ID);
        
        int changeInUsersNearby = 0;
        
        if (isNearby == 1) {
            changeInUsersNearby = 1;
        } 
        else if (isNearby == 0) {
            changeInUsersNearby = -1;
        }
        
        if (dao.updateUserLocations(locations,locations.getPeopleNearby()+changeInUsersNearby)) {
            return true;
        }
        
        return false;
        
    }   
    
    
    private void recalculateEstWaitTime(String ID) {
        WaitTimeCalculator calculator = new CalculatorWithoutLocationTracking();
        int newWaitTime = calculator.calculateWaitTime(ID);
        Date date = new Date();
        int currentTimestamp = (int) (date.getTime()/60000);    // in minutes
        
        WaitTime w = new WaitTime();
        w.setID(ID);
        WaitTimeDAO dao = new WaitTimeDAO();
        
        dao.updateWaitTime(w, newWaitTime, currentTimestamp);
    
    }
    
    public void recalculateBaseDataSet(String ID) {
        BaseDataSetDAO bdsDao = new BaseDataSetDAO();
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        int currentTimestampHour = (int) (date.getTime()/3600000);
        int currentTimestampDay = (int) (currentTimestampHour/24); 
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        String dayOfWeek = simpleDateFormat.format(date);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        
        WaitTimeDAO wtDao = new WaitTimeDAO();
        WaitTime waitTime = wtDao.getWaitTime(ID);
        
        UserLocationsDAO ulDao = new UserLocationsDAO();
        UserLocations locations = ulDao.getUserLocations(ID);
        
        
        BaseDataSet bds = bdsDao.getBaseDataSet(ID, dayOfWeek, hourOfDay);
        
        int daysTracked = currentTimestampDay - bds.getDayTrackedSince();
        
        double updatedWaitTime = (1.5*waitTime.getCurrentWaitTime() + daysTracked*bds.getWaitTime()) / (daysTracked+1);
        
        double updatedAvgPeopleNearby = (1.5*(double)locations.getPeopleNearby() + daysTracked*bds.getAvgPeopleNearby() / (daysTracked+1));
        
        bdsDao.updateBaseDataSet(bds,updatedWaitTime,updatedAvgPeopleNearby);
        
    }
    
    
}
