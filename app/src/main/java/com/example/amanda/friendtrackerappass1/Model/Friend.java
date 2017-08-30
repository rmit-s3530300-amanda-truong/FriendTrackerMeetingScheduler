package com.example.amanda.friendtrackerappass1.Model;

import java.util.Date;

/**
 * Created by Amanda on 10/08/2017.
 */

public class Friend {
    private String id;
    private String name;
    private String email;
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

    public void editBirthday(Date newBirthday)
    {
        birthday = newBirthday;
    }

    public void editName(String newName)
    {
        name = newName;
    }

    public void editEmail(String newEmail)
    {
        name = newEmail;
    }
}
