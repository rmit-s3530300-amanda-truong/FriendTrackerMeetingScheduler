package com.example.amanda.friendtrackerappass1.View;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.R;

public class MainActivity extends AppCompatActivity {

    private String LOG_TAG = this.getClass().getName();
    protected static final int PICK_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "onCreate()");

        Button bAddContact = (Button) findViewById(R.id.bAddContact);
        Button bDisContact = (Button) findViewById(R.id.bDisContact);
        Button bAddMeeting = (Button) findViewById(R.id.bAddMeeting);
        Button bDisMeeting = (Button) findViewById(R.id.bDisMeeting);
        TextView tvContact = (TextView) findViewById(R.id.tvContact);
        TextView tvMeeting = (TextView) findViewById(R.id.tvMeeting);

        tvContact.setText(R.string.contact);
        tvMeeting.setText(R.string.meeting);

        bAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact();
            }
        });

        bDisContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayContact();
            }
        });

        bAddMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMeeting();
            }
        });

        bDisMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayMeeting();
            }
        });
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

    public void addContact() {
        Log.i(LOG_TAG, "addContactActivity()");

        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, PICK_CONTACTS);
    }

    public void displayContact()
    {
        Log.i(LOG_TAG, "displayContactActivity()");

        Intent intent = new Intent(this, DisplayContactActivity.class);
        startActivity(intent);
    }

    public void addMeeting()
    {
        Log.i(LOG_TAG, "addMeetingActivity()");

        Intent intent = new Intent(this, AddMeetingActivity.class);
        startActivity(intent);
    }

    public void displayMeeting()
    {
        Log.i(LOG_TAG, "displayMeetingActivity()");

        Intent intent = new Intent(this, DisplayMeetingActivity.class);
        startActivity(intent);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String name = "";
        String email = "";
        if (requestCode == PICK_CONTACTS)
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

        Intent intent = new Intent(this, DisplayContactActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("email",email);
        startActivity(intent);
   }
}
