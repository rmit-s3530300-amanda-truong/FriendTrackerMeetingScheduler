package com.example.amanda.friendtrackerappass1.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.View.DisplayMeetingActivity;

import java.util.ArrayList;

/**
 * Created by amanda on 5/10/2017.
 */

public class RemoveMeetingAsync extends AsyncTask<Meeting, Void, Void> {

    private DisplayMeetingActivity activity;
    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private String LOG_TAG = this.getClass().getName();
    private DBHandler db;

    public RemoveMeetingAsync(Activity activity, FriendManager friendManager, MeetingManager meetingManager)
    {
        super();
        this.activity = (DisplayMeetingActivity) activity;
        this.friendManager = friendManager;
        this.meetingManager = meetingManager;
        db = new DBHandler(activity);
    }

    @Override
    protected Void doInBackground(Meeting... meeting) {
        meetingManager.unscheduleMeeting(meeting[0]);
        db.removeMeeting(meeting[0]);
        String table = db.getTableAsString("meeting");
        Log.i(LOG_TAG, table);
        db.close();
        return null;
    }
}