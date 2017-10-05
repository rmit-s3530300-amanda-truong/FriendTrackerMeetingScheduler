package com.example.amanda.friendtrackerappass1.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by amanda on 3/10/2017.
 */

public class DBHandler extends SQLiteOpenHelper{

    private String LOG_TAG = this.getClass().getName();

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
            +KEY_EDDATE+" STRING, "+KEY_INVFRIENDS+" STRING, "+KEY_LOCATION+" STRING)";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(LOG_TAG, "create context");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(LOG_TAG, "before create friend");
        sqLiteDatabase.execSQL(CREATE_TABLE_FRIEND);
        Log.i(LOG_TAG, "before create meeting");
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
        if(friend.getBirthday() != null)
        {
            values.put(KEY_BIRTHDAY, friend.getBirthday().toString());
        }
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
                friendIDAppend = friendIDAppend + " " + f.getID();
            }
            count++;
        }
        values.put(KEY_INVFRIENDS, friendIDAppend);
        values.put(KEY_LOCATION, meeting.getLocation());
        db.insert(TABLE_MEETING, null, values);
        return true;
    }

    public ArrayList<Friend> getAllFriends()
    {
        ArrayList<Friend> friendList = new ArrayList<Friend>();
        String select = "SELECT * FROM  " + TABLE_FRIEND;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst())
        {
            do
            {
                Date date = new Date();
                String birthday = cursor.getString(cursor.getColumnIndex(KEY_BIRTHDAY));
                if(birthday != null)
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    try
                    {
                        date = dateFormat.parse(birthday);
                    }
                    catch(ParseException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    date = null;
                }
                Friend friend = new Friend(cursor.getString((cursor.getColumnIndex(KEY_ID))),
                        cursor.getString((cursor.getColumnIndex(KEY_NAME))),
                        cursor.getString((cursor.getColumnIndex(KEY_EMAIL))),
                        date);
                friendList.add(friend);
            }
            while(cursor.moveToNext());
        }
        return friendList;
    }

    public ArrayList<Meeting> getAllMeetings()
    {
        ArrayList<Meeting> meetingList = new ArrayList<Meeting>();
        String select = "SELECT * FROM  " + TABLE_MEETING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst())
        {
            do
            {
                ArrayList<Friend> invitedFriends = new ArrayList<Friend>();
                String inviteFriendsList = cursor.getString((cursor.getColumnIndex(KEY_INVFRIENDS)));
                String[] friendSplit = inviteFriendsList.split(" ");
                for(int i=0; i<friendSplit.length; i++)
                {
                    ArrayList<Friend> friendList = getAllFriends();
                    for(Friend f: friendList)
                    {
                        Log.i(LOG_TAG, f.getID() + ":" + friendSplit[i]);
                        if(friendSplit[i].equals(f.getID()))
                        {
                            invitedFriends.add(f);
                        }
                    }
                }

                Meeting meeting = new Meeting(cursor.getString((cursor.getColumnIndex(KEY_ID))),
                        cursor.getString((cursor.getColumnIndex(KEY_TITLE))),
                        cursor.getString((cursor.getColumnIndex(KEY_STDATE))),
                        cursor.getString((cursor.getColumnIndex(KEY_EDDATE))),
                        invitedFriends,
                        cursor.getString((cursor.getColumnIndex(KEY_LOCATION))));
                meetingList.add(meeting);
            }
            while(cursor.moveToNext());
        }
        return meetingList;
    }

    public void clearFriendTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIEND, null, null);
    }

    public void clearMeetingTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEETING, null, null);
    }

    public boolean updateFriend(Friend friend)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,friend.getName());
        cv.put(KEY_EMAIL,friend.getEmail());
        cv.put(KEY_BIRTHDAY,friend.getBirthday().toString());
        db.update(TABLE_FRIEND, cv, KEY_ID+"='"+friend.getID()+"'", null);
        return true;
    }

    public boolean updateMeeting(Meeting meeting)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = 0;
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE,meeting.getTitle());
        cv.put(KEY_EDDATE,meeting.getEndDate());
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
                friendIDAppend = friendIDAppend + " " + f.getID();
            }
            count++;
        }
        cv.put(KEY_INVFRIENDS,friendIDAppend);
        cv.put(KEY_LOCATION,meeting.getLocation());
        db.update(TABLE_MEETING, cv, KEY_ID+"='"+meeting.getID()+"'", null);
        return true;
    }

    public boolean removeFriend(Friend friend)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIEND, KEY_ID+"='"+friend.getID()+"'", null);
        return true;
    }

    public boolean removeMeeting(Meeting meeting)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEETING, KEY_ID+"='"+meeting.getID()+"'", null);
        return true;
    }

    public String getTableAsString(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }

    // Closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
