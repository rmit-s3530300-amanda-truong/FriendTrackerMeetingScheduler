package com.example.amanda.friendtrackerappass1.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.amanda.friendtrackerappass1.AsyncTask.UpdateContactAsync;
import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;

import java.util.ArrayList;
import java.util.Date;

public class EditContactActivity extends AppCompatActivity {

    private String name;
    private String email;
    private String id;
    private FriendManager friendManager;
    private boolean saved = false;
    private EditText etEditName;
    private EditText etEditEmail;
    private DatePicker datePicker;
    private Friend friend;
    private String LOG_TAG = this.getClass().getName();
    private MeetingManager meetingManager;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        Bundle contactInfo = getIntent().getExtras();
        if(contactInfo!=null)
        {
            name = contactInfo.getString(getResources().getString(R.string.name));
            email = contactInfo.getString(getResources().getString(R.string.email));
            id = contactInfo.getString(getResources().getString(R.string.id));
            friendManager = (FriendManager) contactInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) contactInfo.getSerializable(getResources().getString(R.string.meetingManager));
            location = (String) contactInfo.getString(getResources().getString(R.string.location));
        }

        friend = friendManager.getFriend(id);

        etEditName = (EditText) findViewById(R.id.etEditName);
        etEditEmail = (EditText) findViewById(R.id.etEditEmail);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        Date birthday = friend.getBirthday();
        if(birthday != null)
        {
            String[] birthdayArray = birthday.toString().split(" ");
            int newMonth = 0;
            if(birthdayArray[1].equals(getResources().getString(R.string.jan)))
            {
                newMonth = 1;
            }
            else if(birthdayArray[1].equals(getResources().getString(R.string.feb)))
            {
                newMonth = 2;
            }
            else if(birthdayArray[1].equals(getResources().getString(R.string.mar)))
            {
                newMonth = 3;
            }
            else if(birthdayArray[1].equals(getResources().getString(R.string.apr)))
            {
                newMonth = 4;
            }
            else if(birthdayArray[1].equals(getResources().getString(R.string.may)))
            {
                newMonth = 5;
            }
            else if(birthdayArray[1].equals(getResources().getString(R.string.jun)))
            {
                newMonth = 6;
            }
            else if(birthdayArray[1].equals(getResources().getString(R.string.jul)))
            {
                newMonth = 7;
            }
            else if(birthdayArray[1].equals(getResources().getString(R.string.aug)))
            {
                newMonth = 8;
            }
            else if(birthdayArray[1].equals(getResources().getString(R.string.sep)))
            {
                newMonth = 9;
            }
            else if(birthdayArray[1].equals(getResources().getString(R.string.oct)))
            {
                newMonth = 10;
            }
            else if(birthdayArray[1].equals(getResources().getString(R.string.nov)))
            {
                newMonth = 11;
            }
            else if(birthdayArray[1].equals(getResources().getString(R.string.dec)))
            {
                newMonth = 12;
            }
            datePicker.updateDate(Integer.parseInt(birthdayArray[5]), newMonth, Integer.parseInt(birthdayArray[2]));
        }
        etEditName.setText(name);
        etEditEmail.setText(email);

        Button btSave = (Button) findViewById(R.id.btSave);
        Button btGoBack = (Button) findViewById(R.id.btGoBack);
        btSave.setOnClickListener(new SaveController());
        btGoBack.setOnClickListener(new BackController());
    }

    private class SaveController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            UpdateContactAsync async = new UpdateContactAsync(EditContactActivity.this, friendManager);
            async.execute(friend);
            saved = true;
        }
    }

    private class BackController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(saved)
            {
                Intent intent = new Intent(EditContactActivity.this, DisplayContactActivity.class);
                intent.putExtra(getResources().getString(R.string.name),etEditName.getText().toString());
                intent.putExtra(getResources().getString(R.string.email), etEditEmail.getText().toString());
                intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
                intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
                intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.edit));
                intent.putExtra(getResources().getString(R.string.location), location);
                startActivity(intent);
            }
            else
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditContactActivity.this);
                alert.setTitle(getResources().getString(R.string.back));
                alert.setMessage(getResources().getString(R.string.confirmMessage));
                alert.setPositiveButton(getResources().getString(R.string.yes), new ConfirmController());
                alert.setNegativeButton(getResources().getString(R.string.no), null);
                alert.show();
            }
        }

        private class ConfirmController implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(EditContactActivity.this, DisplayContactActivity.class);
                intent.putExtra(getResources().getString(R.string.name), name);
                intent.putExtra(getResources().getString(R.string.email), email);
                intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
                intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
                intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.edit));
                intent.putExtra(getResources().getString(R.string.location), location);
                startActivity(intent);
            }
        }
    }
}
