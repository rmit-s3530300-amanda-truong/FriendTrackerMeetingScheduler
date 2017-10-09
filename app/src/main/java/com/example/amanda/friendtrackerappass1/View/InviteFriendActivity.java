package com.example.amanda.friendtrackerappass1.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.amanda.friendtrackerappass1.AsyncTask.InviteFriendAsync;
import com.example.amanda.friendtrackerappass1.Model.DummyLocationService;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.ViewModel.FriendListAdapter;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class InviteFriendActivity extends AppCompatActivity{

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
    private String location;
    private String extra;
    private boolean addCheck = true;
    private Date currentDate;
    private boolean firstCheck = false;
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
            location = (String) contactInfo.getString(getResources().getString(R.string.location));
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
                            Log.i(LOG_TAG, f.getName() + friend.getName());
                            if(f.getName().equals(friend.getName()))
                            {
                                Log.i(LOG_TAG, "false");
                                addCheck = false;
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.duplicateFriend), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                    else
                    {
                        invitedFriends.add(friend);
                        firstCheck = true;
                        if(invitedFriends.size() == 1)
                        {
                            findMidPoint();
                        }
                        else
                        {
                            if(className.equals(getResources().getString(R.string.addMeeting)))
                            {
                                goToAddMeeting();
                            }
                            else
                            {
                                goToEditMeeting();
                            }
                        }
                    }

                    if(addCheck && !firstCheck)
                    {
                        invitedFriends.add(friend);
                        if(invitedFriends.size() == 1)
                        {
                            findMidPoint();
                        }
                        else
                        {
                            if(className.equals(getResources().getString(R.string.addMeeting)))
                            {
                                goToAddMeeting();
                            }
                            else
                            {
                                goToEditMeeting();
                            }
                        }
                    }
                    else
                    {
                        if(className.equals(getResources().getString(R.string.addMeeting)))
                        {
                            goToAddMeeting();
                        }
                        else
                        {
                            goToEditMeeting();
                        }
                    }
                }
            });
        }
    }

    public Friend getFriend()
    {
        return friend;
    }

    public String getClassName()
    {
        return className;
    }

    public void findMidPoint()
    {
        InviteFriendAsync async = new InviteFriendAsync(this, friendManager, meetingManager);
        async.execute(friend);
    }

    public void setLocation(String lat, String lon)
    {
        this.lat = lat;
        this.lon = lon;
    }

    public String getLocation()
    {
        return location;
    }

    public Date getCurrentDate()
    {
        return currentDate;
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
        intent.putExtra(getResources().getString(R.string.location), location);
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
        intent.putExtra(getResources().getString(R.string.location), location);
        startActivity(intent);
    }
}
