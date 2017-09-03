package com.example.amanda.friendtrackerappass1.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.Controller.MeetingController;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;

import java.util.ArrayList;

public class AddMeetingActivity extends AppCompatActivity {

    private String LOG_TAG = this.getClass().getName();
    private FriendManager friendManager;
    private MeetingController meetingController;
    private MeetingManager meetingManager;
    EditText etMeetingTitle;
    TextView tvStartDate;
    TextView tvStartTime;
    TextView tvEndDate;
    TextView tvEndTime;
    EditText etLatitude;
    EditText etLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        etMeetingTitle = (EditText) findViewById(R.id.etMeetingTitle);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        Button btStartDate = (Button) findViewById(R.id.btStartDate);
        Button btEndDate = (Button) findViewById(R.id.btEndDate);
        etLatitude = (EditText) findViewById(R.id.etLatitude);
        etLongitude = (EditText) findViewById(R.id.etLongitude);

        Bundle contactInfo = getIntent().getExtras();
        if(contactInfo != null)
        {
            friendManager = (FriendManager) contactInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) contactInfo.getSerializable(getResources().getString(R.string.meetingManager));
        }

        meetingController = new MeetingController(this,friendManager, meetingManager);

        btStartDate.setOnClickListener(meetingController);
        btEndDate.setOnClickListener(meetingController);
        Button btInvite = (Button) findViewById(R.id.btMeetingInvite);
        Button btAdd = (Button) findViewById(R.id.btMeetingAdd);

        //cant press add without typing in all fields
        btInvite.setOnClickListener(meetingController);
        btAdd.setOnClickListener(meetingController);
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

    public ArrayList<String> sendInfo()
    {
        ArrayList<String> info = new ArrayList<String>();
        String title = etMeetingTitle.getText().toString();
        String startDate = tvStartDate.getText().toString();
        String startTime = tvStartTime.getText().toString();
        String endDate = tvEndTime.getText().toString();
        String endTime = tvEndTime.getText().toString();
        String lat = etLatitude.getText().toString();
        String lon = etLongitude.getText().toString();
        info.add(title);
        info.add(startDate);
        info.add(startTime);
        info.add(endDate);
        info.add(endTime);
        info.add(lat);
        info.add(lon);
        return info;
    }

    public void goToInvite()
    {
        Intent intent = new Intent(this, InviteFriendActivity.class);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        startActivity(intent);
    }

    public void goToMeetingList()
    {
        Intent intent = new Intent(this, DisplayMeetingActivity.class);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        startActivity(intent);
    }

}
