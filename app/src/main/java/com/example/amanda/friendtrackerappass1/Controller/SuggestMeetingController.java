package com.example.amanda.friendtrackerappass1.Controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.amanda.friendtrackerappass1.AsyncTask.SuggestNowAsync;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.AddMeetingActivity;
import com.example.amanda.friendtrackerappass1.View.MainActivity;
import com.example.amanda.friendtrackerappass1.View.MapsActivity;
import com.example.amanda.friendtrackerappass1.View.SuggestMeetingActivity;

/**
 * Created by Amanda on 7/10/2017.
 */

public class SuggestMeetingController implements View.OnClickListener{

    private SuggestMeetingActivity activity;
    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private String location;

    public SuggestMeetingController(Activity activity, FriendManager friendManager, MeetingManager meetingManager)
    {
        this.activity = (SuggestMeetingActivity) activity;
        this.friendManager = friendManager;
        this.meetingManager = meetingManager;
        location = ((SuggestMeetingActivity) activity).getLocation();
    }

    @Override
    public void onClick(View view) {
        int buttonClicked = view.getId();
        if(buttonClicked == R.id.btShowLocation)
        {
            Intent intent = new Intent(activity, MapsActivity.class);
            activity.startActivity(intent);
        }
        else if(buttonClicked == R.id.btAccept)
        {
            Intent intent = new Intent(activity, AddMeetingActivity.class);
            intent.putExtra(activity.getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(activity.getResources().getString(R.string.meetingManager), meetingManager);
            intent.putExtra(activity.getResources().getString(R.string.suggestNow), activity.getCurrentInfo());
            intent.putExtra(activity.getResources().getString(R.string.current), activity.getCurrentDate());
            intent.putExtra(activity.getResources().getString(R.string.className), activity.getResources().getString(R.string.suggest));
            activity.startActivity(intent);
        }
        else if(buttonClicked == R.id.btDecline)
        {
            activity.incrementCounter();
        }
        else if(buttonClicked == R.id.btCancel)
        {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra(activity.getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(activity.getResources().getString(R.string.meetingManager), meetingManager);
            activity.startActivity(intent);
        }
    }

    public void calculateMid()
    {
         SuggestNowAsync async = new SuggestNowAsync(activity, friendManager, meetingManager);
         async.execute(location);
    }
}
