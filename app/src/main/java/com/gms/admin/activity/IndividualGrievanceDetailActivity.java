package com.gms.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gms.admin.R;
import com.gms.admin.bean.support.Grievance;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.utils.PreferenceStorage;

public class IndividualGrievanceDetailActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private static final String TAG = IndividualGrievanceDetailActivity.class.getName();

    private Grievance grievance;
    private TextView txtConstituency, seekerType, txtPetitionEnquiry, petitionEnquiryNo, grievanceName,
            grievanceSubCat, grievanceDesc, grievanceReference, grievanceStatus;
    private LinearLayout history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_grievance_detail);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        grievance = (Grievance) getIntent().getSerializableExtra("serviceObj");

        txtConstituency = (TextView) findViewById(R.id.text_constituency);
        seekerType = (TextView) findViewById(R.id.seeker_type);
        txtPetitionEnquiry = (TextView) findViewById(R.id.txt_petition_enquiry);
        petitionEnquiryNo = (TextView) findViewById(R.id.petition_enquiry_number);
        grievanceName = (TextView) findViewById(R.id.grievance_name);
        grievanceSubCat = (TextView) findViewById(R.id.grievance_sub_category);
        grievanceDesc = (TextView) findViewById(R.id.grievance_description);
        grievanceReference = (TextView) findViewById(R.id.grievance_reference_note);
        grievanceStatus = (TextView) findViewById(R.id.grievance_status);

        history = findViewById(R.id.view_message_history);
        history.setOnClickListener(this);

        txtConstituency.setText(PreferenceStorage.getConstituencyName(this));
        seekerType.setText(grievance.getseeker_info());

        if (grievance.getgrievance_type().equalsIgnoreCase("P")) {
            txtPetitionEnquiry.setText(getString(R.string.petition_num));
        } else {
            txtPetitionEnquiry.setText(getString(R.string.enquiry_num));
            grievanceDesc.setVisibility(View.GONE);
            findViewById(R.id.grievance_des_txt).setVisibility(View.GONE);
        }
        petitionEnquiryNo.setText(grievance.getpetition_enquiry_no());
        grievanceName.setText(grievance.getgrievance_name());
        grievanceSubCat.setText(grievance.getSub_category_name());
        grievanceDesc.setText(grievance.getdescription());
        grievanceReference.setText(grievance.getreference_note());
        grievanceStatus.setText(grievance.getstatus());

        if (grievance.getstatus().equalsIgnoreCase("COMPLETED")) {
            grievanceStatus.setTextColor(ContextCompat.getColor(this, R.color.completed_grievance));
        } else {
            grievanceStatus.setTextColor(ContextCompat.getColor(this, R.color.requested));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.totalLayout.setForeground(ContextCompat.getDrawable(context, R.drawable.shadow_foreground));
//            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v == history) {
            Intent intent = new Intent(this, MessageHistoryActivity.class);
            intent.putExtra("grievanceObj", grievance.getid());
            startActivity(intent);
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

}