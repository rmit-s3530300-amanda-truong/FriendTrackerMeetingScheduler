package com.example.amanda.friendtrackerappass1.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.amanda.friendtrackerappass1.Model.Friend;
import com.example.amanda.friendtrackerappass1.Model.FriendManager;
import com.example.amanda.friendtrackerappass1.R;

import java.util.Date;

public class EditContactActivity extends AppCompatActivity {

    private String name;
    private String email;
    private String id;
    private int year;
    private int month;
    private int date;
    private FriendManager friendManager;
    private boolean saved = false;
    private EditText etEditName;
    private EditText etEditEmail;
    private DatePicker datePicker;
    private Friend friend;
    private String LOG_TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        Bundle contactInfo = getIntent().getExtras();
        if(contactInfo!=null)
        {
            name = contactInfo.getString(getResources().getString(R.string.name));
            email = contactInfo.getString(getResources().getString(R.string.email));
            id = contactInfo.getString(getResources().getString(R.string.id));
            friendManager = (FriendManager) contactInfo.getSerializable(getResources().getString(R.string.friendManager));
        }

        friend = friendManager.getFriend(id);

        etEditName = (EditText) findViewById(R.id.etEditName);
        etEditEmail = (EditText) findViewById(R.id.etEditEmail);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        Date birthday = friend.getBirthday();
        if(birthday != null)
        {
            String[] birthdayArray = birthday.toString().split(" ");
            int newMonth = 0;
            if(birthdayArray[1].equals("Jan"))
            {
                newMonth = 1;
            }
            else if(birthdayArray[1].equals("Feb"))
            {
                newMonth = 2;
            }
            else if(birthdayArray[1].equals("Mar"))
            {
                newMonth = 3;
            }
            else if(birthdayArray[1].equals("Apr"))
            {
                newMonth = 4;
            }
            else if(birthdayArray[1].equals("May"))
            {
                newMonth = 5;
            }
            else if(birthdayArray[1].equals("Jun"))
            {
                newMonth = 6;
            }
            else if(birthdayArray[1].equals("Jul"))
            {
                newMonth = 7;
            }
            else if(birthdayArray[1].equals("Aug"))
            {
                newMonth = 8;
            }
            else if(birthdayArray[1].equals("Sep"))
            {
                newMonth = 9;
            }
            else if(birthdayArray[1].equals("Oct"))
            {
                newMonth = 10;
            }
            else if(birthdayArray[1].equals("Nov"))
            {
                newMonth = 11;
            }
            else if(birthdayArray[1].equals("Dec"))
            {
                newMonth = 12;
            }
            datePicker.updateDate(Integer.parseInt(birthdayArray[5]), newMonth, Integer.parseInt(birthdayArray[2]));
        }
        etEditName.setText(name);
        etEditEmail.setText(email);

        Button btSave = (Button) findViewById(R.id.btSave);
        Button btGoBack = (Button) findViewById(R.id.btGoBack);
        btSave.setOnClickListener(new SaveController());
        btGoBack.setOnClickListener(new BackController());
    }

    private class SaveController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            date = datePicker.getDayOfMonth();
            month = datePicker.getMonth();
            year = datePicker.getYear();
            friend.editName(etEditName.getText().toString());
            friend.editEmail(etEditEmail.getText().toString());
            friend.editBirthday(date, month, year);
            Log.i(LOG_TAG,friend.getName());
            Log.i(LOG_TAG, friend.getEmail());
            AlertDialog.Builder alert = new AlertDialog.Builder(EditContactActivity.this);
            alert.setTitle("Saved Information");
            alert.setMessage("Your contact information has been saved!");
            alert.setPositiveButton("OK", null);
            alert.show();
            saved = true;
        }
    }

    private class BackController implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(saved == true)
            {
                Log.i(LOG_TAG, "back" +  etEditName.getText().toString());
                Log.i(LOG_TAG, "back" + etEditEmail.getText().toString());
                Intent intent = new Intent(EditContactActivity.this, DisplayContactActivity.class);
                intent.putExtra(getResources().getString(R.string.name),etEditName.getText().toString());
                intent.putExtra(getResources().getString(R.string.email), etEditEmail.getText().toString());
                intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
                intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.edit));
                startActivity(intent);
            }
            else
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditContactActivity.this);
                alert.setTitle("Go Back");
                alert.setMessage("You haven't saved your changes, are you sure you wish to go back?");
                alert.setPositiveButton("Yes", new ConfirmController());
                alert.setNegativeButton("No", null);
                alert.show();
            }
        }

        private class ConfirmController implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(EditContactActivity.this, DisplayContactActivity.class);
                intent.putExtra(getResources().getString(R.string.name), name);
                intent.putExtra(getResources().getString(R.string.email), email);
                intent.putExtra(getResources().getString(R.string.friendManager), friendManager);
                intent.putExtra(getResources().getString(R.string.className), getResources().getString(R.string.edit));
                startActivity(intent);
            }
        }
    }
}
