package com.gms.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gms.admin.R;
import com.gms.admin.bean.support.Grievance;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GrievanceDetailActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = IndividualGrievanceDetailActivity.class.getName();

    private String grievance, constituent;
    private TextView txtConstituency, seekerType, txtPetitionEnquiry, petitionEnquiryNo, grievanceName,
            grievanceSubCat, grievanceDesc, createdOn, updatedOn, grievanceStatus;
    private TextView history, profile;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_detail);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        grievance = getIntent().getStringExtra("grievanceObj");

        txtConstituency = (TextView) findViewById(R.id.text_constituency);
        seekerType = (TextView) findViewById(R.id.seeker_type);
        txtPetitionEnquiry = (TextView) findViewById(R.id.txt_petition_enquiry);
        petitionEnquiryNo = (TextView) findViewById(R.id.petition_enquiry_number);
        grievanceName = (TextView) findViewById(R.id.grievance_name);
        grievanceSubCat = (TextView) findViewById(R.id.grievance_sub_category);
        grievanceDesc = (TextView) findViewById(R.id.grievance_description);
        createdOn = (TextView) findViewById(R.id.created_on);
        updatedOn = (TextView) findViewById(R.id.updated_on);
        grievanceStatus = (TextView) findViewById(R.id.grievance_status);

        history = findViewById(R.id.msg_history);
        history.setOnClickListener(this);
        profile = findViewById(R.id.view_profile);
        profile.setOnClickListener(this);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);


        getGrievanceDetail();

    }

    private void getGrievanceDetail() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(GMSConstants.KEY_GRIEVANCE_ID, grievance);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_GRIEVANCE_DETAILS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {
        if (v == history) {
            Intent intent = new Intent(this, MessageHistoryActivity.class);
            intent.putExtra("grievanceObj", grievance);
            startActivity(intent);
        }if (v == profile) {
            Intent intent = new Intent(this, ConstituentGrievanceProfileActivity.class);
            intent.putExtra("constituentObj", constituent);
            startActivity(intent);
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private boolean validateResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(GMSConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            try {
                JSONArray arraydata = response.getJSONArray("grievance_details");
                JSONObject data = arraydata.getJSONObject(0);
                seekerType.setText(data.getString("seeker_info"));

                if (data.getString("grievance_type").equalsIgnoreCase("P")) {
                    txtPetitionEnquiry.setText(getString(R.string.petition_num));
                } else {
                    txtPetitionEnquiry.setText(getString(R.string.enquiry_num));
                    grievanceDesc.setVisibility(View.GONE);
                    findViewById(R.id.grievance_des_txt).setVisibility(View.GONE);
                }
                txtConstituency.setText(data.getString("paguthi_name"));
                petitionEnquiryNo.setText(data.getString("petition_enquiry_no"));
                grievanceName.setText(data.getString("grievance_name"));
                grievanceSubCat.setText(data.getString("sub_category_name"));
                grievanceDesc.setText(data.getString("description"));
                grievanceStatus.setText(data.getString("status"));
                createdOn.setText(data.getString("created_at"));
                updatedOn.setText(data.getString("updated_at"));
                constituent = data.getString("constituent_id");
                if (data.getString("status").equalsIgnoreCase("COMPLETED")) {
                    grievanceStatus.setTextColor(ContextCompat.getColor(this, R.color.completed_grievance));
                } else {
                    grievanceStatus.setTextColor(ContextCompat.getColor(this, R.color.requested));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.totalLayout.setForeground(ContextCompat.getDrawable(context, R.drawable.shadow_foreground));
//            }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }
}
