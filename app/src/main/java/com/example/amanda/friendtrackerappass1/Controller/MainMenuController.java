package com.example.amanda.friendtrackerappass1.Controller;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.example.amanda.friendtrackerappass1.R;

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
    }

    public void addContact() {
        Log.i(LOG_TAG, "addContactActivity()");
        Log.i(LOG_TAG, activity.toString());
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        activity.startActivityForResult(contactPickerIntent, activity.getResources().getInteger(R.integer.PICK_CONTACTS));
    }
}
