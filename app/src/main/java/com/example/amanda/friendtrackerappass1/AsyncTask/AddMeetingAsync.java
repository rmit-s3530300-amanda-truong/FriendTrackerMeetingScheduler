package com.example.amanda.friendtrackerappass1.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.View.AddMeetingActivity;

import java.util.ArrayList;

/**
 * Created by amanda on 5/10/2017.
 */

public class AddMeetingAsync extends AsyncTask<String, Void, Void> {

    private AddMeetingActivity activity;
    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private String LOG_TAG = this.getClass().getName();
    private DBHandler db;

    public AddMeetingAsync(Activity activity, FriendManager friendManager, MeetingManager meetingManager)
    {
        super();
        this.activity = (AddMeetingActivity) activity;
        this.friendManager = friendManager;
        this.meetingManager = meetingManager;
        db = new DBHandler(activity);
    }

    @Override
    protected Void doInBackground(String... param) {
        ArrayList<Friend> invitedFriends = activity.getInvitedFriends();
        Meeting meeting = new Meeting(param[0], param[1], param[2], param[3],
                invitedFriends, param[4]);
        meetingManager.scheduleMeeting(meeting);
        db.createMeeting(meeting);
        //testing
        String table = db.getTableAsString("meeting");
        Log.i(LOG_TAG, table);
        db.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void voids)
    {
        activity.goToMeetingList();
    }
}
