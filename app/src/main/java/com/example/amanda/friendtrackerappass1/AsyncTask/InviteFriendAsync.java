package com.example.amanda.friendtrackerappass1.AsyncTask;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.amanda.friendtrackerappass1.Model.DummyLocationService;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.LocationHandler;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.InviteFriendActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by amanda on 6/10/2017.
 */

public class InviteFriendAsync extends AsyncTask<Friend, Void, Void> {

    private InviteFriendActivity activity;
    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private String LOG_TAG = this.getClass().getName();
    List<DummyLocationService.FriendLocation> matched;
    private String lat;
    private String lon;
    private String finalMid;
    private Double latMid;
    private Double lonMid;
    private List<DummyLocationService.FriendLocation> matchListSorted;

    public InviteFriendAsync(Activity activity, FriendManager friendManager, MeetingManager meetingManager) {
        super();
        this.activity = (InviteFriendActivity) activity;
        this.friendManager = friendManager;
        this.meetingManager = meetingManager;
    }

    @Override
    protected Void doInBackground(Friend... friend) {
        boolean end = false;
        int count = 0;
        long shortestTime = 0;
        String location = activity.getLocation();
        String[] locationSplit = location.split(":");
        latMid = Double.parseDouble(locationSplit[0]);
        lonMid = Double.parseDouble(locationSplit[1]);
        DummyLocationService dummyLocationService = DummyLocationService.getSingletonInstance(activity);

        dummyLocationService.logAll();
        matched = dummyLocationService.getFriendLocationsForTime(activity.getCurrentDate(), 2, 0);
        if(matched.size() == 0)
        {
            publishProgress();
            finalMid = "0, 0";
            end = true;
        }

        matchListSorted = new ArrayList<>();
        if(matched.size()>0)
        {
            matchListSorted.add(matched.get(0));
        }

        Boolean found = false;

        for(DummyLocationService.FriendLocation f : matched)
        {
            found = false;
            for(int x =0; x<matchListSorted.size(); x++)
            {
                if (f.name.equals(matchListSorted.get(x).name))
                {
                    long friendDifference = f.time.getTime() - activity.getCurrentDate().getTime();
                    long friendSortedDifference = matchListSorted.get(x).time.getTime() - activity.getCurrentDate().getTime();
                    found = true;
                    if (friendDifference < friendSortedDifference)
                    {
                        matchListSorted.remove(x);
                        matchListSorted.add(x, f);
                    }
                }
            }
            if(found == false)
            {
                matchListSorted.add(f);
            }
        }
        for(DummyLocationService.FriendLocation f: matchListSorted)
        {
            Log.i(LOG_TAG, f.toString() + "match list sorted");
            if(f.name.equals(friend[0].getName()))
            {
                Date date = f.time;
                long timeDiff = date.getTime() - activity.getCurrentDate().getTime();
                lat = String.valueOf(f.latitude);
                lon = String.valueOf(f.longitude);
                Log.i(LOG_TAG, f.toString());
                Log.i(LOG_TAG, lat + lon + "location");
                latMid = (latMid + Double.parseDouble(lat))/2;
                lonMid = (lonMid + Double.parseDouble(lon))/2;
                finalMid = String.valueOf(latMid) + ", " + String.valueOf(lonMid);
                Log.i(LOG_TAG, finalMid + "finalMid");
            }
        }
        String[] locationFinal = finalMid.split(", ");
        lat = locationFinal[0];
        lon = locationFinal[1];
        activity.setLocation(lat, lon);

        return null;
    }

    @Override
    protected void onProgressUpdate(Void...voids)
    {
        Toast toast = Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.invalidLocation), Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onPostExecute(Void voids)
    {
        if(activity.getClassName().equals(activity.getResources().getString(R.string.addMeeting)))
        {
            activity.goToAddMeeting();
        }
        else
        {
            activity.goToEditMeeting();
        }
    }
}
