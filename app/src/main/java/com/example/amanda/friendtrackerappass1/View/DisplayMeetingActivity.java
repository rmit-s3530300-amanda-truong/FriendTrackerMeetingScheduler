package com.example.amanda.friendtrackerappass1.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.amanda.friendtrackerappass1.Controller.MeetingController;
import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.ViewModel.MeetingListAdapter;

import java.util.ArrayList;

public class DisplayMeetingActivity extends AppCompatActivity {

    private MeetingListAdapter adapter;
    private MeetingManager meetingManager;
    private FriendManager friendManager;
    private ArrayList<Meeting> meetingList;
    private MeetingController meetingController;
    private String[] menuItems;
    private String LOG_TAG = this.getClass().getName();
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_meeting);
        Bundle contactInfo = getIntent().getExtras();
        if(contactInfo != null)
        {
            friendManager = (FriendManager) contactInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) contactInfo.getSerializable(getResources().getString(R.string.meetingManager));
            location = (String) contactInfo.getString(getResources().getString(R.string.location));
        }
        meetingController = new MeetingController(null, this, friendManager, meetingManager);
        meetingList = meetingManager.getList();

        adapter = new MeetingListAdapter(this, R.layout.activity_list_view, meetingList);

        ListView lvMeetingList = (ListView) findViewById(R.id.lvMeetingList);
        lvMeetingList.setAdapter(adapter);
        registerForContextMenu(lvMeetingList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.lvMeetingList)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(getResources().getString(R.string.options));
            menuItems = getResources().getStringArray(R.array.menu);
            for(int i=0; i < menuItems.length; i++)
            {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];
        int listPos = info.position;
        if(menuItemName.equals(menuItems[0]))
        {
            Intent intent = new Intent(this, EditMeetingActivity.class);
            intent.putExtra(getResources().getString(R.string.name), adapter.getItem(listPos).getTitle());
            intent.putExtra(getResources().getString(R.string.startTime), adapter.getItem(listPos).getStartDate());
            Log.i(LOG_TAG, adapter.getItem(listPos).getStartDate());
            intent.putExtra(getResources().getString(R.string.endDate), adapter.getItem(listPos).getEndDate());
            intent.putExtra(getResources().getString(R.string.location), adapter.getItem(listPos).getLocation());
            intent.putExtra(getResources().getString(R.string.id), adapter.getItem(listPos).getID());
            intent.putExtra(getResources().getString(R.string.invite), adapter.getItem(listPos).getInvitedFriends());
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
            intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.displaymeetingList));
            intent.putExtra(getResources().getString(R.string.location), location);
            startActivity(intent);
        }
        else if(menuItemName.equals(menuItems[1]))
        {
            meetingController.removeMeeting(adapter.getItem(listPos));
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.action_displayContacts)
        {
            Intent intent = new Intent(this, DisplayContactActivity.class);
            intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.meetingList));
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
            intent.putExtra(getResources().getString(R.string.location), location);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.action_home)
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
            intent.putExtra(getResources().getString(R.string.location), location);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.action_addMeeting)
        {
            Intent intent = new Intent(this, AddMeetingActivity.class);
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
            intent.putExtra(getResources().getString(R.string.location), location);
            startActivity(intent);
        }
        return true;
    }
}
