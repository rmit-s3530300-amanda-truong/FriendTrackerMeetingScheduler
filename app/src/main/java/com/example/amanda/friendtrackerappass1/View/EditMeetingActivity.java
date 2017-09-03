package com.example.amanda.friendtrackerappass1.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.Controller.MeetingController;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;

import java.util.ArrayList;

public class EditMeetingActivity extends AppCompatActivity {

    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private String title;
    private String startDate;
    private String id;
    private String endDate;
    private String location;
    private ArrayList<Friend> invitedFriends;
    private EditText etMeetingTitle;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private EditText etLatitude;
    private EditText etLongitude;
    private Button btStartDate;
    private Button btEndDate;
    private boolean saved = false;
    private MeetingController meetingController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);

        etMeetingTitle = (EditText) findViewById(R.id.etMeetingTitle);
        btStartDate = (Button) findViewById(R.id.btStartDate);
        btEndDate = (Button) findViewById(R.id.btEndDate);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        etLatitude = (EditText) findViewById(R.id.etLatitude);
        etLongitude = (EditText) findViewById(R.id.etLongitude);

        Button btInvite = (Button) findViewById(R.id.btMeetingInvite);
        Button btSave = (Button) findViewById(R.id.btSave);
        Button btBack = (Button) findViewById(R.id.btBack);

        Bundle contactInfo = getIntent().getExtras();
        if(contactInfo != null)
        {
            id = (String) contactInfo.getString(getResources().getString(R.string.id));
            title = (String) contactInfo.getString(getResources().getString(R.string.name));
            startDate = (String) contactInfo.getString(getResources().getString(R.string.startDate));
            endDate = (String) contactInfo.getString(getResources().getString(R.string.endDate));
            location = (String) contactInfo.getString(getResources().getString(R.string.location));
            invitedFriends = (ArrayList<Friend>) contactInfo.get(getResources().getString(R.string.invite));
            friendManager = (FriendManager) contactInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) contactInfo.getSerializable(getResources().getString(R.string.meetingManager));
        }

        //meetingController = new MeetingController(this, friendManager, meetingManager);
        etMeetingTitle.setText(title);
        String[] startDateSplit = startDate.split(" ");
        tvStartDate.setText(startDateSplit[0]);
        tvStartTime.setText(startDateSplit[1]);
        String[] endDateSplit = endDate.split(" ");
        tvEndDate.setText(endDateSplit[0]);
        tvEndTime.setText(endDateSplit[1]);
        String[] locationSplit = location.split(":");
        etLatitude.setText(locationSplit[0]);
        etLongitude.setText(locationSplit[1]);

        //btStartDate.setOnClickListener(meetingController);
        //btEndDate.setOnClickListener(meetingController);
        btSave.setOnClickListener(new SaveController());
        btBack.setOnClickListener(new BackController());
    }

    private class SaveController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Meeting meeting = meetingManager.findMeeting(id);
            meeting.editTitle(etMeetingTitle.getText().toString());
            meeting.editStartDate(tvStartDate.getText().toString() + " " + tvStartTime.getText().toString());
            meeting.editEndDate(tvEndDate.getText().toString() + " " + tvEndTime.getText().toString());
            //havent implemented edit invited friends yet
            meeting.editLocation(etLatitude.getText().toString(), etLongitude.getText().toString());
            AlertDialog.Builder alert = new AlertDialog.Builder(EditMeetingActivity.this);
            alert.setTitle(getResources().getString(R.string.savedInfo));
            alert.setMessage(getResources().getString(R.string.savedMessaged));
            alert.setPositiveButton(getResources().getString(R.string.okay), null);
            alert.show();
            saved = true;
        }
    }

    private class BackController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(saved == true)
            {
                Intent intent = new Intent(EditMeetingActivity.this, DisplayMeetingActivity.class);
                intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
                intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
                intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.edit));
                startActivity(intent);
            }
            else
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditMeetingActivity.this);
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
                Intent intent = new Intent(EditMeetingActivity.this, DisplayMeetingActivity.class);
                intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
                intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
                intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.edit));
                startActivity(intent);
            }
        }
    }
}
