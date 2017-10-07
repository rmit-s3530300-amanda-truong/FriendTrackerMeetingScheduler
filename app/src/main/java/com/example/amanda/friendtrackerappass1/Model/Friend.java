package com.example.amanda.friendtrackerappass1.Model;

import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Amanda on 10/08/2017.
 */

public class Friend implements Serializable{
    private String id;
    private String name;
    private String email;
    private ArrayList<String> locationInfo = new ArrayList<>();
    private Date birthday;
    private String LOG_TAG = this.getClass().getName();

    public Friend(String id, String name, String email, Date birthday)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    public String getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void editBirthday(int date, int month, int year)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = String.valueOf(date);
        String monthStr = String.valueOf(month);
        String yearStr = String.valueOf(year);
        String dateAppend = dateStr + "-" + monthStr + "-" + yearStr;

        try
        {
            birthday = dateFormat.parse(dateAppend);
            Log.i(LOG_TAG, "birthday in friend" + birthday);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    public void editName(String newName)
    {
        name = newName;
    }

    public void editEmail(String newEmail)
    {
        email = newEmail;
    }

    public void setLocationInfo(ArrayList<String> info)
    {
        locationInfo = info;
    }

    public ArrayList<String> getLocation()
    {
        return locationInfo;
    }
}
