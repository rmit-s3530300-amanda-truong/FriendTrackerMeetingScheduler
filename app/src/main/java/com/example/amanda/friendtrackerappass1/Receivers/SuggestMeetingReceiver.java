package com.example.amanda.friendtrackerappass1.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.SuggestMeetingActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Amanda on 10/10/2017.
 */

public class SuggestMeetingReceiver extends BroadcastReceiver{

    private String location;
    private Boolean checkNetworkState;
    private FriendManager friendManager;
    private MeetingManager meetingManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        friendManager = (FriendManager) intent.getExtras().getSerializable(context.getResources().getString(R.string.friendManager));
        meetingManager = (MeetingManager) intent.getExtras().getSerializable(context.getResources().getString(R.string.meetingManager));
        location = (String) intent.getExtras().getString(context.getResources().getString(R.string.location));
        checkNetworkState = (Boolean) intent.getExtras().get(context.getResources().getString(R.string.networkInfo));
        Intent meetingIntent = new Intent(context, SuggestMeetingActivity.class);
        meetingIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        meetingIntent.putExtra(context.getResources().getString(R.string.friendManager), friendManager);
        meetingIntent.putExtra(context.getResources().getString(R.string.meetingManager), meetingManager);
        meetingIntent.putExtra(context.getResources().getString(R.string.location), location);
        meetingIntent.putExtra(context.getResources().getString(R.string.className), context.getResources().getString(R.string.suggestNow));
        context.startActivity(meetingIntent);
    }
}
