package com.example.amanda.friendtrackerappass1.Controller;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.AddMeetingActivity;
import com.example.amanda.friendtrackerappass1.View.DisplayContactActivity;
import com.example.amanda.friendtrackerappass1.View.DisplayMeetingActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Amanda on 29/08/2017.
 */

public class MainMenuController implements View.OnClickListener{

    private String LOG_TAG = this.getClass().getName();
    private Activity activity;

    public MainMenuController(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        int buttonClicked = view.getId();
        if(buttonClicked == R.id.bAddContact)
        {
            addContact();
        }
        else if(buttonClicked == R.id.bDisContact)
        {
            displayContact();
        }
        else if(buttonClicked == R.id.bAddMeeting)
        {
            addMeeting();
        }
        else if(buttonClicked == R.id.bDisMeeting)
        {
            displayMeeting();
        }

    }

    public void addContact() {
        Log.i(LOG_TAG, "addContactActivity()");
        Log.i(LOG_TAG, activity.toString());
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        activity.startActivityForResult(contactPickerIntent, activity.getResources().getInteger(R.integer.PICK_CONTACTS));
    }

    public void displayContact()
    {
        Log.i(LOG_TAG, "displayContactActivity()");

        Intent intent = new Intent(activity, DisplayContactActivity.class);
        intent.putExtra(activity.getResources().getString(R.string.className),"displayContact");
        activity.startActivity(intent);
    }
    public void addMeeting()
    {
        Log.i(LOG_TAG, "addMeetingActivity()");

        Intent intent = new Intent(activity, AddMeetingActivity.class);
        activity.startActivity(intent);
    }

    public void displayMeeting()
    {
        Log.i(LOG_TAG, "displayMeetingActivity()");

        Intent intent = new Intent(activity, DisplayMeetingActivity.class);
        activity.startActivity(intent);
    }
}
