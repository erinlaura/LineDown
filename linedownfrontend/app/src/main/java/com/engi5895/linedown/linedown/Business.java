package com.engi5895.linedown.linedown;

/**
 * Created by erinfitzgerald on 2018-03-04.
 *
 */

public interface Business {
    public String getName();
    public String getAddress();
    public Double getLat();
    public Double getLon();
    public String getPlaceID();
    public int getWaitTime();
}
