package com.example.amanda.friendtrackerappass1.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.Controller.MeetingController;
import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddMeetingActivity extends AppCompatActivity {

    private String LOG_TAG = this.getClass().getName();
    private FriendManager friendManager;
    private MeetingController meetingController;
    private MeetingManager meetingManager;
    private ArrayList<Friend> invitedFriends;
    private EditText etMeetingTitle;
    private TextView tvStartDate;
    private TextView tvStartTime;
    private TextView tvEndDate;
    private TextView tvEndTime;
    private TextView tvInviteFriends;
    private EditText etLatitude;
    private EditText etLongitude;
    private String title;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String lat;
    private String lon;
    private String location;
    private String currentInfo;
    private String className;
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        etMeetingTitle = (EditText) findViewById(R.id.etMeetingTitle);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        tvInviteFriends = (TextView) findViewById(R.id.tvInvitedFriends);
        Button btEndDate = (Button) findViewById(R.id.btEndDate);
        etLatitude = (EditText) findViewById(R.id.etLatitude);
        etLongitude = (EditText) findViewById(R.id.etLongitude);

        currentDate = Calendar.getInstance().getTime();

        Bundle contactInfo = getIntent().getExtras();
        if(contactInfo != null)
        {
            friendManager = (FriendManager) contactInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) contactInfo.getSerializable(getResources().getString(R.string.meetingManager));
            invitedFriends = (ArrayList<Friend>) contactInfo.getSerializable(getResources().getString(R.string.invite));
            title = (String) contactInfo.getString(getResources().getString(R.string.title));
            startDate = (String) contactInfo.getString(getResources().getString(R.string.startDate));
            startTime = (String) contactInfo.getString(getResources().getString(R.string.startTime));
            endTime = (String) contactInfo.getString(getResources().getString(R.string.endTime));
            endDate = (String) contactInfo.getString(getResources().getString(R.string.endDate));
            lat = (String) contactInfo.getString(getResources().getString(R.string.latitude));
            lon = (String) contactInfo.getString(getResources().getString(R.string.longitude));
            location = (String) contactInfo.getString(getResources().getString(R.string.location));
            className = (String) contactInfo.getString(getResources().getString(R.string.className));
            if(className != null)
            {
                if(className.equals(getResources().getString(R.string.suggest)))
                {
                    currentInfo = (String) contactInfo.getString(getResources().getString(R.string.suggestNow));
                    currentDate = (Date) contactInfo.get(getResources().getString(R.string.current));
                }
            }
        }

        initialiseValues();

        if(currentInfo != null)
        {
            String[] split = currentInfo.split(":");
            Friend friend = friendManager.getFriend(split[0]);
            invitedFriends.add(friend);
            setInvitedFriends();
            String[] location = split[2].split(", ");
            lat = location[0];
            lon = location[1];
            setLatitude(lat);
            setLongitude(lon);
        }

        for(Friend f: invitedFriends)
        {
            Log.i(LOG_TAG, f.getName());
        }

        meetingController = new MeetingController(this, null, friendManager, meetingManager);

        btEndDate.setOnClickListener(meetingController);
        Button btInvite = (Button) findViewById(R.id.btMeetingInvite);
        Button btRemove = (Button) findViewById(R.id.btRemoveInvited);
        Button btAdd = (Button) findViewById(R.id.btMeetingAdd);

        btInvite.setOnClickListener(meetingController);
        btRemove.setOnClickListener(meetingController);
        btAdd.setOnClickListener(meetingController);

        setCurrentTime();
    }

    public void setTitle(String title)
    {
        etMeetingTitle.setText(title);
    }

    public void setCurrentTime()
    {
        tvStartTime.setText(currentDate.toString());
    }

    public void setEndDate(int day, int month, int year)
    {
        tvEndDate.setText(day + "-" + (month+1) + "-" + year);
    }

    public void setEndTime(int hour, int minute)
    {
        tvEndTime.setText(String.format("%02d:%02d", hour, minute));
    }

    public void setLatitude(String lat)
    {
        etLatitude.setText(lat);
    }

    public void setLongitude(String lon)
    {
        etLongitude.setText(lon);
    }

    public void setInvitedFriends()
    {
        String appendedFriends = "";
        for(Friend f: invitedFriends)
        {
            if(appendedFriends.equals(""))
            {
                appendedFriends = appendedFriends + f.getName();
            }
            else
            {
                appendedFriends = appendedFriends + ", " + f.getName();
            }
        }
        tvInviteFriends.setText(appendedFriends);
    }

    public ArrayList<String> sendInfo()
    {
        ArrayList<String> info = new ArrayList<String>();
        title = etMeetingTitle.getText().toString();
        startTime = tvStartTime.getText().toString();
        endDate = tvEndDate.getText().toString();
        endTime = tvEndTime.getText().toString();
        String lat = etLatitude.getText().toString();
        String lon = etLongitude.getText().toString();
        info.add(title);
        info.add(startTime);
        info.add(endDate);
        info.add(endTime);
        info.add(lat);
        info.add(lon);
        return info;
    }

    public ArrayList<Friend> getInvitedFriends()
    {
        return invitedFriends;
    }

    public void goToInvite()
    {
        Intent intent = new Intent(this, InviteFriendActivity.class);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        intent.putExtra(getResources().getString(R.string.invite), invitedFriends);
        intent.putExtra(getResources().getString(R.string.title), etMeetingTitle.getText().toString());
        intent.putExtra(getResources().getString(R.string.startTime), tvStartTime.getText().toString());
        intent.putExtra(getResources().getString(R.string.endDate), tvEndDate.getText().toString());
        intent.putExtra(getResources().getString(R.string.endTime), tvEndTime.getText().toString());
        intent.putExtra(getResources().getString(R.string.latitude), etLatitude.getText().toString());
        intent.putExtra(getResources().getString(R.string.longitude), etLongitude.getText().toString());
        intent.putExtra(getResources().getString(R.string.className),getResources().getString(R.string.addMeeting));
        intent.putExtra(getResources().getString(R.string.current), currentDate);
        intent.putExtra(getResources().getString(R.string.location), location);
        startActivity(intent);
    }

    public void goToRemove()
    {
        Intent intent = new Intent(this, InviteFriendActivity.class);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        intent.putExtra(getResources().getString(R.string.invite), invitedFriends);
        intent.putExtra(getResources().getString(R.string.title), etMeetingTitle.getText().toString());
        intent.putExtra(getResources().getString(R.string.startTime), tvStartTime.getText().toString());
        intent.putExtra(getResources().getString(R.string.endDate), tvEndDate.getText().toString());
        intent.putExtra(getResources().getString(R.string.endTime), tvEndTime.getText().toString());
        intent.putExtra(getResources().getString(R.string.latitude), etLatitude.getText().toString());
        intent.putExtra(getResources().getString(R.string.longitude), etLongitude.getText().toString());
        intent.putExtra(getResources().getString(R.string.className),getResources().getString(R.string.removeInvited));
        intent.putExtra(getResources().getString(R.string.extra),getResources().getString(R.string.add));
        intent.putExtra(getResources().getString(R.string.location), location);
        startActivity(intent);
    }

    public void goToMeetingList()
    {
        Intent intent = new Intent(this, DisplayMeetingActivity.class);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        intent.putExtra(getResources().getString(R.string.location), location);
        startActivity(intent);
    }

    public void initialiseValues()
    {
        if(invitedFriends == null)
        {
            invitedFriends = new ArrayList<Friend>();
        }
        else
        {
            setInvitedFriends();
        }
        if(title != null)
        {
            setTitle(title);
        }
        if(endDate != null)
        {
            String[] dateSplit = endDate.split("-");
            setEndDate(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1])-1, Integer.parseInt(dateSplit[2]));
        }
        if(endTime != null)
        {
            String[] timeSplit = endTime.split(":");
            setEndTime(Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]));
        }
        if(lat != null)
        {
            setLatitude(lat);
        }
        if(lon != null)
        {
            setLongitude(lon);
        }
    }
}
