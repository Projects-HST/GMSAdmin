package com.gms.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.gms.admin.R;
import com.gms.admin.activity.ReportBirthdayActivity;
import com.gms.admin.activity.ReportCategoryActivity;
import com.gms.admin.activity.ReportConstituentActivity;
import com.gms.admin.activity.ReportFestivalActivity;
import com.gms.admin.activity.ReportLocationActivity;
import com.gms.admin.activity.ReportMeetingActivity;
import com.gms.admin.activity.ReportStaffActivity;
import com.gms.admin.activity.ReportStatusActivity;
import com.gms.admin.activity.ReportSubCategoryActivity;
import com.gms.admin.activity.ReportVideoActivity;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.serviceinterfaces.IServiceListener;

import org.json.JSONObject;

public class ReportFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {
    private View view;
    private SearchView searchView;
    LinearLayout statusLayout, grievancesLayout, meetingLayout, festivalLayout, constituentLayout, videoLayout,staffLayout, birthdayLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_report, container, false);


        statusLayout = view.findViewById(R.id.status_layout);
        grievancesLayout = view.findViewById(R.id.grievance_report_layout);
        meetingLayout = view.findViewById(R.id.meeting_report_layout);
        birthdayLayout = view.findViewById(R.id.birthday_layout);
        festivalLayout = view.findViewById(R.id.festival_wishes_layout);
        constituentLayout = view.findViewById(R.id.constituent_report_layout);
        videoLayout = view.findViewById(R.id.video_layout);
        staffLayout = view.findViewById(R.id.staff_layout);

        statusLayout.setOnClickListener(this);
        grievancesLayout.setOnClickListener(this);
        meetingLayout.setOnClickListener(this);
        birthdayLayout.setOnClickListener(this);
        festivalLayout.setOnClickListener(this);
        constituentLayout.setOnClickListener(this);
        videoLayout.setOnClickListener(this);
        staffLayout.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        if (v == statusLayout) {
            Intent i = new Intent(statusLayout.getContext(), ReportStatusActivity.class);
            startActivity(i);
        }if (v == grievancesLayout) {
            Intent i = new Intent(grievancesLayout.getContext(), ReportCategoryActivity.class);
            startActivity(i);
        }if (v == meetingLayout) {
            Intent i = new Intent(meetingLayout.getContext(), ReportMeetingActivity.class);
            startActivity(i);
        }if (v == birthdayLayout) {
            Intent i = new Intent(birthdayLayout.getContext(), ReportBirthdayActivity.class);
            startActivity(i);
        }if (v == festivalLayout) {
            Intent i = new Intent(festivalLayout.getContext(), ReportFestivalActivity.class);
            startActivity(i);
        }if (v == constituentLayout) {
            Intent i = new Intent(constituentLayout.getContext(), ReportConstituentActivity.class);
            startActivity(i);
        }if (v == videoLayout) {
            Intent i = new Intent(videoLayout.getContext(), ReportVideoActivity.class);
            startActivity(i);
        }if (v == staffLayout) {
            Intent i = new Intent(videoLayout.getContext(), ReportStaffActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}
