package com.example.amanda.friendtrackerappass1.AsyncTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.amanda.friendtrackerappass1.Model.DummyLocationService;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.View.InviteFriendActivity;
import com.example.amanda.friendtrackerappass1.View.SuggestMeetingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by amanda on 6/10/2017.
 */

public class SuggestNowAsync extends AsyncTask<String, Void, Void> {

    private SuggestMeetingActivity activity;
    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private String LOG_TAG = this.getClass().getName();
    List<DummyLocationService.FriendLocation> matched;
    private String lat;
    private String lon;
    private String finalMid;
    private String location;
    private Date currentDate;
    private URL url;
    private HttpURLConnection connection;
    private ArrayList<String> infoList;
    private ArrayList<Friend> friendList;
    private List<DummyLocationService.FriendLocation> matchListSorted;
    private long furthest;

    public SuggestNowAsync(Activity activity, FriendManager friendManager, MeetingManager meetingManager) {
        super();
        this.activity = (SuggestMeetingActivity) activity;
        this.friendManager = friendManager;
        this.meetingManager = meetingManager;
    }

    @Override
    protected void onPreExecute()
    {
        currentDate = Calendar.getInstance().getTime();
        infoList = new ArrayList<String>();
        friendList = friendManager.getFriendList();
    }

    @Override
    protected Void doInBackground(String... locations) {
        boolean end = false;
        location = locations[0];
        String[] locationSplit = location.split(":");
        Double myLat = Double.valueOf(locationSplit[0]);
        Double myLon = Double.valueOf(locationSplit[1]);
        Double latMid = myLat;
        Double lonMid = myLon;

        DummyLocationService dummyLocationService = DummyLocationService.getSingletonInstance(activity);
        dummyLocationService.logAll();
        matched = dummyLocationService
                .getFriendLocationsForTime(currentDate, 2, 0);
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

        for(DummyLocationService.FriendLocation friend : matched)
        {
            found = false;
            for(int x =0; x<matchListSorted.size(); x++)
            {
                if (friend.name.equals(matchListSorted.get(x).name))
                {
                    long friendDifference = friend.time.getTime() - currentDate.getTime();
                    long friendSortedDifference = matchListSorted.get(x).time.getTime() - currentDate.getTime();
                    found = true;
                    if (friendDifference < friendSortedDifference)
                    {
                        matchListSorted.remove(x);
                        matchListSorted.add(x, friend);
                    }
                }
            }
            if(found == false)
            {
                matchListSorted.add(friend);
            }
        }

        for(Friend friend: friendList)
        {
            for(DummyLocationService.FriendLocation f: matchListSorted)
            {
                if(f.name.equals(friend.getName()))
                {
                    lat = String.valueOf(f.latitude);
                    lon = String.valueOf(f.longitude);

                    String myDistance = getDistance(latMid, lonMid, myLat, myLon);
                    Log.i(LOG_TAG, lat + ":" + lon + f.name + "after");
                    String friendDistance = getDistance(latMid, lonMid, Double.parseDouble(lat), Double.parseDouble(lon));
                    long myDistanceTime = 0;
                    String[] myDistanceSplit = myDistance.split(" ");
                    if (myDistanceSplit.length == 2)
                    {
                        myDistanceTime = Integer.parseInt(myDistanceSplit[0]) * 60;
                    }
                    else if (myDistanceSplit.length == 4)
                    {
                        myDistanceTime = ((Integer.parseInt(myDistanceSplit[0]) * 60) + Integer.parseInt(myDistanceSplit[2])) * 60;
                    }

                    long friendDistanceTime = 0;
                    String[] friendDistanceSplit = friendDistance.split(" ");
                    if (friendDistanceSplit.length == 2)
                    {
                        friendDistanceTime = Integer.parseInt(friendDistanceSplit[0]) * 60;
                    }
                    else if (friendDistanceSplit.length == 4)
                    {
                        friendDistanceTime = ((Integer.parseInt(friendDistanceSplit[0]) * 60) + Integer.parseInt(friendDistanceSplit[2])) * 60;
                    }

                    if(myDistanceTime > friendDistanceTime)
                    {
                        furthest = myDistanceTime;
                    }
                    else
                    {
                        furthest = friendDistanceTime;
                    }
                    long totalDuration = myDistanceTime + friendDistanceTime;
                    infoList.add(friend.getID() + ":" + totalDuration + ":" + latMid + ", " + lonMid);
                }
            }
        }
        activity.setInfoList(infoList);
        return null;
    }

    @Override
    protected void onProgressUpdate(Void...voids)
    {
        Toast toast = Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.invalidLocation), Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onPostExecute(Void voids)
    {
        for(String s: infoList)
        {
            Log.i(LOG_TAG, s);
        }
        if(infoList.size() != 0)
        {
            Log.i(LOG_TAG, currentDate.toString() + furthest);
            Date date = new Date(currentDate.getTime() + furthest*1000);
            Log.i(LOG_TAG, date.toString());
            activity.setCurrentDate(date);
            activity.setUp();
        }
    }

    public String getDistance(Double latMid, Double lonMid, Double myLat, Double myLon)
    {
        Log.i(LOG_TAG, lat + "," + lon + "location");
        latMid = (latMid + Double.parseDouble(lat))/2;
        lonMid = (lonMid + Double.parseDouble(lon))/2;
        finalMid = String.valueOf(latMid) + ", " + String.valueOf(lonMid);
        Log.i(LOG_TAG, finalMid + "finalMid");
        int statusCode = 0;
        String duration = null;
        String urlStr = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latMid+","+lonMid + "&destinations=" + myLat+","+myLon + "&mode=walking";
        try
        {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            statusCode = connection.getResponseCode();
            if(statusCode==HttpURLConnection.HTTP_OK)
            {
                BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb=new StringBuilder();
                String line=br.readLine();
                while(line!=null)
                {
                    sb.append(line);
                    line=br.readLine();
                }
                String json=sb.toString();
                Log.d("JSON",json);
                JSONObject root=new JSONObject(json);
                JSONArray array_rows=root.getJSONArray("rows");
                Log.d("JSON","array_rows:"+array_rows);
                JSONObject object_rows=array_rows.getJSONObject(0);
                Log.d("JSON","object_rows:"+object_rows);
                JSONArray array_elements=object_rows.getJSONArray("elements");
                Log.d("JSON","array_elements:"+array_elements);
                JSONObject  object_elements=array_elements.getJSONObject(0);
                Log.d("JSON","object_elements:"+object_elements);
                JSONObject object_duration=object_elements.getJSONObject("duration");
                //JSONObject object_distance=object_elements.getJSONObject("distance");

                Log.d("JSON","object_duration:"+object_duration);
                //String duration = object_duration.getString("text");
                duration = object_duration.getString("text");
                Log.i(LOG_TAG,duration);

            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch(IOException exc)
        {
            exc.printStackTrace();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return duration;
    }
}
