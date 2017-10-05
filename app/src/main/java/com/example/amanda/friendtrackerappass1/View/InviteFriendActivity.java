package com.example.amanda.friendtrackerappass1.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.amanda.friendtrackerappass1.Model.DummyLocationService;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.ViewModel.FriendListAdapter;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class InviteFriendActivity extends Activity {

    private FriendListAdapter adapter;

    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private ListView lvFriendList;
    private Friend friend;
    private ArrayList<Friend> invitedFriends;
    private String LOG_TAG = this.getClass().getName();
    private String title;
    private String id;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String lat;
    private String lon;
    private String className;
    private String extra;
    private boolean addCheck;
    private Date currentDate;
    List<DummyLocationService.FriendLocation> matched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        Bundle contactInfo = getIntent().getExtras();
        if(contactInfo != null)
        {
            friendManager = (FriendManager) contactInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) contactInfo.getSerializable(getResources().getString(R.string.meetingManager));
            id = (String) contactInfo.getString(getResources().getString(R.string.id));
            invitedFriends = (ArrayList<Friend>) contactInfo.getSerializable(getResources().getString(R.string.invite));
            title = (String) contactInfo.getString(getResources().getString(R.string.title));
            startTime = (String) contactInfo.getString(getResources().getString(R.string.startTime));
            endDate = (String) contactInfo.getString(getResources().getString(R.string.endDate));
            endTime = (String) contactInfo.getString(getResources().getString(R.string.endTime));
            lat = (String) contactInfo.getString(getResources().getString(R.string.latitude));
            lon = (String) contactInfo.getString(getResources().getString(R.string.longitude));
            className = (String) contactInfo.getString(getResources().getString(R.string.className));
            extra = (String) contactInfo.getString(getResources().getString(R.string.extra));
            currentDate = (Date) contactInfo.get(getResources().getString(R.string.current));
        }
        if(className.equals(getResources().getString(R.string.removeInvited)))
        {
            adapter = new FriendListAdapter(this, R.layout.activity_list_view, invitedFriends);
            lvFriendList = (ListView) findViewById(R.id.lvFriendList);
            lvFriendList.setAdapter(adapter);
            lvFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    friend = (Friend) adapterView.getAdapter().getItem(i);
                    invitedFriends.remove(friend);
                    if(extra.equals(getResources().getString(R.string.edit)))
                    {
                        goToEditMeeting();
                    }
                    else
                    {
                        goToAddMeeting();
                    }
                }
            });
        }
        else
        {
            adapter = new FriendListAdapter(this, R.layout.activity_list_view, friendManager.getFriendList());

            lvFriendList = (ListView) findViewById(R.id.lvFriendList);
            lvFriendList.setAdapter(adapter);
            lvFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    friend = (Friend) adapterView.getAdapter().getItem(i);
                    if(invitedFriends.size() != 0)
                    {
                        for(Friend f: invitedFriends)
                        {
                            if(f.getName().equals(friend.getName()))
                            {
                                addCheck = false;
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.duplicateFriend), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else
                            {
                                addCheck = true;
                            }
                        }
                    }
                    else
                    {
                        invitedFriends.add(friend);
                        String[] location = findMidPoint().split(", ");
                        lat = location[0];
                        lon = location[1];
                    }

                    if(addCheck)
                    {
                        invitedFriends.add(friend);
                        String[] location = findMidPoint().split(", ");
                        lat = location[0];
                        lon = location[1];
                    }
                    if(className.equals(getResources().getString(R.string.addMeeting)))
                    {
                        goToAddMeeting();
                    }
                    else
                    {
                        goToEditMeeting();
                    }
                }
            });
        }
    }

    public String findMidPoint()
    {
        int count = 0;
        long shortestTime = 0;
        Double latMid = -30.35;
        Double lonMid = 50.13;
        DummyLocationService dummyLocationService = DummyLocationService.getSingletonInstance(this);

        Friend firstFriend = invitedFriends.get(0);
        dummyLocationService.logAll();
            matched = dummyLocationService
                    .getFriendLocationsForTime(currentDate, 2, 0);
        if(matched.size() == 0)
        {
            Toast toast = Toast.makeText(this.getApplicationContext(), this.getResources().getString(R.string.invalidLocation), Toast.LENGTH_LONG);
            toast.show();
            return "0, 0";
        }
        for(DummyLocationService.FriendLocation f: matched)
        {
            if(f.name.equals(firstFriend.getName()))
            {
                Date date = f.time;
                long timeDiff = date.getTime() - currentDate.getTime();
                if(count == 0)
                {
                    shortestTime = timeDiff;
                    lat = String.valueOf(f.latitude);
                    lon = String.valueOf(f.longitude);
                }
                else
                {
                    if(timeDiff < shortestTime)
                    {
                        shortestTime = timeDiff;
                        lat = String.valueOf(f.latitude);
                        lon = String.valueOf(f.longitude);
                    }
                }
                count++;
                Log.i(LOG_TAG, f.toString());
            }
            else
            {
                Toast toast = Toast.makeText(this.getApplicationContext(), this.getResources().getString(R.string.invalidLocation), Toast.LENGTH_LONG);
                toast.show();
                return "0, 0";
            }
        }
        latMid = (latMid + Double.parseDouble(lat))/2;
        lonMid = (lonMid + Double.parseDouble(lon))/2;

        String finalMid = String.valueOf(latMid) + ", " + String.valueOf(lonMid);
        return finalMid;
    }

    public void goToAddMeeting()
    {
        Intent intent = new Intent(this, AddMeetingActivity.class);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        intent.putExtra(getResources().getString(R.string.invite), invitedFriends);
        intent.putExtra(getResources().getString(R.string.title), title);
        intent.putExtra(getResources().getString(R.string.startTime), startTime);
        intent.putExtra(getResources().getString(R.string.endDate), endDate);
        intent.putExtra(getResources().getString(R.string.endTime), endTime);
        intent.putExtra(getResources().getString(R.string.latitude), lat);
        intent.putExtra(getResources().getString(R.string.longitude), lon);
        startActivity(intent);
    }

    public void goToEditMeeting()
    {
        Intent intent = new Intent(this, EditMeetingActivity.class);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        intent.putExtra(getResources().getString(R.string.id), id);
        intent.putExtra(getResources().getString(R.string.invite), invitedFriends);
        intent.putExtra(getResources().getString(R.string.invTitle), title);
        intent.putExtra(getResources().getString(R.string.invStTime), startTime);
        intent.putExtra(getResources().getString(R.string.invEndDate), endDate);
        intent.putExtra(getResources().getString(R.string.invEndTime), endTime);
        intent.putExtra(getResources().getString(R.string.invLat), lat);
        intent.putExtra(getResources().getString(R.string.invLon), lon);
        intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.invite));
        startActivity(intent);
    }
}
