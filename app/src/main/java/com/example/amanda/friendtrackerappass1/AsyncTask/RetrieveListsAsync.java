package com.example.amanda.friendtrackerappass1.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.View.MainActivity;

import java.util.ArrayList;

/**
 * Created by amanda on 5/10/2017.
 */

public class RetrieveListsAsync extends AsyncTask<Void, Void, Void> {

    private MainActivity activity;
    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private String LOG_TAG = this.getClass().getName();

    public RetrieveListsAsync(MainActivity activity, FriendManager friendManager, MeetingManager meetingManager)
    {
        super();
        this.activity = activity;
        this.friendManager = friendManager;
        this.meetingManager = meetingManager;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DBHandler db = new DBHandler(activity);
        if(friendManager.getFriendList().size() == 0)
        {
            ArrayList<Friend> friendList = db.getAllFriends();
            if(friendList.size() > 0)
            {
                friendManager.setFriendList(friendList);
                Log.i(LOG_TAG, "friendList set");
            }
        }
        if(meetingManager.getList().size() == 0)
        {
            ArrayList<Meeting> meetingList = db.getAllMeetings();
            if(meetingList.size() > 0)
            {
                meetingManager.setList(meetingList);
                Log.i(LOG_TAG, "meetingList set");
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void voids)
    {
        activity.setFriendManager(friendManager);
        activity.setMeetingManager(meetingManager);
        Log.i(LOG_TAG, friendManager.getFriendList().size()+ "friendManager");
    }
}
