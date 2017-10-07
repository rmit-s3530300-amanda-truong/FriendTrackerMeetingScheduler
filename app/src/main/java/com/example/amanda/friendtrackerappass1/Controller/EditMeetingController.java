package com.example.amanda.friendtrackerappass1.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.amanda.friendtrackerappass1.AsyncTask.UpdateMeetingAsync;
import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.DisplayMeetingActivity;
import com.example.amanda.friendtrackerappass1.View.EditMeetingActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by amanda on 3/09/2017.
 */

public class EditMeetingController implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditMeetingActivity activity;
    private FriendManager friendManager;
    private String title;
    private String startTime;
    private String endDate;
    private String endTime;
    private String lat;
    private String lon;
    private TimePickerDialog endtimePickerDialog;
    private DatePickerDialog endDatePicker;
    private MeetingManager meetingManager;
    private boolean dateCheck;
    private int startYearFinal, startMonthFinal, startDayFinal, startHourFinal, startMinuteFinal;
    private int endYear, endMonth, endDay, endHour, endMinute;
    private int endYearFinal, endMonthFinal, endDayFinal, endHourFinal, endMinuteFinal;
    private String LOG_TAG = this.getClass().getName();
    private boolean saved = false;
    private DBHandler db;

    public EditMeetingController(Activity activity, FriendManager friendManager, MeetingManager meetingManager, DBHandler db){
        this.activity = (EditMeetingActivity) activity;
        this.meetingManager = meetingManager;
        this.friendManager = friendManager;
        this.db = db;
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
        else if(buttonClicked == R.id.btSave)
        {
            startTime = activity.getStartTime();
            String[] startDateSplit = startTime.split(" ");
            startYearFinal = Integer.parseInt(startDateSplit[5]);
            startMonthFinal = getMonthCompare(startDateSplit[1]) + 1;
            startDayFinal = Integer.parseInt(startDateSplit[2]);

            String[] startTimeSplit = startDateSplit[3].split(":");
            startHourFinal = Integer.parseInt(startTimeSplit[0]);
            startMinuteFinal = Integer.parseInt(startTimeSplit[1]);

            endDate = activity.getEndDate();
            String[] endDateSplit = endDate.split("-");
            endYearFinal = Integer.parseInt(endDateSplit[2]);
            endMonthFinal = Integer.parseInt(endDateSplit[1]);
            endDayFinal = Integer.parseInt(endDateSplit[0]);

            endTime = activity.getEndTime();
            String[] endTimeSplit = endTime.split(":");
            endHourFinal = Integer.parseInt(endTimeSplit[0]);
            endMinuteFinal = Integer.parseInt(endTimeSplit[1]);
            boolean check = validateDateTime();
            if(check)
            {
                UpdateMeetingAsync async = new UpdateMeetingAsync(activity, friendManager, meetingManager);
                async.execute();
                saved = true;
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
        else if(buttonClicked == R.id.btBack)
        {
            if(saved)
            {
                Intent intent = new Intent(activity, DisplayMeetingActivity.class);
                intent.putExtra(activity.getResources().getString(R.string.friendManager), friendManager);
                intent.putExtra(activity.getResources().getString(R.string.meetingManager), meetingManager);
                intent.putExtra(activity.getResources().getString(R.string.className), activity.getResources().getString(R.string.edit));
                activity.startActivity(intent);
            }
            else
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle(activity.getResources().getString(R.string.back));
                alert.setMessage(activity.getResources().getString(R.string.confirmMessage));
                alert.setPositiveButton(activity.getResources().getString(R.string.yes), new ConfirmController());
                alert.setNegativeButton(activity.getResources().getString(R.string.no), null);
                alert.show();
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

    private class ConfirmController implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent(activity, DisplayMeetingActivity.class);
            intent.putExtra(activity.getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(activity.getResources().getString(R.string.meetingManager), meetingManager);
            intent.putExtra(activity.getResources().getString(R.string.className), activity.getResources().getString(R.string.edit));
            activity.startActivity(intent);
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
}
