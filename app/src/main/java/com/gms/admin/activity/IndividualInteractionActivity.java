package com.gms.admin.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.gms.admin.R;
import com.gms.admin.adapter.GrievanceListAdapter;
import com.gms.admin.bean.support.Grievance;
import com.gms.admin.bean.support.IndividualGrievanceList;
import com.gms.admin.bean.support.User;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IndividualInteractionActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = IndividualGrievanceActivity.class.getName();
    private View rootView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private LinearLayout listView;
    private int ab = 0;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_interaction);

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

        user = (User) getIntent().getSerializableExtra("userObj");

        listView = (LinearLayout) findViewById(R.id.list_view_interaction);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        getInteraction();

    }

    private void getInteraction() {
        JSONObject jsonObject = new JSONObject();
        String id = "";
        id = user.getid();
        try {
            jsonObject.put(GMSConstants.KEY_CONSTITUENT_ID, id);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_CONSTITUENT_INTERACTION;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {

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
                loadMembersList(response.getJSONArray("interaction_details"));
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

    private void loadMembersList(JSONArray memberCount) {

        try {

            for (int c1 = 0; c1 < memberCount.length(); c1++) {
                LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mainLayoutParams.setMargins(0,0,0,0);

                LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cellParams.setMargins(40,10,40,10);

                LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
                lineParams.setMargins(40,20,40,0);

                LinearLayout.LayoutParams textQuestionParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textQuestionParams.setMargins(10,10,10,10);

                LinearLayout.LayoutParams textAnswerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textAnswerParams.setMargins(40,20,40,10);

                LinearLayout.LayoutParams yesParams = new LinearLayout.LayoutParams(140, 60);
                yesParams.setMargins(0,0,40,0);

                LinearLayout.LayoutParams noParams = new LinearLayout.LayoutParams(140, 60);
                noParams.setMargins(40,0,0,0);

                TextView textQuestion = new TextView(this);
                textQuestion.setId(R.id.interaction_question);
                textQuestion.setTextColor(ContextCompat.getColor(this, R.color.black));
                textQuestion.setGravity(Gravity.CENTER_HORIZONTAL);
                textQuestion.setText(capitalizeString(memberCount.getJSONObject(c1).getString("interaction_text")));
                textQuestion.setLayoutParams(textQuestionParams);

                TextView yes = new TextView(this);
                TextView no = new TextView(this);
                yes.setGravity(Gravity.CENTER);
                no.setGravity(Gravity.CENTER);
                yes.setLayoutParams(yesParams);
                no.setLayoutParams(noParams);
                yes.setText(R.string.alert_button_yes);
                no.setText(R.string.alert_button_no);
                yes.setTextColor(ContextCompat.getColor(this, R.color.text_grey));
                yes.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_inactive));
                no.setTextColor(ContextCompat.getColor(this, R.color.text_grey));
                no.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_inactive));

                if (memberCount.getJSONObject(c1).getString("status").equalsIgnoreCase("No")) {
                    yes.setTextColor(ContextCompat.getColor(this, R.color.text_grey));
                    yes.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_inactive));
                    no.setTextColor(ContextCompat.getColor(this, R.color.white));
                    no.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_no));
                } else {
                    yes.setTextColor(ContextCompat.getColor(this, R.color.white));
                    yes.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_yes));
                    no.setTextColor(ContextCompat.getColor(this, R.color.text_grey));
                    no.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_inactive));
                }

                LinearLayout answerLayout = new LinearLayout(this);
                answerLayout.setOrientation(LinearLayout.HORIZONTAL);
                answerLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                answerLayout.setLayoutParams(textAnswerParams);

                answerLayout.addView(yes);
                answerLayout.addView(no);

                View view = new View(this);
                view.setBackgroundColor(ContextCompat.getColor(this, R.color.line_interaction));
                view.setLayoutParams(lineParams);

                LinearLayout cell = new LinearLayout(this);
                cell.setOrientation(LinearLayout.VERTICAL);
                cell.setLayoutParams(cellParams);
                cell.addView(textQuestion);
                cell.addView(answerLayout);
                cell.addView(view);

                LinearLayout mainLayout = new LinearLayout(this);
                mainLayout.setOrientation(LinearLayout.VERTICAL);
                mainLayout.setLayoutParams(mainLayoutParams);
                mainLayout.addView(cell);

                listView.addView(mainLayout);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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


}