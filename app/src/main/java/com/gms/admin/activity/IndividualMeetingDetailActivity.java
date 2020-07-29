package com.gms.admin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.gms.admin.R;
import com.gms.admin.bean.support.IndividualMeeting;
import com.gms.admin.interfaces.DialogClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IndividualMeetingDetailActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private static final String TAG = IndividualMeetingDetailActivity.class.getName();

    private IndividualMeeting meeting;
    private ImageView newsImage;
    private TextView meetingTitle, meetingDetail, meetingDate, meetingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_meeting_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

        meeting = (IndividualMeeting) getIntent().getSerializableExtra("meetingObj");

        meetingTitle = (TextView) findViewById(R.id.meeting_title);
        meetingDetail = (TextView) findViewById(R.id.meeting_details);
        meetingDate = (TextView) findViewById(R.id.meeting_date);
        meetingStatus = (TextView) findViewById(R.id.meeting_status);


        meetingTitle.setText(capitalizeString(meeting.getmeeting_title()));
        meetingDetail.setText(capitalizeString(meeting.getmeeting_detail()));
        meetingDate.setText(getserverdateformat(meeting.getmeeting_date()));
        meetingStatus.setText(capitalizeString(meeting.getmeeting_status()));
        if (meeting.getmeeting_status().equalsIgnoreCase("COMPLETED")) {
            meetingStatus.setTextColor(ContextCompat.getColor(this, R.color.completed_meeting));
        } else {
            meetingStatus.setTextColor(ContextCompat.getColor(this, R.color.requested));
        }
    }

    private String getserverdateformat(String dd) {
        String serverFormatDate = "";
        if (dd != null && dd != "") {

            String date = dd;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date testDate = null;
            try {
                testDate = formatter.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            serverFormatDate = sdf.format(testDate);
            System.out.println(".....Date..." + serverFormatDate);
        }
        return serverFormatDate;
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

}