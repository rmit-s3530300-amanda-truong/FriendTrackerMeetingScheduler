package com.example.amanda.friendtrackerappass1.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.DummyLocationService;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.DisplayContactActivity;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by amanda on 5/10/2017.
 */

public class RemoveContactAsync extends AsyncTask<Friend, Void, Void> {

    private DisplayContactActivity activity;
    private FriendManager friendManager;
    private String LOG_TAG = this.getClass().getName();
    private DBHandler db;

    public RemoveContactAsync(Activity activity, FriendManager friendManager)
    {
        super();
        this.activity = (DisplayContactActivity) activity;
        this.friendManager = friendManager;
        db = new DBHandler(activity);
    }

    @Override
    protected Void doInBackground(Friend... friend) {
        friendManager.removeFriend(friend[0]);
        db.removeFriend(friend[0]);
        //testing
        String table = db.getTableAsString("friend");
        Log.i(LOG_TAG, table);
        return null;
    }

}
