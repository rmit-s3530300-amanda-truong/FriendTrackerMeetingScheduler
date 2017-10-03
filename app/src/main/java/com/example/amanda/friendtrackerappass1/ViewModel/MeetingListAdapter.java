package com.example.amanda.friendtrackerappass1.ViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.Meeting;
import com.example.amanda.friendtrackerappass1.R;

import java.util.ArrayList;

/**
 * Created by amanda on 4/09/2017.
 */

public class MeetingListAdapter extends ArrayAdapter<Meeting> {
    private ArrayList<Meeting> meetingList;

    public MeetingListAdapter(Context context, int textViewResourceId, ArrayList<Meeting> objects)
    {
        super(context, textViewResourceId, objects);
        meetingList = objects;
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
        TextView tv = (TextView) v.findViewById(R.id.textView1);
        tv.setText(meetingList.get(position).getTitle());
        return v;
    }
}
