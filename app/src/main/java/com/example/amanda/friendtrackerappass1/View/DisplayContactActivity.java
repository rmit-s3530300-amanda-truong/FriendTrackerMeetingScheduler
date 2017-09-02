package com.example.amanda.friendtrackerappass1.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.Controller.FriendListController;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.R;

public class DisplayContactActivity extends AppCompatActivity {

    private String LOG_TAG = this.getClass().getName();

    private String name;
    private String email;
    private String id;
    private String callingClass;
    private FriendListAdapter adapter;
    private FriendListController friendListController;
    private FriendManager friendManager;
    String[] menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        Log.i(LOG_TAG, "onCreate()");

        TextView tvFriendList = (TextView) findViewById(R.id.tvFriendList);
        tvFriendList.setText(R.string.friendList);

        Bundle contactInfo = getIntent().getExtras();
        if(contactInfo!=null)
        {
            name = contactInfo.getString(getResources().getString(R.string.name));
            email = contactInfo.getString(getResources().getString(R.string.email));
            friendManager = (FriendManager) contactInfo.getSerializable(getResources().getString(R.string.friendManager));
            callingClass = contactInfo.getString(getResources().getString(R.string.className));
            Log.i(LOG_TAG, "CALLING CLASS" + callingClass);
        }
//        if(callingClass.equals("displayContact"))
//        {
//            if(friendManager == null)
//            {
//                friendManager = new FriendManager();
//                friendListController = new FriendListController(this, friendManager);
//            }
//        }
//        else
//        {
//            friendListController = new FriendListController(this, friendManager);
//            if(callingClass.equals(getResources().getString(R.string.main)))
//            {
//                friendListController.addContact(name, email);
//                id = friendListController.getID();
//            }
//
//            adapter = friendListController.getAdapter();
//
//            ListView lvFriendList = (ListView) findViewById(R.id.lvFriendList);
//            lvFriendList.setAdapter(adapter);
//            registerForContextMenu(lvFriendList);
//        }
        friendListController = new FriendListController(this, friendManager);
        friendListController = new FriendListController(this, friendManager);
        if(callingClass.equals(getResources().getString(R.string.main)))
        {
            friendListController.addContact(name, email);
            id = friendListController.getID();
        }

        adapter = friendListController.getAdapter();

        ListView lvFriendList = (ListView) findViewById(R.id.lvFriendList);
        lvFriendList.setAdapter(adapter);
        registerForContextMenu(lvFriendList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.lvFriendList)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(getResources().getString(R.string.options));
            menuItems = getResources().getStringArray(R.array.menu);
            for(int i=0; i < menuItems.length; i++)
            {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String menuItemName = menuItems[menuItemIndex];
        int listPos = info.position;
        if(menuItemName.equals(menuItems[0]))
        {
            Intent intent = new Intent(this, EditContactActivity.class);
            intent.putExtra(getResources().getString(R.string.name), adapter.getItem(listPos).getName());
            intent.putExtra(getResources().getString(R.string.email), adapter.getItem(listPos).getEmail());
            intent.putExtra(getResources().getString(R.string.id), adapter.getItem(listPos).getID());
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            startActivity(intent);
        }
        else if(menuItemName.equals(menuItems[1]))
        {
            friendManager.removeFriend(adapter.getItem(listPos));
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.action_addContact)
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(getResources().getString(R.string.addContact),"add button");
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            startActivity(intent);
        }
        return true;
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
}
