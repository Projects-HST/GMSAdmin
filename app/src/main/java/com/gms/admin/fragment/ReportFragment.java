package com.gms.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.gms.admin.R;
import com.gms.admin.activity.IndividualDocumentsActivity;
import com.gms.admin.activity.IndividualGrievanceActivity;
import com.gms.admin.activity.IndividualInteractionActivity;
import com.gms.admin.activity.IndividualMeetingActivity;
import com.gms.admin.activity.IndividualPlantDonationActivity;
import com.gms.admin.activity.IndividualProfileActivity;
import com.gms.admin.activity.ReportBirthdayActivity;
import com.gms.admin.activity.ReportCategoryActivity;
import com.gms.admin.activity.ReportLocationActivity;
import com.gms.admin.activity.ReportMeetingActivity;
import com.gms.admin.activity.ReportStaffActivity;
import com.gms.admin.activity.ReportStatusActivity;
import com.gms.admin.activity.ReportSubCategoryActivity;
import com.gms.admin.activity.SearchResultActivity;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.PreferenceStorage;

import org.json.JSONObject;

public class ReportFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {
    private View view;
    private SearchView searchView;
    LinearLayout statusLayout, categoryLayout, subcategoryLayout, locationLayout, meetingLayout, staffLayout, birthdayLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_report, container, false);


        statusLayout = view.findViewById(R.id.status_layout);
        categoryLayout = view.findViewById(R.id.category_layout);
        subcategoryLayout = view.findViewById(R.id.sub_category_layout);
        locationLayout = view.findViewById(R.id.location_layout);
        meetingLayout = view.findViewById(R.id.meeting_layout);
        staffLayout = view.findViewById(R.id.staff_layout);
        birthdayLayout = view.findViewById(R.id.birthday_layout);

        statusLayout.setOnClickListener(this);
        categoryLayout.setOnClickListener(this);
        subcategoryLayout.setOnClickListener(this);
        locationLayout.setOnClickListener(this);
        meetingLayout.setOnClickListener(this);
        staffLayout.setOnClickListener(this);
        birthdayLayout.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        if (v == statusLayout) {
            Intent i = new Intent(statusLayout.getContext(), ReportStatusActivity.class);
            startActivity(i);
        }if (v == categoryLayout) {
            Intent i = new Intent(categoryLayout.getContext(), ReportCategoryActivity.class);
            startActivity(i);
        }if (v == subcategoryLayout) {
            Intent i = new Intent(subcategoryLayout.getContext(), ReportSubCategoryActivity.class);
            startActivity(i);
        }if (v == locationLayout) {
            Intent i = new Intent(locationLayout.getContext(), ReportLocationActivity.class);
            startActivity(i);
        }if (v == meetingLayout) {
            Intent i = new Intent(meetingLayout.getContext(), ReportMeetingActivity.class);
            startActivity(i);
        }if (v == staffLayout) {
            Intent i = new Intent(staffLayout.getContext(), ReportStaffActivity.class);
            startActivity(i);
        }if (v == birthdayLayout) {
            Intent i = new Intent(birthdayLayout.getContext(), ReportBirthdayActivity.class);
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
