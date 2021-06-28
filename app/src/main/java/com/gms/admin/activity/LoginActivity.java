package com.gms.admin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.gms.admin.R;
import com.gms.admin.bean.support.ConstituencyList;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.GMSValidator;
import com.gms.admin.utils.PreferenceStorage;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = LoginActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private TextInputLayout tiNumber, tiPassword, tiMail;
    private TextInputEditText edtPhone, edtMail, edtPassword;
    private TextView signIn, useMail, usePhone;
    private TextView forgot;
    private ImageView laang;
    String IMEINo = "";
    private RelativeLayout selectConstituency, layoutNumber, layoutMail, changeLayoutNumber, changeLayoutMail;
    private String whatRes = "" , loginMethod = "number";
    private ConstituencyList constituencyList;
    private LinearLayout layoutSpinner;
    private String feebackAns = "";
    private int pooos, constituencyCount;
    private TextView constituencyCancel, constituencyOK, contistuencyText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        layoutNumber = (RelativeLayout) findViewById(R.id.layout_number);
        layoutMail = (RelativeLayout) findViewById(R.id.layout_email);
        changeLayoutNumber = (RelativeLayout) findViewById(R.id.change_ph_layout);
        changeLayoutMail = (RelativeLayout) findViewById(R.id.change_layout);

        tiNumber = (TextInputLayout) findViewById(R.id.ti_phone);
        tiMail = (TextInputLayout) findViewById(R.id.ti_email);
        tiPassword = (TextInputLayout) findViewById(R.id.ti_password);

        edtPhone = (TextInputEditText) findViewById(R.id.phone);
        edtMail = (TextInputEditText) findViewById(R.id.email);
        edtPassword = (TextInputEditText) findViewById(R.id.password);

        forgot = (TextView) findViewById(R.id.forgot);
        forgot.setOnClickListener(this);
        signIn = findViewById(R.id.btn_login_go);
        signIn.setOnClickListener(this);
        useMail = findViewById(R.id.btn_email_go);
        useMail.setOnClickListener(this);
        usePhone = findViewById(R.id.btn_ph_go);
        usePhone.setOnClickListener(this);

        contistuencyText = (TextView) findViewById(R.id.text_constituency);
        selectConstituency = (RelativeLayout) findViewById(R.id.select_constituency);
        selectConstituency.setOnClickListener(this);

        constituencyCancel = (TextView) findViewById(R.id.selection_cancel);
        constituencyOK = (TextView) findViewById(R.id.selection_done);
        constituencyCancel.setOnClickListener(this);
        constituencyOK.setOnClickListener(this);

        layoutSpinner = (LinearLayout) findViewById(R.id.list_view);
        layoutSpinner.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
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

    private void getConstituencyList() {
        whatRes = "constituency";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_PARTY_ID, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = GMSConstants.BUILD_URL + GMSConstants.CONSTITUENCY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {
        if (v == selectConstituency) {
            getConstituencyList();
        } else if (v == constituencyCancel) {
            selectConstituency.setClickable(true);
            edtMail.setClickable(true);
            edtPassword.setClickable(true);
            forgot.setClickable(true);
            signIn.setClickable(true);
            layoutSpinner.removeAllViews();
            findViewById(R.id.spinner_layout).setVisibility(View.GONE);
        } else if (v == constituencyOK) {
            constituencyOK.setBackground(ContextCompat.getDrawable(this, R.drawable.default_logo));
            sendSelectedConstituency();
        } else if (v == signIn) {
            if (loginMethod.equalsIgnoreCase("number")) {
                if (validateFields()) {
                    PreferenceStorage.saveMobileNo(this, edtPhone.getText().toString());
                    sendMobileLogin();
                }
            }else {
                if (validateEmailFields()){
                    sendEmailLogin();
                }
            }
        } else if (v == forgot) {
            if (contistuencyText.getText().toString().equalsIgnoreCase(getString(R.string.select_constituency))) {
                AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.select_constituency_alert));
            } else {
                Intent intent = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        }
        else if (v == useMail){
            loginMethod = "email";
            layoutNumber.setVisibility(View.GONE);
            layoutMail.setVisibility(View.VISIBLE);
            changeLayoutMail.setVisibility(View.GONE);
            changeLayoutNumber.setVisibility(View.VISIBLE);
        }
        else if (v == usePhone){
            loginMethod = "number";
            layoutNumber.setVisibility(View.VISIBLE);
            layoutMail.setVisibility(View.GONE);
            changeLayoutMail.setVisibility(View.VISIBLE);
            changeLayoutNumber.setVisibility(View.GONE);
        }
    }

    private boolean validateFields(){
        if (!GMSValidator.checkNullString(this.edtPhone.getText().toString().trim())) {
            tiNumber.setError(getString(R.string.error_number));
            requestFocus(edtPhone);
            return false;
        }
        if (!GMSValidator.checkMobileNumLength(this.edtPhone.getText().toString().trim())) {
            tiNumber.setError(getString(R.string.error_number_min));
            requestFocus(edtPhone);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmailFields() {
        if (!GMSValidator.isEmailValid(this.edtMail.getText().toString().trim())) {
            tiMail.setError(getString(R.string.error_email));
            requestFocus(edtMail);
            return false;
        }
        if (!GMSValidator.checkNullString(this.edtMail.getText().toString().trim())) {
            tiMail.setError(getString(R.string.error_email));
            requestFocus(edtMail);
            return false;
        }
        if (!GMSValidator.checkNullString(this.edtPhone.getText().toString().trim())) {
            tiNumber.setError(getString(R.string.error_number));
            requestFocus(edtPhone);
            return false;
        }
        if (!GMSValidator.checkMobileNumLength(this.edtPhone.getText().toString().trim())) {
            tiNumber.setError(getString(R.string.error_number_min));
            requestFocus(edtPhone);
            return false;
        }
        if (!GMSValidator.checkNullString(this.edtPassword.getText().toString().trim())) {
            tiPassword.setError(getString(R.string.error_password));
            requestFocus(edtPassword);
            return false;
        }
        if (!GMSValidator.checkStringMinLength(6, this.edtPassword.getText().toString().trim())) {
            tiPassword.setError(getString(R.string.error_password_min));
            requestFocus(edtPassword);
            return false;
        }
        if (contistuencyText.getText().toString().equalsIgnoreCase(getString(R.string.select_constituency))) {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.select_constituency_alert));
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void sendMobileLogin(){
        whatRes = "number";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_MOBILE_NUMBER, edtPhone.getText().toString());
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.MOBILE_LOGIN;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void sendEmailLogin() {
        whatRes = "email";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_USER_NAME, edtMail.getText().toString());
            jsonObject.put(GMSConstants.KEY_PASSWORD, edtPassword.getText().toString());
            jsonObject.put(GMSConstants.DEVICE_TOKEN, PreferenceStorage.getGCM(this));
            jsonObject.put(GMSConstants.MOBILE_TYPE, GMSConstants.MOBILE_TYPE_VALUE);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.USER_LOGIN;
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
                    if (status.equalsIgnoreCase("Success")) {
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
                if (whatRes.equalsIgnoreCase("constituency")) {
                    Gson gson = new Gson();
                    constituencyList = gson.fromJson(response.toString(), ConstituencyList.class);
                    if (constituencyList.getConstituencyArrayList() != null && constituencyList.getConstituencyArrayList().size() > 0) {
                        int totalCount = constituencyList.getCount();
//                    this.serviceHistoryArrayList.addAll(ongoingServiceList.getserviceArrayList());
                        boolean isLoadingForFirstTime = false;
//                        updateListAdapter(serviceHistoryList.getFeedbackArrayList());
                        constituencyCount = constituencyList.getConstituencyArrayList().size();
                        loadMembersList(constituencyCount);
                        findViewById(R.id.spinner_layout).setVisibility(View.VISIBLE);
                        selectConstituency.setClickable(false);
                        edtPhone.setClickable(false);
                        edtMail.setClickable(false);
                        edtPassword.setClickable(false);
                        forgot.setClickable(false);
                        signIn.setClickable(false);
                    }
                }else if (whatRes.equalsIgnoreCase("number")){
                    Intent i = new Intent(this, NumberVerificationActivity.class);
                    startActivity(i);
                }
                else if (whatRes.equalsIgnoreCase("email")) {
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

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("page", "login");
                    startActivity(intent);
                    finish();
                } else if (whatRes.equalsIgnoreCase("ANS")) {
                    String constituencyID = response.getJSONArray("data").getJSONObject(0).getString("constituency_id");
                    String constituencyName = response.getJSONArray("data").getJSONObject(0).getString("constituency_name");
                    String clientURL = response.getJSONArray("data").getJSONObject(0).getString("client_api_url");

//                    PreferenceStorage.saveConstituencyID(this, constituencyID);
//                    PreferenceStorage.saveConstituencyName(this, constituencyName);
                    PreferenceStorage.saveClientUrl(this, clientURL);

                    selectConstituency.setClickable(true);
                    edtMail.setClickable(true);
                    edtPassword.setClickable(true);
                    forgot.setClickable(true);
                    signIn.setClickable(true);
                    contistuencyText.setText(constituencyName);
                    layoutSpinner.removeAllViews();
                    findViewById(R.id.spinner_layout).setVisibility(View.GONE);
                    selectConstituency.setClickable(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }

    private void loadMembersList(int memberCount) {

        try {

            for (int c1 = 0; c1 < memberCount; c1++) {
                RadioButton constiRadio = new RadioButton(this);
                RadioGroup.LayoutParams radioParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                radioParams.setMargins(20, 10, 10, 10);
                constiRadio.setLayoutParams(radioParams);

                String consti = constituencyList.getConstituencyArrayList().get(c1).getconstituency_name();
                constiRadio.setText(consti);
                constiRadio.setId(R.id.radio_constituency);
                constiRadio.setTextSize(16.0f);
                constiRadio.setElevation(20.0f);
                constiRadio.setPadding(10, 0, 0, 0);
                constiRadio.setTextColor(ContextCompat.getColor(this, R.color.radio_grey));
                constiRadio.setButtonTintList(ContextCompat.getColorStateList(this, R.color.black));

                final int finalC = c1;

                constiRadio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v == constiRadio) {
                            pooos = finalC;
                            onRadioButtonClicked();
                        }
                    }
                });
                if (c1 == 0) {
                    constiRadio.setChecked(true);
                    constiRadio.setButtonTintList(ContextCompat.getColorStateList(this, R.color.black));
                } else {
                    constiRadio.setChecked(false);
                    constiRadio.setButtonTintList(ContextCompat.getColorStateList(this, R.color.radio_grey));
                }
                layoutSpinner.addView(constiRadio);
            }

            ScrollView.LayoutParams params = null;
            if (memberCount < 5) {
                int height = memberCount * 100;
                params = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);

            } else {
                params = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
            }
            layoutSpinner.setLayoutParams(params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onRadioButtonClicked() {
        for (int position = 0; position < constituencyCount; position++) {
            RadioButton rad = layoutSpinner.getChildAt(position).findViewById(R.id.radio_constituency);
            if (position == pooos) {
                rad.setChecked(true);
                rad.setButtonTintList(ContextCompat.getColorStateList(this, R.color.black));

            } else {
                rad.setChecked(false);
                rad.setButtonTintList(ContextCompat.getColorStateList(this, R.color.radio_grey));
            }

        }
    }

    private void sendSelectedConstituency() {
        whatRes = "ANS";
        int idConsti = 0;
        for (int position = 0; position < constituencyCount; position++) {
            RadioButton rad = layoutSpinner.getChildAt(position).findViewById(R.id.radio_constituency);
            if (rad.isChecked()) {
                idConsti = position;
                PreferenceStorage.saveUserConstituencyName(this, rad.getText().toString());
            }

        }

        JSONObject jsonObject = new JSONObject();
        String id = "";
        id = constituencyList.getConstituencyArrayList().get(idConsti).getid();

        try {
            jsonObject.put(GMSConstants.KEY_JUST_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = GMSConstants.BUILD_URL + GMSConstants.SELECTED_CONSTITUENCY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

}
