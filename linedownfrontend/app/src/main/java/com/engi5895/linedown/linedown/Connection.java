package com.engi5895.linedown.linedown;

/**
 * Created by jonathan on 04/03/18.
 */

public interface Connection {

    void getWaitTime(String ID);

    void addWaitTime(String ID, int waitTime);

    void nearbyRestaurant(String ID, boolean isNearby);
}
