package com.example.amanda.friendtrackerappass1.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.R;

import java.util.ArrayList;

/**
 * Created by amanda on 3/09/2017.
 */

public class InviteFriendAdapter extends ArrayAdapter<Friend> {
    private ArrayList<Friend> friendList;
    private Context context;

    public InviteFriendAdapter(Context context, int textViewResourceId, ArrayList<Friend> objects)
    {
        super(context, textViewResourceId, objects);
        this.context = context;
        friendList = objects;
    }

    @Override
    public int getCount()
    {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.activity_invite_list_view, parent, false);
        TextView tv = (TextView) convertView.findViewById(R.id.textViewInvite);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);

        tv.setText(friendList.get(position).getName());
        return convertView;
    }
}
