package com.example.amanda.friendtrackerappass1.AsyncTask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.EditContactActivity;

/**
 * Created by amanda on 5/10/2017.
 */

public class UpdateContactAsync extends AsyncTask<Friend, Void, Void> {

    private EditContactActivity activity;
    private FriendManager friendManager;
    private String LOG_TAG = this.getClass().getName();
    private DBHandler db;
    private String name;
    private String email;
    private int date;
    private int month;
    private int year;

    public UpdateContactAsync(Activity activity, FriendManager friendManager)
    {
        super();
        this.activity = (EditContactActivity) activity;
        this.friendManager = friendManager;
        db = new DBHandler(activity);
    }

    @Override
    protected void onPreExecute()
    {
        if(activity != null)
        {
            EditText etName = (EditText) activity.findViewById(R.id.etEditName);
            name = etName.getText().toString();
            EditText etEmail = (EditText) activity.findViewById(R.id.etEditEmail);
            email = etEmail.getText().toString();
            DatePicker datePicker = (DatePicker) activity.findViewById(R.id.datePicker);
            date = datePicker.getDayOfMonth();
            month = datePicker.getMonth();
            year = datePicker.getYear();
        }

    }

    @Override
    protected Void doInBackground(Friend... friend) {
        friend[0].editName(name);
        friend[0].editEmail(email);
        friend[0].editBirthday(date, month, year);
        db.updateFriend(friend[0]);
        String table = db.getTableAsString("friend");
        Log.i(LOG_TAG, table);
        db.close();

        return null;
    }

    @Override
    protected void onPostExecute(Void voids)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(activity.getResources().getString(R.string.savedInfo));
        alert.setMessage(activity.getResources().getString(R.string.savedMessaged));
        alert.setPositiveButton(activity.getResources().getString(R.string.okay), null);
        alert.show();
    }

}
