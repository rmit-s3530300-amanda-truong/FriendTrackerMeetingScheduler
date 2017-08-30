package com.example.amanda.friendtrackerappass1.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DisplayContactActivity extends AppCompatActivity {

    private String LOG_TAG = this.getClass().getName();
    private ArrayList<Friend> friendList;
    private ArrayList<String> friendNameList;
    private String name;
    private String email;
    private Date birthday;
    private FriendManager friendManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        Log.i(LOG_TAG, "onCreate()");

        TextView tvFriendList = (TextView) findViewById(R.id.tvFriendList);
        tvFriendList.setText(R.string.friendList);
        friendManager = new FriendManager();
        friendList = friendManager.getFriendList();
        friendNameList = friendManager.getFriendNameList();

        //https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application
        Bundle contactInfo = getIntent().getExtras();
        if(contactInfo!=null)
        {
            name = contactInfo.getString("name");
            email = contactInfo.getString("email");
        }
        addContact(name, email);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, friendNameList);

        ListView lvFriendList = (ListView) findViewById(R.id.lvFriendList);
        lvFriendList.setAdapter(adapter);
        registerForContextMenu(lvFriendList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.lvFriendList)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        }
    }

    @Override
    protected void onRestart()
    {
        Log.i(LOG_TAG, "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStart()
    {
        Log.i(LOG_TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        Log.i(LOG_TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        Log.i(LOG_TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        Log.i(LOG_TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Log.i(LOG_TAG, "onDestroy()");
        super.onDestroy();
    }

    public String generateID()
    {
        String uuid = UUID.randomUUID().toString();
        //uuid.replaceAll("-","");
        return uuid;
    }

    public void addContact(String name, String email)
    {
        String id = generateID();
        birthday = null;
        friendNameList.add(name);
        Friend friend = new Friend(id, name, email, birthday);
        friendList.add(friend);
    }

}
