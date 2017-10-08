package com.example.amanda.friendtrackerappass1.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.Controller.SuggestMeetingController;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class SuggestMeetingActivity extends AppCompatActivity {

    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private String LOG_TAG = this.getClass().getName();
    private String location;
    private SuggestMeetingController controller;
    private ArrayList<String> infoList;
    private ArrayList<String> orderedList;
    private TextView tvFriendName;
    private TextView tvLocationCoor;
    private int indexCounter = 0;
    private String currentInfo;
    private boolean calculateCheck = true;
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_meeting);

        Bundle buttonInfo = getIntent().getExtras();
        if(buttonInfo != null)
        {
            friendManager = (FriendManager) buttonInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) buttonInfo.getSerializable(getResources().getString(R.string.meetingManager));
            location = (String) buttonInfo.getSerializable(getResources().getString(R.string.location));
        }

        Button btShowLocation = (Button) findViewById(R.id.btShowLocation);
        Button btAccept = (Button) findViewById(R.id.btAccept);
        Button btDecline = (Button) findViewById(R.id.btDecline);
        Button btCancel = (Button) findViewById(R.id.btCancel);
        tvFriendName = (TextView) findViewById(R.id.tvFriendName);
        tvLocationCoor = (TextView) findViewById(R.id.tvLocationCoor);

        controller = new SuggestMeetingController(this, friendManager, meetingManager);
        if(calculateCheck == true)
        {
            controller.calculateMid();
            calculateCheck = false;
        }
        btShowLocation.setOnClickListener(controller);
        btAccept.setOnClickListener(controller);
        btDecline.setOnClickListener(controller);
        btCancel.setOnClickListener(controller);
    }

    public String getLocation()
    {
        return location;
    }

    public void setInfoList(ArrayList<String> infoList)
    {
        this.infoList = infoList;
    }

    public void setUp()
    {
        orderedList = orderList();
        retrieveInfo(indexCounter);
    }

    public void setCurrentDate(Date currentDate)
    {
        this.currentDate = currentDate;
    }

    public Date getCurrentDate()
    {
        return currentDate;
    }

    public ArrayList<String> orderList()
    {
        int shortestIndex = 0;
        ArrayList<String> copyList = new ArrayList<String>();
        for(String s: infoList)
        {
            copyList.add(s);
            Log.i(LOG_TAG, s);
        }
        ArrayList<String> orderedList = new ArrayList<String>();
        while(copyList.size() != 0)
        {
            long shortest = 0;
            int count = 0;
            for(String s: copyList)
            {
                String[] split = s.split(":");
                long duration = Long.parseLong(split[1]);
                int index = copyList.indexOf(s);
                Log.i(LOG_TAG, String.valueOf(index) + "before");
                if(count == 0 )
                {
                    shortest = duration;
                    shortestIndex = index;
                }
                else if(duration < shortest)
                {
                    shortest = duration;
                    shortestIndex = index;
                }
                count++;
            }
            String first = copyList.get(shortestIndex);
            orderedList.add(first);
            for(String s : orderedList)
            {
                Log.i(LOG_TAG, s);
            }
            Log.i(LOG_TAG, String.valueOf(shortestIndex) + "after");
            copyList.remove(shortestIndex);
            for(String s : copyList)
            {
                Log.i(LOG_TAG, s);
            }
        }

        return orderedList;
    }

    public void retrieveInfo(int index)
    {
        String s = orderedList.get(index);
        String[] split = s.split(":");
        Friend friend = friendManager.getFriend(split[0]);
        tvFriendName.setText(friend.getName());
        tvLocationCoor.setText(split[2]);
        currentInfo = s;
    }

    public void incrementCounter()
    {
        indexCounter++;
        if(indexCounter == orderedList.size())
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
            startActivity(intent);
        }
        else
        {
            retrieveInfo(indexCounter);
        }
    }

    public String getCurrentInfo()
    {
        return currentInfo;
    }
}
