package com.gms.admin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.gms.admin.R;
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

import java.util.ArrayList;

public class StaffDetailsActivity extends AppCompatActivity implements IServiceListener, DialogClickListener {

    private static final String TAG = IndividualMeetingDetailActivity.class.getName();

    private String meetingID;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private LinearLayout selectStatus;
    private Button update;
    private ArrayList<String> spinnerData = new ArrayList<>();
    private ArrayAdapter<String> spinnerDataArrayAdapter = null;
    String chkRes = "";
    public TextView txtstaffName, txtstaffMail, txtPaguthi, txtStatus;
    private ImageView userImage, staffStatus;
    private EditText txtRole, txtGEnder, txtnumber, txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_details);

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

        meetingID = getIntent().getStringExtra("meetingObj");


        txtstaffName = (TextView) findViewById(R.id.staff_name);
        txtPaguthi = (TextView) findViewById(R.id.staff_paguthi);
        txtstaffMail = (TextView) findViewById(R.id.staff_mail);
        txtStatus = (TextView) findViewById(R.id.staff_status);
        userImage = (ImageView) findViewById(R.id.staff_image);
        staffStatus = (ImageView) findViewById(R.id.staff_status_img);
        txtRole = (EditText) findViewById(R.id.txt_role);
        txtGEnder = (EditText) findViewById(R.id.txt_gender);
        txtnumber = (EditText) findViewById(R.id.txt_number);
        txtAddress = (EditText) findViewById(R.id.txt_address);


        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        getMeetingDetail();
    }

    private void getMeetingDetail() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(GMSConstants.KEY_STAFF_ID, meetingID);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_STAFF_DETAILS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
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
                JSONArray data = response.getJSONArray("staff_details");
                txtstaffName.setText(capitalizeString(data.getJSONObject(0).getString("full_name")));
                txtPaguthi.setText(capitalizeString(data.getJSONObject(0).getString("paguthi_name")));
                txtstaffMail.setText(data.getJSONObject(0).getString("email_id"));
                txtStatus.setText(capitalizeString(data.getJSONObject(0).getString("status")));
                txtRole.setText(capitalizeString(data.getJSONObject(0).getString("role_name")));
                txtGEnder.setText(capitalizeString(data.getJSONObject(0).getString("gender")));
                txtnumber.setText(capitalizeString(data.getJSONObject(0).getString("phone_number")));
                txtAddress.setText(capitalizeString(data.getJSONObject(0).getString("address")));

                if (txtStatus.getText().toString().equalsIgnoreCase("Inactive")) {
                    staffStatus.setBackground(ContextCompat.getDrawable(txtStatus.getContext(), R.drawable.inactive_dot));
                    txtStatus.setTextColor(ContextCompat.getColor(txtStatus.getContext(), R.color.inactive));
                } else {
                    staffStatus.setBackground(ContextCompat.getDrawable(txtStatus.getContext(), R.drawable.active_dot));
                    txtStatus.setTextColor(ContextCompat.getColor(txtStatus.getContext(), R.color.active));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                totalLayout.setForeground(ContextCompat.getDrawable(context, R.drawable.shadow_foreground));
//            }
                }

                String urrl = PreferenceStorage.getClientUrl(userImage.getContext()) + GMSConstants.KEY_STAFFPIC_URL + data.getJSONObject(0).getString("profile_pic");

                if (GMSValidator.checkNullString(data.getJSONObject(0).getString("profile_pic"))) {
                    Picasso.get().load(urrl).into(userImage);
                } else {
                    userImage.setImageResource(R.drawable.ic_profile);
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
    public void onError(String error) {

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
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}