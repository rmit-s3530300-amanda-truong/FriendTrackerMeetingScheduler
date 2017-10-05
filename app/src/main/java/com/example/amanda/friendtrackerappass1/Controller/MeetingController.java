package com.example.amanda.friendtrackerappass1.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.AddMeetingActivity;
import com.example.amanda.friendtrackerappass1.View.EditMeetingActivity;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by amanda on 3/09/2017.
 */

public class MeetingController implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String uuid;
    private AddMeetingActivity activity;
    private FriendManager friendManager;
    private String title;
    private String startTime;
    private String endDate;
    private String endTime;
    private String lat;
    private String lon;
    private ArrayList<Friend> invitedFriends;
    private ArrayList<Friend> friendList;
    private TimePickerDialog endtimePickerDialog;
    private DatePickerDialog endDatePicker;
    private MeetingManager meetingManager;
    private boolean dateCheck;
    private int startYearFinal, startMonthFinal, startDayFinal, startHourFinal, startMinuteFinal;
    private int endYear, endMonth, endDay, endHour, endMinute;
    private int endYearFinal, endMonthFinal, endDayFinal, endHourFinal, endMinuteFinal;
    private String LOG_TAG = this.getClass().getName();
    private DBHandler db;

    public MeetingController(Activity activity, FriendManager friendManager, MeetingManager meetingManager, DBHandler db){
        this.activity = (AddMeetingActivity) activity;
        this.meetingManager = meetingManager;
        this.friendManager = friendManager;
        this.db = db;
    }

    public void generateID()
    {
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public void onClick(View view) {
        int buttonClicked = view.getId();

        if(buttonClicked == R.id.btEndDate)
        {
            Calendar c = Calendar.getInstance();
            endYear = c.get(Calendar.YEAR);
            endMonth = c.get(Calendar.MONTH);
            endDay = c.get(Calendar.DAY_OF_MONTH);
            endDatePicker= new DatePickerDialog(activity, this, endYear, endMonth, endDay);
            endDatePicker.show();
        }
        else if(buttonClicked == R.id.btMeetingInvite)
        {
            activity.goToInvite();
        }
        else if(buttonClicked == R.id.btRemoveInvited)
        {
            activity.goToRemove();
        }
        else if(buttonClicked == R.id.btMeetingAdd)
        {
            ArrayList<String> info = activity.sendInfo();
            title = info.get(0);
            startTime = info.get(1);
            endDate = info.get(2);
            endTime = info.get(3);
            lat = info.get(4);
            lon = info.get(5);

            String[] startDateSplit = startTime.split(" ");
            startYearFinal = Integer.parseInt(startDateSplit[5]);
            startMonthFinal = getMonthCompare(startDateSplit[1]) + 1;
            startDayFinal = Integer.parseInt(startDateSplit[2]);

            String[] startTimeSplit = startDateSplit[3].split(":");
            startHourFinal = Integer.parseInt(startTimeSplit[0]);
            startMinuteFinal = Integer.parseInt(startTimeSplit[1]);

            String[] endDateSplit = endDate.split("-");
            endYearFinal = Integer.parseInt(endDateSplit[2]);
            endMonthFinal = Integer.parseInt(endDateSplit[1]);
            endDayFinal = Integer.parseInt(endDateSplit[0]);

            String[] endTimeSplit = endTime.split(":");
            endHourFinal = Integer.parseInt(endTimeSplit[0]);
            endMinuteFinal = Integer.parseInt(endTimeSplit[1]);

            boolean check = validateDateTime();
            if(check)
            {
                generateID();
                ArrayList<Friend> invitedFriends = activity.getInvitedFriends();
                Meeting meeting = new Meeting(uuid, title, startTime, endDate + " " + endTime,
                        invitedFriends, lat+":"+lon);
                meetingManager.scheduleMeeting(meeting);
                db.createMeeting(meeting);
                String table = db.getTableAsString("meeting");
                Log.i(LOG_TAG, table);
                ArrayList<Meeting> meetingList = db.getAllMeetings();
                for(Meeting m: meetingList)
                {
                    Log.i(LOG_TAG, m.getID());
                    Log.i(LOG_TAG, m.getTitle());
                    Log.i(LOG_TAG, m.getStartDate());
                    Log.i(LOG_TAG, m.getEndDate());
                    Log.i(LOG_TAG, m.getInvitedFriends().toString());
                    Log.i(LOG_TAG, m.getLocation());
                }
                db.close();
                activity.goToMeetingList();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(activity.getResources().getString(R.string.invalid));
                builder.setMessage(activity.getResources().getString(R.string.invAfter));
                builder.setPositiveButton(activity.getResources().getString(R.string.okay),null);
                builder.show();
            }
        }
    }

    public boolean validateDateTime()
    {
        if(startYearFinal < endYearFinal)
        {
            dateCheck = true;
        }
        else if(startYearFinal == endYearFinal)
        {
            if(startMonthFinal < endMonthFinal)
            {
                dateCheck = true;
            }
            else if(startMonthFinal == endMonthFinal)
            {
                if(startDayFinal < endDayFinal)
                {
                    dateCheck = true;
                }
                else if(startDayFinal == endDayFinal)
                {
                    dateCheck = false;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }

        if(dateCheck)
        {
            return true;
        }
        else
        {
            if(startHourFinal < endHourFinal)
            {
                return true;
            }
            else if(startHourFinal == endHourFinal)
            {
                if(startMinuteFinal < endMinuteFinal)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
    }

    public int getMonthCompare(String monthStr)
    {
        int newMonth = 0;
        if(monthStr.equals(activity.getResources().getString(R.string.jan)))
        {
            newMonth = 1;
        }
        else if(monthStr.equals(activity.getResources().getString(R.string.feb)))
        {
            newMonth = 2;
        }
        else if(monthStr.equals(activity.getResources().getString(R.string.mar)))
        {
            newMonth = 3;
        }
        else if(monthStr.equals(activity.getResources().getString(R.string.apr)))
        {
            newMonth = 4;
        }
        else if(monthStr.equals(activity.getResources().getString(R.string.may)))
        {
            newMonth = 5;
        }
        else if(monthStr.equals(activity.getResources().getString(R.string.jun)))
        {
            newMonth = 6;
        }
        else if(monthStr.equals(activity.getResources().getString(R.string.jul)))
        {
            newMonth = 7;
        }
        else if(monthStr.equals(activity.getResources().getString(R.string.aug)))
        {
            newMonth = 8;
        }
        else if(monthStr.equals(activity.getResources().getString(R.string.sep)))
        {
            newMonth = 9;
        }
        else if(monthStr.equals(activity.getResources().getString(R.string.oct)))
        {
            newMonth = 10;
        }
        else if(monthStr.equals(activity.getResources().getString(R.string.nov)))
        {
            newMonth = 11;
        }
        else if(monthStr.equals(activity.getResources().getString(R.string.dec)))
        {
            newMonth = 12;
        }
        return newMonth;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        endYearFinal = i;
        endMonthFinal = i1+1;
        endDayFinal = i2;
        activity.setEndDate(endDayFinal, endMonthFinal, endYearFinal);
        Calendar c = Calendar.getInstance();
        endHour = c.get(Calendar.HOUR_OF_DAY);
        endMinute = c.get(Calendar.MINUTE);

        endtimePickerDialog = new TimePickerDialog(activity, this, endHour, endMinute, DateFormat.is24HourFormat(activity));
        endtimePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        endHourFinal = i;
        endMinuteFinal = i1;
        activity.setEndTime(endHourFinal, endMinuteFinal);
    }
}
