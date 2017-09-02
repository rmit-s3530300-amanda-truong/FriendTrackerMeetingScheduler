package com.example.amanda.friendtrackerappass1.Model;

import android.util.Log;

import com.example.amanda.friendtrackerappass1.Model.Friend;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Amanda on 10/08/2017.
 */

public class FriendManager implements Serializable{

    private ArrayList<Friend> friendList;
    private ArrayList<String> friendNameList;
    private Friend friend;
    private String LOG_TAG = this.getClass().getName();

    public FriendManager() {
        friendList = new ArrayList<Friend>();
        friendNameList = new ArrayList<String>();
    }

    public void addFriend(Friend friend) {
        if (friendList.contains(friend)) {
            Log.i(LOG_TAG, "Error: Friend list already contains " + friend);
        } else {
            friendList.add(friend);
            friendNameList.add(friend.getName());
        }
    }

    public void removeFriend(Friend friend)
    {
        if(friendList.contains(friend))
        {
            friendList.remove(friend);
            friendNameList.add(friend.getName());
        }
        else
        {
            Log.i(LOG_TAG, "Error: Friend list doesnt contain " + friend);
        }
    }

    public Friend getFriend(String id)
    {
        for(Friend friend: friendList)
        {
            if(friend.getID().equals(id))
            {
                return friend;
            }
        }
        return null;
    }

    public ArrayList<Friend> getFriendList()
    {
        return friendList;
    }

    public ArrayList<String> getFriendNameList()
    {
        return friendNameList;
    }

}
