package com.example.amanda.friendtrackerappass1.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;

/**
 * Created by Amanda on 7/10/2017.
 */

public class SettingsPop extends Activity{

    private Button btSave;
    private EditText etSuggestMeeting;
    private EditText etUpcomingMeeting;
    private int suggestInt;
    private int upcomingInt;
    private FriendManager friendManager;
    private MeetingManager meetingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_popup);

        etSuggestMeeting = (EditText) findViewById(R.id.etSuggestMeeting);
        etUpcomingMeeting = (EditText) findViewById(R.id.etUpcomingMeetings);
        btSave = (Button) findViewById(R.id.btSave);

        Bundle buttonInfo = getIntent().getExtras();
        if(buttonInfo != null)
        {
            friendManager = (FriendManager) buttonInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) buttonInfo.getSerializable(getResources().getString(R.string.meetingManager));
        }

        SharedPreferences pref = getSharedPreferences(getResources().getString(R.string.settings), MODE_PRIVATE);
        int upcoming = pref.getInt(getResources().getString(R.string.upcomingMeetings), 0);
        int suggest = pref.getInt(getResources().getString(R.string.suggestNow), 0);
        if(upcoming != 0)
        {
            etUpcomingMeeting.setText(String.valueOf(upcoming));
            upcomingInt = upcoming;
        }
        if(suggest != 0)
        {
            etSuggestMeeting.setText(String.valueOf(suggest));
            suggestInt = suggest;
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        double sWidth = width*.8;
        double sHeight = height*.6;

        getWindow().setLayout((int)sWidth,(int)sHeight);

        btSave.setOnClickListener(new SaveController());
    }

    private class SaveController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            suggestInt = Integer.valueOf(etSuggestMeeting.getText().toString());
            upcomingInt = Integer.valueOf(etUpcomingMeeting.getText().toString());
            SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.settings), MODE_PRIVATE).edit();
            editor.putInt(getResources().getString(R.string.upcomingMeetings), upcomingInt);
            editor.putInt(getResources().getString(R.string.suggestNow), suggestInt);
            editor.apply();
            Intent intent = new Intent(SettingsPop.this, MainActivity.class);
            intent.putExtra(getResources().getString(R.string.upcomingMeetings), upcomingInt);
            intent.putExtra(getResources().getString(R.string.suggestNow), suggestInt);
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
            startActivity(intent);
        }
    }
}
