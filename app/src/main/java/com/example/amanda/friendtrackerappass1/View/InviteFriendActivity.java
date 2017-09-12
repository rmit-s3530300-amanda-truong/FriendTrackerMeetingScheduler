package com.example.amanda.friendtrackerappass1.View;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.amanda.friendtrackerappass1.Controller.EditMeetingController;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.ViewModel.FriendListAdapter;
import com.example.amanda.friendtrackerappass1.ViewModel.InviteFriendAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
            startDate = (String) contactInfo.getString(getResources().getString(R.string.startDate));
            startTime = (String) contactInfo.getString(getResources().getString(R.string.startTime));
            endDate = (String) contactInfo.getString(getResources().getString(R.string.endDate));
            endTime = (String) contactInfo.getString(getResources().getString(R.string.endTime));
            lat = (String) contactInfo.getString(getResources().getString(R.string.latitude));
            lon = (String) contactInfo.getString(getResources().getString(R.string.longitude));
            className = (String) contactInfo.getString(getResources().getString(R.string.className));
            extra = (String) contactInfo.getString(getResources().getString(R.string.extra));
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
                    Log.i(LOG_TAG, String.valueOf(invitedFriends.size()));
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
                    }

                    if(addCheck == true)
                    {
                        invitedFriends.add(friend);
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

    public void goToAddMeeting()
    {
        Intent intent = new Intent(this, AddMeetingActivity.class);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        intent.putExtra(getResources().getString(R.string.invite), invitedFriends);
        intent.putExtra(getResources().getString(R.string.title), title);
        intent.putExtra(getResources().getString(R.string.startDate), startDate);
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
        intent.putExtra(getResources().getString(R.string.invStDate), startDate);
        intent.putExtra(getResources().getString(R.string.invStTime), startTime);
        intent.putExtra(getResources().getString(R.string.invEndDate), endDate);
        intent.putExtra(getResources().getString(R.string.invEndTime), endTime);
        intent.putExtra(getResources().getString(R.string.invLat), lat);
        intent.putExtra(getResources().getString(R.string.invLon), lon);
        intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.invite));
        startActivity(intent);
    }
}
