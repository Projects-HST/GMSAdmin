package com.gms.admin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gms.admin.R;
import com.gms.admin.customview.OtpEditText;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.CommonUtils;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Narendar on 12/06/2020.
 */

public class NumberVerificationActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = NumberVerificationActivity.class.getName();

    private OtpEditText otpEditText;
    private TextView tvResendOTP, tvCountDown;
    private TextView btnConfirm;
    private Button btnChangeNumber;
    private String mobileNo;
    private String checkVerify;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        mobileNo = PreferenceStorage.getMobileNo(getApplicationContext());
        otpEditText = (OtpEditText) findViewById(R.id.otp_view);
        tvResendOTP = (TextView) findViewById(R.id.resend);
        tvResendOTP.setOnClickListener(this);
        tvCountDown = findViewById(R.id.contentresend);
        btnConfirm = (TextView) findViewById(R.id.sendcode);
        btnConfirm.setOnClickListener(this);
//        btnChangeNumber = (Button) findViewById(R.id.changenumber);
//        btnChangeNumber.setOnClickListener(this);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        countDownTimers();

    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
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

    @Override
    public void onClick(View v) {

        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {

            if (v == tvResendOTP) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Do you want to resend OTP ?");
                alertDialogBuilder.setMessage("Confirm your mobile number : " + PreferenceStorage.getMobileNo(getApplicationContext()));
                alertDialogBuilder.setPositiveButton("Proceed",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                checkVerify = "Resend";
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put(GMSConstants.KEY_MOBILE_NUMBER, mobileNo);
                                    jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getApplicationContext()));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                                String url = GMSConstants.BUILD_URL + GMSConstants.MOBILE_LOGIN;
                                serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
                            }
                        });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

//                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialogBuilder.show();

            } else if (v == btnConfirm) {

                if (otpEditText.hasValidOTP()) {
                    checkVerify = "Confirm";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(GMSConstants.KEY_MOBILE_NUMBER, mobileNo);
                        jsonObject.put(GMSConstants.KEY_OTP, otpEditText.getOTP());
                        jsonObject.put(GMSConstants.KEY_DEVICE_ID, PreferenceStorage.getGCM(getApplicationContext()));
                        jsonObject.put(GMSConstants.MOBILE_TYPE, "2");
                        jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getApplicationContext()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    String url = PreferenceStorage.getClientUrl(this) + GMSConstants.MOBILE_VERIFY;
                    serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
                } else {
                    AlertDialogHelper.showSimpleAlertDialog(this, "Invalid OTP");
                }

            } else if (v == btnChangeNumber) {

//                Intent homeIntent = new Intent(getApplicationContext(), ChangeNumberActivity.class);
//                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                homeIntent.putExtra("mobile_no", mobileNo);
//                startActivity(homeIntent);
//                finish();
            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, "No Network connection available");
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
                if (checkVerify.equalsIgnoreCase("Resend")) {

                    Toast.makeText(getApplicationContext(), "OTP resent successfully", Toast.LENGTH_SHORT).show();

                } else if (checkVerify.equalsIgnoreCase("Confirm")) {
                    JSONObject data = response.getJSONObject("userData");

                    String userID = data.getString("user_id");
                    String userRole = data.getString("user_role");
                    String constituencyId = data.getString("constituency_id");
                    String pugathiId = data.getString("pugathi_id");
                    String fullName = data.getString("full_name");
                    String phoneNumber = data.getString("phone_number");
                    String emailId = data.getString("email_id");
                    String gender = data.getString("gender");
                    String address = data.getString("address");
                    String pictureUrl = data.getString("picture_url");
                    String status = data.getString("status");
                    String lastLogin = data.getString("last_login");
                    String loginCount = data.getString("login_count");
                    String appColor = data.getString("base_colour");

                    PreferenceStorage.saveUserId(this, userID);
                    PreferenceStorage.saveUserRole(this, userRole);
                    PreferenceStorage.saveConstituencyID(this, constituencyId);
                    PreferenceStorage.savePaguthiID(this, pugathiId);
                    PreferenceStorage.saveAdminName(this, fullName);
                    PreferenceStorage.saveAdminMobileNo(this, phoneNumber);
                    PreferenceStorage.saveAdminEmail(this, emailId);
                    PreferenceStorage.saveAdminGender(this, gender);
                    PreferenceStorage.saveAdminAddress(this, address);
                    PreferenceStorage.saveProfilePic(this, pictureUrl);
                    PreferenceStorage.saveStatus(this, status);
                    PreferenceStorage.saveLogin(this, lastLogin);
                    PreferenceStorage.saveLoginCount(this, loginCount);
                    PreferenceStorage.saveAppBaseColor(this, appColor);

                    Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                    homeIntent.putExtra("page", "verify");
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    homeIntent.putExtra("profile_state", "new");
                    startActivity(homeIntent);
                    this.finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    void countDownTimers() {
        new CountDownTimer(30 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvResendOTP.setVisibility(View.GONE);
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tvCountDown.setText("Resend in " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds) + " seconds");
            }

            public void onFinish() {
                tvCountDown.setText("Try again...");
                tvCountDown.setVisibility(View.GONE);
                tvResendOTP.setVisibility(View.VISIBLE);
            }
        }.start();
    }


}
