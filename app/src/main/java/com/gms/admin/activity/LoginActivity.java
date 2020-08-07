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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = LoginActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private TextInputEditText edtUserName, edtPassword;
    private Button signIn;
    private TextView forgot;
    private ImageView laang;
    String IMEINo = "";
    private RelativeLayout selectConstituency;
    private String whatRes = "";
    private ConstituencyList constituencyList;
    private LinearLayout layoutSpinner;
    private String feebackAns = "";
    private int pooos, constituencyCount;
    private TextView constituencyCancel, constituencyOK, contistuencyText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        edtUserName = (TextInputEditText) findViewById(R.id.email_or_phone);
        edtPassword = (TextInputEditText) findViewById(R.id.password);
        forgot = (TextView) findViewById(R.id.forgot);
        forgot.setOnClickListener(this);
        signIn = findViewById(R.id.btn_go);
        signIn.setOnClickListener(this);

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


    private void sendLogin() {
        whatRes = "login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_USER_NAME, edtUserName.getText().toString());
            jsonObject.put(GMSConstants.KEY_PASSWORD, edtPassword.getText().toString());
            jsonObject.put(GMSConstants.DEVICE_TOKEN, PreferenceStorage.getGCM(this));
            jsonObject.put(GMSConstants.MOBILE_TYPE, GMSConstants.MOBILE_TYPE_VALUE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.USER_LOGIN;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }


    @Override
    public void onClick(View v) {
        if (v == selectConstituency) {
            getConstituencyList();
        } else if (v == constituencyCancel) {
            selectConstituency.setClickable(true);
            edtUserName.setClickable(true);
            edtPassword.setClickable(true);
            forgot.setClickable(true);
            signIn.setClickable(true);
            layoutSpinner.removeAllViews();
            findViewById(R.id.spinner_layout).setVisibility(View.GONE);
        } else if (v == constituencyOK) {
            sendSelectedConstituency();
        } else if (v == signIn) {
            if (validateFields()) {
                sendLogin();
            }
        } else if (v == forgot) {
            if (contistuencyText.getText().toString().equalsIgnoreCase(getString(R.string.select_constituency))) {
                AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.select_constituency_alert));
            } else {
                Intent intent = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        }
    }

    private boolean validateFields() {
        if (!GMSValidator.checkNullString(this.edtUserName.getText().toString().trim())) {
            edtUserName.setError(getString(R.string.error_username));
            requestFocus(edtUserName);
            return false;
        }if (!GMSValidator.checkNullString(this.edtPassword.getText().toString().trim())) {
            edtPassword.setError(getString(R.string.error_password));
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
                        edtUserName.setClickable(false);
                        edtPassword.setClickable(false);
                        forgot.setClickable(false);
                        signIn.setClickable(false);
                    }
                } else if (whatRes.equalsIgnoreCase("login")) {
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
                    edtUserName.setClickable(true);
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
                constiRadio.setTextSize(18.0f);
                constiRadio.setElevation(20.0f);
                constiRadio.setPadding(10, 0, 0, 0);
                constiRadio.setTextColor(ContextCompat.getColor(this, R.color.black));
                constiRadio.setButtonTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));

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
                    constiRadio.setButtonTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
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
                rad.setButtonTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));

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
