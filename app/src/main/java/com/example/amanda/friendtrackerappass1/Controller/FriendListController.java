package com.example.amanda.friendtrackerappass1.Controller;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.amanda.friendtrackerappass1.Model.DummyLocationService;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.ViewModel.FriendListAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by amanda on 31/08/2017.
 */

public class FriendListController {

    private FriendManager friendManager;
    private ArrayList<Friend> friendList;
    private Activity activity;
    private FriendListAdapter adapter;
    private String uuid;
    private String LOG_TAG = this.getClass().getName();
    private DummyLocationService dummyLocationService;

    public FriendListController(Activity activity, FriendManager friendManager)
    {
        this.friendManager = friendManager;
        this.activity = activity;
        friendList = friendManager.getFriendList();
        adapter = new FriendListAdapter(activity, R.layout.activity_list_view, friendList);
        dummyLocationService = DummyLocationService.getSingletonInstance(activity);
        dummyLocationService.logAll();
    }

    public FriendListAdapter getAdapter()
    {
        return adapter;
    }

    public void generateID()
    {
        uuid = UUID.randomUUID().toString();
    }

    public void addContact(String name, String email)
    {
        generateID();
        Date birthday = null;
        ArrayList<String> locationInfo = dummyLocationService.getLocationInfo();
        ArrayList<String> infoStore = new ArrayList<>();
        for(String s: locationInfo)
        {
            String[] split = s.split(", ");
            String[] nameSplit = split[2].split("=");
            if(nameSplit[1].equals(name))
            {
                infoStore.add(split[0]);
                infoStore.add(split[1]);
                infoStore.add(split[2]);
                infoStore.add(split[3]);
                infoStore.add(split[4]);
            }
        }
        Friend friend = new Friend(uuid, name, email, birthday);
        friend.setLocationInfo(infoStore);
        dummyLocationService.clearLocationInfo();
        boolean check = false;
        if(friendList.size() > 0)
        {
            for(Friend f: friendList)
            {
                if(friend.getName().equals(f.getName()))
                {
                    Toast toast = Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.duplicateFriend), Toast.LENGTH_SHORT);
                    toast.show();
                    check = true;
                }
            }
            if(!check)
            {
                friendManager.addFriend(friend);
            }
        }
        else
        {
            friendManager.addFriend(friend);
        }

    }

    public String getID()
    {
        return uuid;
    }
}
