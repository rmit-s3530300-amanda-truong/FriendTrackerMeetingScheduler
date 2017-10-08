package com.example.amanda.friendtrackerappass1.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Amanda on 8/10/2017.
 */

public class MeetingNotificationService extends Service {

    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private int upComing;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent myIntent = intent;

        friendManager = (FriendManager) myIntent.getExtras().getSerializable(getResources().getString(R.string.friendManager));
        meetingManager = (MeetingManager) myIntent.getExtras().getSerializable(getResources().getString(R.string.meetingManager));
        upComing = (int) myIntent.getExtras().getInt(getResources().getString(R.string.upcomingMeetings));

        createNotification();
        return flags;
    }

    public void createNotification()
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pIntent);

        mBuilder.setSmallIcon(R.drawable.ic_assignment_black_24dp);
        mBuilder.setContentTitle(getResources().getString(R.string.notif));
        mBuilder.setContentText(getResources().getString(R.string.notifText));
        mBuilder.addAction(R.drawable.ic_cancel_black_24dp, getResources().getString(R.string.cancel), pIntent);
        mBuilder.addAction(R.drawable.ic_close_black_24dp, getResources().getString(R.string.dismiss), pIntent);
        mBuilder.addAction(R.drawable.ic_add_alert_black_24dp, getResources().getString(R.string.remindMe), pIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());
    }

}
