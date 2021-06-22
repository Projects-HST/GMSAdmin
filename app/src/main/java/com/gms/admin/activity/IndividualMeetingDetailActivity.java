package com.gms.admin.activity;

import android.graphics.drawable.GradientDrawable;
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
    private TextView meetingTitle, meetingDetail, reqDate, meetingDate, meetingStatus, createdBy;
    GradientDrawable drawable;

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

        drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(8);

        meetingTitle = (TextView) findViewById(R.id.meeting_title);
        meetingDetail = (TextView) findViewById(R.id.meeting_details);
        reqDate = (TextView) findViewById(R.id.created_on);
        createdBy = (TextView) findViewById(R.id.user_status);
        meetingDate = (TextView) findViewById(R.id.schedule_date);
        meetingStatus = (TextView) findViewById(R.id.meet_status);
        meetingStatus.setBackground(drawable);


        meetingTitle.setText(capitalizeString(meeting.getmeeting_title()));
        meetingDetail.setText(capitalizeString(meeting.getmeeting_detail()));
        reqDate.setText(meeting.getcreated_at());
        createdBy.setText(capitalizeString(meeting.getCreated_user()));
        meetingStatus.setText(capitalizeString(meeting.getmeeting_status()));

        if (meeting.getmeeting_status().equalsIgnoreCase("REQUESTED")) {
            meetingDate.setVisibility(View.GONE);
            meetingStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.schedule));
        } else {
            if (meeting.getmeeting_status().equalsIgnoreCase("SCHEDULED")) {
                meetingDate.setVisibility(View.VISIBLE);
                meetingDate.setText(("Meeting Date" + " : " + meeting.getmeeting_date()));
                meetingStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.requested));
            }else {
                meetingDate.setVisibility(View.VISIBLE);
                meetingDate.setText(("Meeting Date" + " : " + meeting.getmeeting_date()));
                meetingStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.completed_meeting));
            }
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