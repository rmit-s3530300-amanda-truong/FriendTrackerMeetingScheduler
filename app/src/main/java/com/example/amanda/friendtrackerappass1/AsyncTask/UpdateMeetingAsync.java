package com.example.amanda.friendtrackerappass1.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.EditMeetingActivity;

import java.util.ArrayList;

/**
 * Created by amanda on 5/10/2017.
 */

public class UpdateMeetingAsync extends AsyncTask<Void, Void, Void> {

    private EditMeetingActivity activity;
    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private String LOG_TAG = this.getClass().getName();
    private DBHandler db;

    public UpdateMeetingAsync(Activity activity, FriendManager friendManager, MeetingManager meetingManager)
    {
        super();
        this.activity = (EditMeetingActivity) activity;
        this.friendManager = friendManager;
        this.meetingManager = meetingManager;
        db = new DBHandler(activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ArrayList<String> values = activity.saveValues();
        saveMeeting(values);
        return null;
    }

    @Override
    protected void onPostExecute(Void voids)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(activity.getResources().getString(R.string.savedInfo));
        alert.setMessage(activity.getResources().getString(R.string.savedMessaged));
        alert.setPositiveButton(activity.getResources().getString(R.string.okay), null);
        alert.show();
    }

    public void saveMeeting(ArrayList<String> meetingInfo)
    {
        Meeting meeting = meetingManager.findMeeting(meetingInfo.get(0));
        meeting.editTitle(meetingInfo.get(1));
        meeting.editStartDate(meetingInfo.get(2));
        meeting.editEndDate(meetingInfo.get(3));
        String[] locationSplit = meetingInfo.get(4).split(":");
        meeting.editLocation(locationSplit[0], locationSplit[1]);
        ArrayList<Friend> invitedFriends = activity.getInvitedFriends();
        meeting.editInvitedFriends(invitedFriends);
        db.updateMeeting(meeting);
        //testing
        String table = db.getTableAsString("meeting");
        Log.i(LOG_TAG, table);
        db.close();
    }

}
