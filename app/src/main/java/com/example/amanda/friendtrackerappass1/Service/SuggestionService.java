package com.example.amanda.friendtrackerappass1.Service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.PersistableBundle;
import android.util.Log;

import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.SuggestMeetingActivity;

/**
 * Created by Amanda on 8/10/2017.
 */

public class SuggestionService extends JobService {

    private FriendManager friendManager;
    private String LOG_TAG = this.getClass().getName();
    private MeetingManager meetingManager;
    private String location;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
//        Intent intent = new Intent(getApplicationContext(), SuggestMeetingActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
