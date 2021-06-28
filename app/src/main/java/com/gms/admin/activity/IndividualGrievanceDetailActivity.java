package com.gms.admin.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.gms.admin.R;
import com.gms.admin.bean.support.Grievance;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.utils.PreferenceStorage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IndividualGrievanceDetailActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener {

    private static final String TAG = IndividualGrievanceDetailActivity.class.getName();

    private Grievance grievance;
    private TextView txtConstituency, seekerType, txtPetition, txtEnquiry, petitionEnquiryNo, grievanceName,
            grievanceSubCat, grievanceDesc, grievanceReference, grievanceStatus, createdOn, updatedOn;
    private TextView history;
    int colour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_grievance_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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

        grievance = (Grievance) getIntent().getSerializableExtra("serviceObj");

        colour = Color.parseColor(PreferenceStorage.getAppBaseColor(this));

        txtConstituency = (TextView) findViewById(R.id.text_constituency);
        seekerType = (TextView) findViewById(R.id.seeker_type);
        txtPetition = (TextView) findViewById(R.id.txt_petition);
        txtEnquiry = (TextView) findViewById(R.id.txt_enquiry);
        petitionEnquiryNo = (TextView) findViewById(R.id.petition_enquiry_number);
        grievanceName = (TextView) findViewById(R.id.grievance_name);
        grievanceSubCat = (TextView) findViewById(R.id.grievance_sub_category);
        grievanceDesc = (TextView) findViewById(R.id.grievance_description);
        grievanceReference = (TextView) findViewById(R.id.grievance_reference_note);
        grievanceStatus = (TextView) findViewById(R.id.grievance_status);
        createdOn = (TextView) findViewById(R.id.created_on);
        updatedOn = (TextView) findViewById(R.id.updated_on);

        history = findViewById(R.id.view_message_history);
        history.setBackgroundColor(colour);
        history.setOnClickListener(this);

        txtConstituency.setText(capitalizeString(PreferenceStorage.getConstituencyName(this)));
        seekerType.setText(capitalizeString(grievance.getseeker_info()));

        if (grievance.getgrievance_type().equalsIgnoreCase("P")) {
            txtEnquiry.setVisibility(View.GONE);
            txtPetition.setText(getString(R.string.petition_num));
        } else {
            txtPetition.setVisibility(View.GONE);
            txtEnquiry.setVisibility(View.VISIBLE);
            txtEnquiry.setText(getString(R.string.enquiry_num));
//            findViewById(R.id.desc_layout).setVisibility(View.GONE);
        }
        petitionEnquiryNo.setText(grievance.getpetition_enquiry_no());
        grievanceName.setText(capitalizeString(grievance.getgrievance_name()));
        grievanceSubCat.setText(capitalizeString(grievance.getSub_category_name()));
        grievanceDesc.setText(capitalizeString(grievance.getdescription()));
        grievanceReference.setText(capitalizeString(grievance.getreference_note()));
        grievanceStatus.setText(capitalizeString(grievance.getstatus()));

        createdOn.setText(getserverdateformat(grievance.getCreated_at()));
        updatedOn.setText(getserverdateformat(grievance.getUpdated_at()));
        if (grievance.getstatus().equalsIgnoreCase("COMPLETED")) {
            grievanceStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_round_completed));
        } else {
            grievanceStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_round_processing));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.totalLayout.setForeground(ContextCompat.getDrawable(context, R.drawable.shadow_foreground));
//            }
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