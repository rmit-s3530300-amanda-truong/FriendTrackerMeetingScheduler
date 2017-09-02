package com.example.amanda.friendtrackerappass1.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.R;

import java.util.ArrayList;

/**
 * Created by amanda on 2/09/2017.
 */

public class FriendListAdapter extends ArrayAdapter<Friend> {
    private ArrayList<Friend> friendList;

    public FriendListAdapter(Context context, int textViewResourceId, ArrayList<Friend> objects)
    {
        super(context, textViewResourceId, objects);
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
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_list_view, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(friendList.get(position).getName());
        return v;
    }
}
