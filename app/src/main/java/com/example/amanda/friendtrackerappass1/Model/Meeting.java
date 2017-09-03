package com.example.amanda.friendtrackerappass1.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by amanda on 2/09/2017.
 */

public class Meeting implements Serializable{
    private String id;
    private String title;
    private String startDate;
    private String endDate;
    private ArrayList<Friend> invitedFriends;
    private String location;

    public Meeting(String id, String title, String startDate, String endDate, ArrayList<Friend> invitedFriends, String location)
    {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.invitedFriends = invitedFriends;
        this.location = location;
    }

    public String getID()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public ArrayList<Friend> getInvitedFriends()
    {
        return invitedFriends;
    }

    public String getLocation()
    {
        return location;
    }

    public void editTitle(String title)
    {
        this.title = title;
    }

    public void editStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public void editEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public void editInvitedFriends(ArrayList<Friend> invitedFriends)
    {
        this.invitedFriends = invitedFriends;
    }

    public void editLocation(String lat, String lon)
    {
        this.location = lat + ":" + lon;
    }
}
