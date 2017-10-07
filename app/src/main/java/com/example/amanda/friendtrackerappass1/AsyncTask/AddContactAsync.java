package com.example.amanda.friendtrackerappass1.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.View.DisplayContactActivity;

/**
 * Created by amanda on 5/10/2017.
 */

public class AddContactAsync extends AsyncTask<Friend, Void, Void> {

    private DisplayContactActivity activity;
    private FriendManager friendManager;
    private String LOG_TAG = this.getClass().getName();
    private DBHandler db;

    public AddContactAsync(Activity activity, FriendManager friendManager)
    {
        super();
        this.activity = (DisplayContactActivity) activity;
        this.friendManager = friendManager;
        db = new DBHandler(activity);
    }

    @Override
    protected Void doInBackground(Friend... friend) {
            friendManager.addFriend(friend[0]);
            db.createFriend(friend[0]);
            //testing only
            String table = db.getTableAsString("friend");
            Log.i(LOG_TAG, table);
            db.close();
        return null;
    }

}
