package com.example.amanda.friendtrackerappass1.View;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amanda.friendtrackerappass1.AsyncTask.RetrieveListsAsync;
import com.example.amanda.friendtrackerappass1.Controller.MainMenuController;
import com.example.amanda.friendtrackerappass1.Model.ContactDataManager;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.LocationHandler;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.Receivers.SuggestMeetingReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    private String LOG_TAG = this.getClass().getName();
    private MainMenuController controller;
    private BroadcastReceiver receiver;
    private Boolean checkNetworkState = true;
    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private LocationManager locationManager;
    private LocationHandler locationHandler;
    private int upcomingInt;
    private int suggestInt;
    private int remindMeInt;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "onCreate()");

        Button bAddContact = (Button) findViewById(R.id.bAddContact);
        Button bDisContact = (Button) findViewById(R.id.btDisplayContact);
        Button bDisMeeting = (Button) findViewById(R.id.btDisplayMeeting);
        Button bSuggest = (Button) findViewById(R.id.btSuggest);
        TextView tvContact = (TextView) findViewById(R.id.tvContact);
        TextView tvInstruct = (TextView) findViewById(R.id.tvInstruct);
        callBroadCastReceiver();
        controller = new MainMenuController(this);

        Intent intent = getIntent();
        if(intent.hasExtra(getResources().getString(R.string.friendManager)))
        {
            Bundle buttonInfo = getIntent().getExtras();
            String addButton = buttonInfo.getString(getResources().getString(R.string.addContact));
            if (addButton != null)
            {
                if (addButton.equals(getResources().getString(R.string.addContact)))
                {
                    controller.addContact();
                }
            }
            friendManager = (FriendManager) buttonInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) buttonInfo.getSerializable(getResources().getString(R.string.meetingManager));
            String newLocation = (String) buttonInfo.getString(getResources().getString(R.string.location));
            if (location != null)
            {
                location = newLocation;
            }
        }
        else
        {
            friendManager = new FriendManager();
            meetingManager = new MeetingManager();
        }

        getLocationPermission();
        getContactPermission();

        RetrieveListsAsync asyncTask = new RetrieveListsAsync(this, friendManager, meetingManager);
        asyncTask.execute();

        bAddContact.setOnClickListener(controller);
        bSuggest.setOnClickListener(new SuggestController());
        bDisContact.setOnClickListener(new DisContactMainController());
        bDisMeeting.setOnClickListener(new DisMeetingMainController());

        SharedPreferences pref = getSharedPreferences(getResources().getString(R.string.settings), MODE_PRIVATE);
        if(pref != null)
        {
            int upcoming = pref.getInt(getResources().getString(R.string.upcomingMeetings), 0);
            int suggest = pref.getInt(getResources().getString(R.string.suggestNow), 0);
            int remind = pref.getInt(getResources().getString(R.string.remind), 0);
            if(upcoming != 0)
            {
                upcomingInt = upcoming;
//                Intent intent = new Intent(this, MeetingNotificationService.class);
//                intent.putExtra(getResources().getString(R.string.upcomingMeetings), upcomingInt);
//                intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
//                intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
//                startService(intent);
            }
            if(suggest != 0)
            {
                suggestInt = suggest;
                scheduleAlarm(suggestInt);
            }
            if(remind != 0)
            {
                remindMeInt = remind;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsPop.class);
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
            intent.putExtra(getResources().getString(R.string.location), location);
            startActivity(intent);
        }
        return true;
    }

    public void getLocationPermission()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationHandler = new LocationHandler();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                ,getResources().getInteger(R.integer.REQUEST_LOCATION_PERMISSION));
            }
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationHandler);
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Double lat = myLocation.getLatitude();
        Double lon = myLocation.getLongitude();
        location = lat + ":" + lon;
    }

    public void getContactPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Allow FriendTracker to access your contacts?");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , getResources().getInteger(R.integer.REQUEST_CONTACT_PERMISSION));
                        }
                    });
                    builder.show();
                }
                else
                {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            getResources().getInteger(R.integer.REQUEST_CONTACT_PERMISSION));
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults)
    {
         if(requestCode == getResources().getInteger(R.integer.REQUEST_CONTACT_PERMISSION))
         {
             getContactPermission();
         }
         else if(requestCode == getResources().getInteger(R.integer.REQUEST_LOCATION_PERMISSION))
         {
             getLocationPermission();
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
        try
        {
            if(receiver != null)
            {
                unregisterReceiver(receiver);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
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
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.main));
        intent.putExtra(getResources().getString(R.string.location), location);
        startActivity(intent);
   }

    private class DisContactMainController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, DisplayContactActivity.class);
            intent.putExtra(getResources().getString(R.string.className),getResources().getString(R.string.contactList));
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
            intent.putExtra(getResources().getString(R.string.location), location);
            startActivity(intent);
        }
    }

    private class DisMeetingMainController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, DisplayMeetingActivity.class);
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
            intent.putExtra(getResources().getString(R.string.location), location);
            startActivity(intent);
        }
    }

    private class SuggestController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(checkNetworkState)
            {
                boolean check = checkEmptyContacts();
                if (check) {
                    Intent intent = new Intent(MainActivity.this, SuggestMeetingActivity.class);
                    intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
                    intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
                    intent.putExtra(getResources().getString(R.string.location), location);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.noContacts), Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.networkFalse),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setFriendManager(FriendManager friendManager)
    {
        this.friendManager = friendManager;
    }

    public void setMeetingManager(MeetingManager meetingManager)
    {
        this.meetingManager = meetingManager;
    }

    public boolean checkEmptyContacts()
    {
        if(friendManager.getFriendList().size() ==0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private void callBroadCastReceiver()
    {
        if (receiver == null) {

            receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle extras = intent.getExtras();
                    NetworkInfo info = (NetworkInfo) extras.getParcelable(getResources().getString(R.string.networkInfo));
                    NetworkInfo.State state = info.getState();
                    if (state == NetworkInfo.State.CONNECTED)
                    {
                        Toast.makeText(context, getResources().getString(R.string.networkTrue), Toast.LENGTH_SHORT).show();
                        checkNetworkState = true;
                    }
                    else
                        {
                        Toast.makeText(context, getResources().getString(R.string.networkFalse), Toast.LENGTH_SHORT).show();
                        checkNetworkState = false;
                    }
                }
            };
            final IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(receiver, filter);
        }
    }

    private void scheduleAlarm(long seconds) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, SuggestMeetingReceiver.class);
        intent.putExtra(getResources().getString(R.string.location), location);
        intent.putExtra(getResources().getString(R.string.networkInfo), checkNetworkState);
        intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), seconds*1000, pendingIntent);
    }
}
