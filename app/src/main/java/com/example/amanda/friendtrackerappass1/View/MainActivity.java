package com.example.amanda.friendtrackerappass1.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.Controller.MainMenuController;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.R;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity{

    private String LOG_TAG = this.getClass().getName();
    private MainMenuController controller;
    private FriendManager friendManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MainMenuController(this);

        Log.i(LOG_TAG, "onCreate()");

        Button bAddContact = (Button) findViewById(R.id.bAddContact);
        Button bDisContact = (Button) findViewById(R.id.bDisContact);
        Button bAddMeeting = (Button) findViewById(R.id.bAddMeeting);
        Button bDisMeeting = (Button) findViewById(R.id.bDisMeeting);
        TextView tvContact = (TextView) findViewById(R.id.tvContact);
        TextView tvMeeting = (TextView) findViewById(R.id.tvMeeting);

        tvContact.setText(R.string.contact);
        tvMeeting.setText(R.string.meeting);

        bAddContact.setOnClickListener(controller);

        bDisContact.setOnClickListener(controller);

        bAddMeeting.setOnClickListener(controller);

        bDisMeeting.setOnClickListener(controller);

        Bundle buttonInfo = getIntent().getExtras();
        if(buttonInfo != null)
        {
            String buttonStr = buttonInfo.getString(getResources().getString(R.string.addContact));
            if(buttonStr.equals(getResources().getString(R.string.add_button)))
            {
                controller.addContact();
            }
            friendManager = (FriendManager) buttonInfo.getSerializable(getResources().getString(R.string.friendManager));
        }
        else
        {
            friendManager = new FriendManager();
        }
    }

    @Override
    protected void onRestart() {
        Log.i(LOG_TAG, "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(LOG_TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String name = "";
        String email = "";
        if (requestCode == this.getResources().getInteger(R.integer.PICK_CONTACTS))
        {
            if (resultCode == RESULT_OK)
            {
                ContactDataManager contactsManager = new ContactDataManager(this, data);
                try
                {
                    name = contactsManager.getContactName();
                    email = contactsManager.getContactEmail();
                    Log.i(LOG_TAG, name);
                    Log.i(LOG_TAG, email);
                }
                catch (ContactDataManager.ContactQueryException e)
                {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }

        Intent intent = new Intent(MainActivity.this, DisplayContactActivity.class);
        intent.putExtra(getResources().getString(R.string.name),name);
        intent.putExtra(getResources().getString(R.string.email),email);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.main));
        startActivity(intent);
   }
}
