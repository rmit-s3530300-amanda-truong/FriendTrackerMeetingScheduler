package com.example.amanda.friendtrackerappass1.View;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Parcelable;
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

import com.example.amanda.friendtrackerappass1.AsyncTask.RetrieveListsAsync;
import com.example.amanda.friendtrackerappass1.Controller.MainMenuController;
import com.example.amanda.friendtrackerappass1.Model.DBHandler;
import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.LocationHandler;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private String LOG_TAG = this.getClass().getName();
    private MainMenuController controller;
    private FriendManager friendManager;
    private MeetingManager meetingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "onCreate()");

        Button bAddContact = (Button) findViewById(R.id.bAddContact);
        Button bDisContact = (Button) findViewById(R.id.btDisplayContact);
        Button bDisMeeting = (Button) findViewById(R.id.btDisplayMeeting);
        TextView tvContact = (TextView) findViewById(R.id.tvContact);
        TextView tvInstruct = (TextView) findViewById(R.id.tvInstruct);

        controller = new MainMenuController(this);

        Bundle buttonInfo = getIntent().getExtras();
        if(buttonInfo != null)
        {
            String buttonStr = buttonInfo.getString(getResources().getString(R.string.addContact));
            if(buttonStr != null)
            {
                if(buttonStr.equals(getResources().getString(R.string.add_button)))
                {
                    controller.addContact();
                }
            }
            friendManager = (FriendManager) buttonInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) buttonInfo.getSerializable(getResources().getString(R.string.meetingManager));
        }
        else
        {
            friendManager = new FriendManager();
            meetingManager = new MeetingManager();
        }

        getLocation();
        getContactPermission();

        RetrieveListsAsync asyncTask = new RetrieveListsAsync(this, friendManager, meetingManager);
        asyncTask.execute();

        bAddContact.setOnClickListener(controller);
        bDisContact.setOnClickListener(new DisContactMainController());
        bDisMeeting.setOnClickListener(new DisMeetingMainController());
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
            startActivity(intent);
        }
        return true;
    }

    public void getLocation()
    {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationHandler locationHandler = new LocationHandler();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                ,getResources().getInteger(R.integer.REQUEST_LOCATION_PERMISSION));
            }
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationHandler);
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
             getLocation();
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
        intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.main));
        startActivity(intent);
   }

    private class DisContactMainController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, DisplayContactActivity.class);
            intent.putExtra(getResources().getString(R.string.className),getResources().getString(R.string.contactList));
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
            startActivity(intent);
        }
    }

    private class DisMeetingMainController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, DisplayMeetingActivity.class);
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
            startActivity(intent);
        }
    }
}
