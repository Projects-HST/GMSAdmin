package com.gms.admin.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;

public class IndividualInteractionActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = IndividualGrievanceActivity.class.getName();
    private View rootView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private LinearLayout listView;
    private int ab = 0;
    private ArrayList<Grievance> grievances = new ArrayList<>();
    private GrievanceListAdapter grievanceListAdapter;
    private TextView grievanceCount, noGrievance;
    private User user;
    private IndividualGrievanceList individualGrievanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_interaction);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                textQuestion.setText(memberCount.getJSONObject(c1).getString("interaction_text"));
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
}