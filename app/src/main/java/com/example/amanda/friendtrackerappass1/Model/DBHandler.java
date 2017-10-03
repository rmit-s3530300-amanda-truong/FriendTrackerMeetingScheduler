package com.example.amanda.friendtrackerappass1.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by amanda on 3/10/2017.
 */

public class DBHandler extends SQLiteOpenHelper{

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "friendTrackerDB";

    // Table Names
    private static final String TABLE_FRIEND = "friend";
    private static final String TABLE_MEETING = "meeting";

    // Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_LOCATIONINFO = "locationInfo";
    private static final String KEY_TITLE = "title";
    private static final String KEY_STDATE = "startDate";
    private static final String KEY_EDDATE = "endDate";
    private static final String KEY_INVFRIENDS = "invitedFriends";
    private static final String KEY_LOCATION = "location";

    // Table create statements
    private static final String CREATE_TABLE_FRIEND = "CREATE TABLE " + TABLE_FRIEND +
            "(" +KEY_ID+" STRING PRIMARY KEY, "+ KEY_NAME+" STRING, "+KEY_EMAIL+" STRING, "
            +KEY_BIRTHDAY+" DATE, "+KEY_LOCATIONINFO+" STRING)";

    private static final String CREATE_TABLE_MEETING = "CREATE TABLE " + TABLE_MEETING +
            "(" +KEY_ID+" STRING PRIMARY KEY, "+ KEY_TITLE+" STRING, "+KEY_STDATE+" STRING, "
            +KEY_EDDATE+" DATE, "+KEY_INVFRIENDS+" STRING, "+KEY_LOCATION+" STRING)";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_FRIEND);
        sqLiteDatabase.execSQL(CREATE_TABLE_MEETING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MEETING);
        onCreate(sqLiteDatabase);
    }

    // Creating friend
    public boolean createFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_ID, friend.getID());
        values.put(KEY_NAME, friend.getName());
        values.put(KEY_EMAIL, friend.getEmail());
        values.put(KEY_BIRTHDAY, friend.getBirthday().toString());
        ArrayList<String> friendLocation = friend.getLocation();
        String locationAppend = "";
        for(String f: friendLocation)
        {
            if(count == 0)
            {
                locationAppend = locationAppend + f;
            }
            else
            {
                locationAppend = locationAppend + "-" + f;
            }
            count++;
        }
        values.put(KEY_LOCATIONINFO, locationAppend);
        db.insert(TABLE_FRIEND, null, values);
        return true;
    }

    // Creating Meeting
    public boolean createMeeting(Meeting meeting) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_ID, meeting.getID());
        values.put(KEY_TITLE, meeting.getTitle());
        values.put(KEY_STDATE, meeting.getStartDate());
        values.put(KEY_EDDATE, meeting.getEndDate());
        ArrayList<Friend> invitedFriends = meeting.getInvitedFriends();
        String friendIDAppend = "";
        for(Friend f: invitedFriends)
        {
            if(count == 0)
            {
                friendIDAppend = friendIDAppend + f.getID();
            }
            else
            {
                friendIDAppend = friendIDAppend + "-" + f.getID();
            }
            count++;
        }
        values.put(KEY_LOCATION, meeting.getLocation());
        db.insert(TABLE_MEETING, null, values);
        return true;
    }

    // Closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
