package com.gms.admin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.gms.admin.R;
import com.gms.admin.bean.support.Grievance;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.GMSValidator;
import com.gms.admin.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GrievanceDetailActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = IndividualGrievanceDetailActivity.class.getName();

    private String grievance, constituent;
    private TextView txtConstituency, seekerType, txtPetitionEnquiry, petitionEnquiryNo, grievanceName,
            grievanceSubCat, grievanceDesc, createdOn, updatedOn, grievanceReference, grievanceStatus;
    private TextView history, profile;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    String resjj = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_detail);

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
        grievanceReference = (TextView) findViewById(R.id.grievance_reference_note);

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
        resjj = "griv_det";
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

    private void getConstituentData() {
        resjj = "consti_profile";
        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(GMSConstants.KEY_CONSTITUENT_ID, constituent);
            String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_CONSTITUENT_DETAIL;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == history) {
            Intent intent = new Intent(this, MessageHistoryActivity.class);
            intent.putExtra("grievanceObj", grievance);
            startActivity(intent);
        }
        if (v == profile) {
            getConstituentData();
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
                if (resjj.equalsIgnoreCase("consti_profile")) {
                    JSONObject data = response.getJSONArray("constituent_details").getJSONObject(0);
                    String name = capitalizeString(data.getString("full_name"));
                    String number = capitalizeString(data.getString("mobile_no"));
                    String constituency = capitalizeString(data.getString("constituency_name"));
                    String ward = capitalizeString(data.getString("ward_name"));
                    String aadhaar = data.getString("aadhaar_no");
                    String serial = data.getString("serial_no");
                    String voter = data.getString("voter_id_no");

                    String address = capitalizeString(data.getString("address"));
                    String pincode = data.getString("pin_code");
                    String fatherHusbandName = capitalizeString(data.getString("father_husband_name"));
                    String mobileNo = data.getString("mobile_no");
                    String whatsappNo = data.getString("whatsapp_no");
                    String emailId = data.getString("email_id");
//                String religionName = capitalizeString(data.getString("religion_name"));
                    String constituencyName = capitalizeString(data.getString("constituency_name"));
                    String paguthiName = capitalizeString(data.getString("paguthi_name"));
                    String wardName = capitalizeString(data.getString("ward_name"));
                    String boothName = capitalizeString(data.getString("booth_name"));
                    String boothAddress = capitalizeString(data.getString("booth_address"));
                    String serialNo = data.getString("serial_no");
                    String aadhaarNo = data.getString("aadhaar_no");
                    String voterIdNo = data.getString("voter_id_no");
                    String dob = getserverdateformat(data.getString("dob"));
                    String gender = capitalizeString(data.getString("gender"));
                    String profilePicture = PreferenceStorage.getClientUrl(this) + GMSConstants.KEY_PIC_URL + data.getString("profile_pic");
                    PreferenceStorage.saveConstituentName(this, name);
                    PreferenceStorage.saveAddress(this, address);
                    PreferenceStorage.savePincode(this, pincode);
                    PreferenceStorage.savefatherORhusband(this, fatherHusbandName);
                    PreferenceStorage.saveMobileNo(this, mobileNo);
                    PreferenceStorage.saveWhatsappNo(this, whatsappNo);
                    PreferenceStorage.saveEmail(this, emailId);
                    PreferenceStorage.saveReligionName(this, "religionName");
                    PreferenceStorage.saveConstituencyName(this, constituencyName);
                    PreferenceStorage.savePaguthiName(this, paguthiName);
                    PreferenceStorage.saveWard(this, wardName);
                    PreferenceStorage.saveBooth(this, boothName);
                    PreferenceStorage.saveBoothAddress(this, boothAddress);
                    PreferenceStorage.saveSerialNo(this, serialNo);
                    PreferenceStorage.saveAadhaarNo(this, aadhaarNo);
                    PreferenceStorage.saveVoterId(this, voterIdNo);
                    PreferenceStorage.saveDob(this, dob);
                    PreferenceStorage.saveGender(this, gender);
                    PreferenceStorage.saveCOnstituentProfilePic(this, profilePicture);

                    PreferenceStorage.saveConstituencyName(this, constituency);
                    Intent intent = new Intent(this, ConstituentGrievanceProfileActivity.class);
                    startActivity(intent);
                } else {
                    JSONArray arraydata = response.getJSONArray("grievance_details");
                    JSONObject data = arraydata.getJSONObject(0);
                    seekerType.setText(data.getString("seeker_info"));

                    if (data.getString("grievance_type").equalsIgnoreCase("P")) {
                        txtPetitionEnquiry.setText(getString(R.string.petition_num));
                    } else {
                        txtPetitionEnquiry.setText(getString(R.string.enquiry_num));
                        findViewById(R.id.desc_layout).setVisibility(View.GONE);
                    }
                    txtConstituency.setText(capitalizeString(data.getString("paguthi_name")));
                    petitionEnquiryNo.setText(data.getString("petition_enquiry_no"));
                    grievanceName.setText(capitalizeString(data.getString("grievance_name")));
                    grievanceSubCat.setText(capitalizeString(data.getString("sub_category_name")));
                    grievanceDesc.setText(capitalizeString(data.getString("description")));
                    grievanceStatus.setText(capitalizeString(data.getString("status")));
                    createdOn.setText(getserverdateformat(data.getString("created_at")));
                    updatedOn.setText(getserverdateformat(data.getString("updated_at")));
                    constituent = data.getString("constituent_id");
                    grievanceReference.setText(data.getString("reference_note"));
                    if (data.getString("status").equalsIgnoreCase("COMPLETED")) {
                        grievanceStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_round_completed));
                    } else {
                        grievanceStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_round_processing));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.totalLayout.setForeground(ContextCompat.getDrawable(context, R.drawable.shadow_foreground));
//            }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
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

    @Override
    public void onError(String error) {

    }
}
