package com.linedown.backend;

import java.util.*;
import java.text.SimpleDateFormat;

public class CalculatorWithLocationTracking implements WaitTimeCalculator {
 
    public CalculatorWithLocationTracking() {
    }
    
    public int getWaitTime (String ID){
        WaitTime dbWaitTime = new WaitTime(ID);
        
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int currentTimestamp = (int) (date.getTime()/60000);    // in minutes
        int timeSinceLastUpdated = currentTimestamp - dbWaitTime.getLastUpdated();
        
        if (timeSinceLastUpdated > 60) {
            timeSinceLastUpdated = 60;
        }
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        String dayOfWeek = simpleDateFormat.format(date);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        
        BaseDataSetDAO bdsDao = new BaseDataSetDAO();
        BaseDataSet bdsWaitTime = bdsDao.getBaseDataSet(ID,dayOfWeek,hourOfDay);
        UserLocationsDAO ulDao = new UserLocationsDAO();
        UserLocations locations = ulDao.getUserLocations(ID);
        
        double estWaitTime = ( ( ((60-timeSinceLastUpdated) * dbWaitTime.getCurrentWaitTime()
                             * (locations.getPeopleNearby() / bdsWaitTime.getAvgPeopleNearby()) ) / 60 ) +
                               ( (timeSinceLastUpdated * bdsWaitTime.getWaitTime()) / 60) 
                             )  
                             + 0.5; 
         
        return (int) estWaitTime;
    }
    
    public int calculateWaitTime (String ID) {
        UserInputDAO dao = new UserInputDAO();
        List<UserInput> inputs = dao.getUserInputFor(ID);
        
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int currentTimestamp = (int) (date.getTime()/60000);    // in minutes
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        String dayOfWeek = simpleDateFormat.format(date);
        int hourOfDay = (int) ((double) calendar.get(Calendar.HOUR_OF_DAY) - 2.5);
        
        BaseDataSetDAO bdsDao = new BaseDataSetDAO();
        BaseDataSet bdsWaitTime = bdsDao.getBaseDataSet(ID,dayOfWeek,hourOfDay);
        
        double bdsTime = bdsWaitTime.getWaitTime();
        int sum = (int) bdsTime;
        for (UserInput u: inputs) {
            sum += u.getWaitTime();
        }
        
        double mean = sum/(inputs.size()+1);       

        double[] weights = new double[inputs.size()+1];
        double sumOfWeights = weights[0];
        weights[0] = 1/(Math.pow(bdsTime-mean,2)+1);
        for (int i=0; i<inputs.size(); i++) { 
            double accuracyFactor = Math.pow((inputs.get(i).getWaitTime()-mean),2) + 0.1;
            double currentnessFactor = Math.abs(currentTimestamp-inputs.get(i).getTimeInput()) + 0.1;
            weights[i+1] = 1/(accuracyFactor*currentnessFactor + 0.9); 
            sumOfWeights += weights[i+1];
        }
        
        double weightedSum = bdsTime*weights[0];
        for (int i = 0; i<inputs.size(); i++) {
            weightedSum += inputs.get(i).getWaitTime()*weights[i+1];
        }

       double weightedMean = weightedSum/(sumOfWeights);
        
       return (int) weightedMean;
       
    }
    
}
