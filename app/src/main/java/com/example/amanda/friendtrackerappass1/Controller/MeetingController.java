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
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String lat;
    private String lon;
    private ArrayList<Friend> invitedFriends;
    private ArrayList<Friend> friendList;
    private TimePickerDialog timePickerDialog;
    private TimePickerDialog endtimePickerDialog;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog endDatePicker;
    private MeetingManager meetingManager;
    private boolean afterCheck;
    private boolean dateCheck;
    private int startYear, startMonth, startDay, startHour, startMinute;
    private int startYearFinal, startMonthFinal, startDayFinal, startHourFinal, startMinuteFinal;
    private int endYear, endMonth, endDay, endHour, endMinute;
    private int endYearFinal, endMonthFinal, endDayFinal, endHourFinal, endMinuteFinal;
    private String LOG_TAG = this.getClass().getName();

    public MeetingController(Activity activity, FriendManager friendManager, MeetingManager meetingManager){
        this.activity = (AddMeetingActivity) activity;
        this.meetingManager = meetingManager;
        this.friendManager = friendManager;
    }

    public void generateID()
    {
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public void onClick(View view) {
        int buttonClicked = view.getId();

        if(buttonClicked == R.id.btStartDate) {
            Calendar c = Calendar.getInstance();
            startYear = c.get(Calendar.YEAR);
            startMonth = c.get(Calendar.MONTH);
            startDay = c.get(Calendar.DAY_OF_MONTH);
            afterCheck = false;
            datePickerDialog = new DatePickerDialog(activity, this, startYear, startMonth, startDay);
            datePickerDialog.show();
        }
        else if(buttonClicked == R.id.btEndDate)
        {
            Calendar c = Calendar.getInstance();
            endYear = c.get(Calendar.YEAR);
            endMonth = c.get(Calendar.MONTH);
            endDay = c.get(Calendar.DAY_OF_MONTH);
            afterCheck = true;
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
            startDate = info.get(1);
            startTime = info.get(2);
            endDate = info.get(3);
            endTime = info.get(4);
            lat = info.get(5);
            lon = info.get(6);
            String[] startDateSplit = startDate.split("-");
            startYearFinal = Integer.parseInt(startDateSplit[2]);
            startMonthFinal = Integer.parseInt(startDateSplit[1]);
            startDayFinal = Integer.parseInt(startDateSplit[0]);

            Log.i(LOG_TAG, String.valueOf(startYearFinal));
            Log.i(LOG_TAG, String.valueOf(startMonthFinal));
            Log.i(LOG_TAG, String.valueOf(startDayFinal));

            String[] startTimeSplit = startTime.split(":");
            startHourFinal = Integer.parseInt(startTimeSplit[0]);
            startMinuteFinal = Integer.parseInt(startTimeSplit[1]);

            Log.i(LOG_TAG, String.valueOf(startHourFinal));
            Log.i(LOG_TAG, String.valueOf(startMinuteFinal));

            String[] endDateSplit = endDate.split("-");
            endYearFinal = Integer.parseInt(endDateSplit[2]);
            endMonthFinal = Integer.parseInt(endDateSplit[1]);
            endDayFinal = Integer.parseInt(endDateSplit[0]);

            Log.i(LOG_TAG, String.valueOf(endYearFinal));
            Log.i(LOG_TAG, String.valueOf(endMonthFinal));
            Log.i(LOG_TAG, String.valueOf(endDayFinal));

            String[] endTimeSplit = endTime.split(":");
            endHourFinal = Integer.parseInt(endTimeSplit[0]);
            endMinuteFinal = Integer.parseInt(endTimeSplit[1]);

            Log.i(LOG_TAG, String.valueOf(endHourFinal));
            Log.i(LOG_TAG, String.valueOf(endMinuteFinal));

            boolean check = validateDateTime();
            if(check == true)
            {
                generateID();
                ArrayList<Friend> invitedFriends = activity.getInvitedFriends();
                Meeting meeting = new Meeting(uuid, title, startDate + " " + startTime, endDate + " " + endTime,
                        invitedFriends, lat+":"+lon);
                meetingManager.scheduleMeeting(meeting);
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

        if(dateCheck == true)
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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if(afterCheck == false)
        {
            startYearFinal = i;
            startMonthFinal = i1+1;
            startDayFinal = i2;
            activity.setStartDate(startDayFinal, startMonthFinal, startYearFinal);
            Calendar c = Calendar.getInstance();
            startHour = c.get(Calendar.HOUR_OF_DAY);
            startMinute = c.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(activity, this, startHour, startMinute, DateFormat.is24HourFormat(activity));
            timePickerDialog.show();
        }
        else
        {
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
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if(afterCheck == false)
        {
            startHourFinal = i;
            startMinuteFinal = i1;
            activity.setStartTime(startHourFinal, startMinuteFinal);
        }
        else
        {
            endHourFinal = i;
            endMinuteFinal = i1;
            activity.setEndTime(endHourFinal, endMinuteFinal);
        }
    }
}
