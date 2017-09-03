package com.example.amanda.friendtrackerappass1.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by amanda on 3/09/2017.
 */

public class MeetingManager implements Serializable{
    private Friend friend;
    private Meeting meeting;
    private ArrayList<Meeting> meetingList;

    public MeetingManager()
    {
        meetingList = new ArrayList<Meeting>();
    }

    public void scheduleMeeting(Meeting meeting)
    {
        meetingList.add(meeting);
    }

    public void unscheduleMeeting(Meeting meeting)
    {
        meetingList.remove(meeting);
    }

    public ArrayList<Meeting> getList()
    {
        return meetingList;
    }

    public Meeting findMeeting(String id)
    {
        for(Meeting m: meetingList)
        {
            if(m.getID().equals(id))
            {
                return m;
            }
        }
        return null;
    }
}
