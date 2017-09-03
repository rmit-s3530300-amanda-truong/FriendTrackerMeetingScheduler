package com.example.amanda.friendtrackerappass1.View;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.Model.MeetingManager;
import com.example.amanda.friendtrackerappass1.R;
import com.example.amanda.friendtrackerappass1.ViewModel.InviteFriendAdapter;

public class InviteFriendActivity extends Activity {

    private InviteFriendAdapter adapter;

    private FriendManager friendManager;
    private MeetingManager meetingManager;
    private ListView lvFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);

        Bundle contactInfo = getIntent().getExtras();
        if(contactInfo != null)
        {
            friendManager = (FriendManager) contactInfo.getSerializable(getResources().getString(R.string.friendManager));
            meetingManager = (MeetingManager) contactInfo.getSerializable(getResources().getString(R.string.meetingManager));
        }
        Button btInvite = (Button) findViewById(R.id.btInvite);
        adapter = new InviteFriendAdapter(this, R.layout.activity_invite_list_view, friendManager.getFriendList());

        lvFriendList = (ListView) findViewById(R.id.listViewInvite);
        lvFriendList.setAdapter(adapter);
        lvFriendList.setOnClickListener(new AddInvitedController());
        btInvite.setOnClickListener(new SubmitController());
    }

    private class AddInvitedController implements View.OnClickListener {

        @Override
        public void onClick(View view) {

        }
    }

    private class SubmitController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(InviteFriendActivity.this, AddMeetingActivity.class);
            intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
            intent.putExtra(getResources().getString(R.string.meetingManager), meetingManager);
        }
    }
}
