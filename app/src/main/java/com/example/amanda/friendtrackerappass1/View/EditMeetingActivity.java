package com.example.amanda.friendtrackerappass1.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.Controller.EditMeetingController;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;

import java.util.ArrayList;

public class EditMeetingActivity extends AppCompatActivity {

    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private String title;
    private String startDate;
    private String startTime;
    private String endTime;
    private String id;
    private String endDate;
    private String invTitle;
    private String invSDate;
    private String invSTime;
    private String invEDate;
    private String invETime;
    private String lat;
    private String lon;
    private String location;
    private ArrayList<Friend> invitedFriends;
    private EditText etMeetingTitle;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private EditText etLatitude;
    private EditText etLongitude;
    private Button btStartDate;
    private Button btEndDate;
    private TextView tvInvitedFriend;
    private Button btInvite;
    private Button btSave;
    private Button btBack;
    private Button btRemoveInvited;
    private String className;
    private boolean saved = false;
    private EditMeetingController meetingController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);

        etMeetingTitle = (EditText) findViewById(R.id.etMeetingTitle);
        btStartDate = (Button) findViewById(R.id.btStartDate);
        btEndDate = (Button) findViewById(R.id.btEndDate);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        etLatitude = (EditText) findViewById(R.id.etLatitude);
        etLongitude = (EditText) findViewById(R.id.etLongitude);
        tvInvitedFriend = (TextView) findViewById(R.id.tvInvitedFriend);

        btInvite = (Button) findViewById(R.id.btMeetingInvite);
        btRemoveInvited = (Button) findViewById(R.id.btRemoveInvited);
        btSave = (Button) findViewById(R.id.btSave);
        btBack = (Button) findViewById(R.id.btBack);

        Bundle contactInfo = getIntent().getExtras();
        if(contactInfo != null)
        {
            id = (String) contactInfo.getString(getResources().getString(R.string.id));
            title = (String) contactInfo.getString(getResources().getString(R.string.name));
            startDate = (String) contactInfo.getString(getResources().getString(R.string.startDate));
            endDate = (String) contactInfo.getString(getResources().getString(R.string.endDate));
            location = (String) contactInfo.getString(getResources().getString(R.string.location));
            invTitle = (String) contactInfo.getString(getResources().getString(R.string.invTitle));
            invSDate = (String) contactInfo.getString(getResources().getString(R.string.invStDate));
            invSTime = (String) contactInfo.getString(getResources().getString(R.string.invStTime));
            invEDate = (String) contactInfo.getString(getResources().getString(R.string.invEndDate));
            invETime = (String) contactInfo.getString(getResources().getString(R.string.invEndTime));
            lat = (String) contactInfo.getString(getResources().getString(R.string.invLat));
            lon = (String) contactInfo.getString(getResources().getString(R.string.invLon));
            invitedFriends = (ArrayList<Friend>) contactInfo.get(getResources().getString(R.string.invite));
            friendManager = (FriendManager) contactInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) contactInfo.getSerializable(getResources().getString(R.string.meetingManager));
            className = (String) contactInfo.getString(getResources().getString(R.string.className));
        }

        meetingController = new EditMeetingController(this, friendManager, meetingManager);
        if(className.equals(getResources().getString(R.string.invite)))
        {
            initialiseValues();
        }
        else if(className.equals(getResources().getString(R.string.displaymeetingList)))
        {
            etMeetingTitle.setText(title);
            String[] startDateSplit = startDate.split(" ");
            tvStartDate.setText(startDateSplit[0]);
            tvStartTime.setText(startDateSplit[1]);
            String[] endDateSplit = endDate.split(" ");
            tvEndDate.setText(endDateSplit[0]);
            tvEndTime.setText(endDateSplit[1]);
            String[] locationSplit = location.split(":");
            etLatitude.setText(locationSplit[0]);
            etLongitude.setText(locationSplit[1]);
            setInvitedFriends();
        }

        btStartDate.setOnClickListener(meetingController);
        btEndDate.setOnClickListener(meetingController);
        btInvite.setOnClickListener(meetingController);
        btRemoveInvited.setOnClickListener(meetingController);
        btSave.setOnClickListener(meetingController);
        btBack.setOnClickListener(meetingController);
    }

    public void setTitle(String title)
    {
        etMeetingTitle.setText(title);
    }

    public void setStartDate(int day, int month, int year)
    {
        tvStartDate.setText(day + "-" + (month+1) + "-" + year);
    }

    public void setStartTime(int hour, int minute)
    {
        tvStartTime.setText(String.format("%02d:%02d", hour, minute));
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
        tvInvitedFriend.setText(appendedFriends);
    }

    public void goToInvite()
    {
        Intent intent = new Intent(this, InviteFriendActivity.class);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        intent.putExtra(getResources().getString(R.string.invite), invitedFriends);
        intent.putExtra(getResources().getString(R.string.id), id);
        intent.putExtra(getResources().getString(R.string.title), etMeetingTitle.getText().toString());
        intent.putExtra(getResources().getString(R.string.startDate), tvStartDate.getText().toString());
        intent.putExtra(getResources().getString(R.string.startTime), tvStartTime.getText().toString());
        intent.putExtra(getResources().getString(R.string.endDate), tvEndDate.getText().toString());
        intent.putExtra(getResources().getString(R.string.endTime), tvEndTime.getText().toString());
        intent.putExtra(getResources().getString(R.string.latitude), etLatitude.getText().toString());
        intent.putExtra(getResources().getString(R.string.longitude), etLongitude.getText().toString());
        intent.putExtra(getResources().getString(R.string.className),getResources().getString(R.string.edit));
        startActivity(intent);
    }

    public void goToRemove()
    {
        Intent intent = new Intent(this, InviteFriendActivity.class);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        intent.putExtra(getResources().getString(R.string.invite), invitedFriends);
        intent.putExtra(getResources().getString(R.string.id), id);
        intent.putExtra(getResources().getString(R.string.title), etMeetingTitle.getText().toString());
        intent.putExtra(getResources().getString(R.string.startDate), tvStartDate.getText().toString());
        intent.putExtra(getResources().getString(R.string.startTime), tvStartTime.getText().toString());
        intent.putExtra(getResources().getString(R.string.endDate), tvEndDate.getText().toString());
        intent.putExtra(getResources().getString(R.string.endTime), tvEndTime.getText().toString());
        intent.putExtra(getResources().getString(R.string.latitude), etLatitude.getText().toString());
        intent.putExtra(getResources().getString(R.string.longitude), etLongitude.getText().toString());
        intent.putExtra(getResources().getString(R.string.className),getResources().getString(R.string.removeInvited));
        intent.putExtra(getResources().getString(R.string.extra), getResources().getString(R.string.edit));
        startActivity(intent);
    }

    public void initialiseValues()
    {
        setInvitedFriends();
        if(invTitle != null)
        {
            setTitle(invTitle);
        }
        if(invSDate != null)
        {
            String[] dateSplit = invSDate.split("-");
            setStartDate(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1])-1, Integer.parseInt(dateSplit[2]));
        }
        if(invSTime != null)
        {
            String[] timeSplit = invSTime.split(":");
            setStartTime(Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]));
        }
        if(invEDate != null)
        {
            String[] dateSplit = invEDate.split("-");
            setEndDate(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1])-1, Integer.parseInt(dateSplit[2]));
        }
        if(invETime != null)
        {
            String[] timeSplit = invETime.split(":");
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

    public String getStartDate()
    {
        return tvStartDate.getText().toString();
    }

    public String getStartTime()
    {
        return tvStartTime.getText().toString();
    }

    public String getEndDate()
    {
        return tvEndDate.getText().toString();
    }

    public String getEndTime()
    {
        return tvEndTime.getText().toString();
    }

    public void saveValues()
    {
        Meeting meeting = meetingManager.findMeeting(id);
        meeting.editTitle(etMeetingTitle.getText().toString());
        meeting.editStartDate(tvStartDate.getText().toString() + " " + tvStartTime.getText().toString());
        meeting.editEndDate(tvEndDate.getText().toString() + " " + tvEndTime.getText().toString());
        meeting.editLocation(etLatitude.getText().toString(), etLongitude.getText().toString());
        meeting.editInvitedFriends(invitedFriends);
    }
}
