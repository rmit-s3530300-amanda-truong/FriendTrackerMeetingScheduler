package com.example.amanda.friendtrackerappass1.Controller;

import android.app.Activity;

import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.ViewModel.FriendListAdapter;

import java.util.ArrayList;
import java.util.Date;
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

    public FriendListController(Activity activity, FriendManager friendManager)
    {
        this.friendManager = friendManager;
        this.activity = activity;
        friendList = friendManager.getFriendList();
        adapter = new FriendListAdapter(activity, R.layout.activity_list_view, friendList);
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
        Friend friend = new Friend(uuid, name, email, birthday);
        friendManager.addFriend(friend);
    }

    public String getID()
    {
        return uuid;
    }
}
