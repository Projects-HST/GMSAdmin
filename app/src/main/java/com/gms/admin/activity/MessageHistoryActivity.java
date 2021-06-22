package com.gms.admin.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.gms.admin.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageHistoryActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = IndividualGrievanceActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private LinearLayout listView;
    private String grievance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_history);

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

        listView = (LinearLayout) findViewById(R.id.list_view_msg_history);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        getInteraction();

    }

    private void getInteraction() {
        JSONObject jsonObject = new JSONObject();
        String id = "";
        id = grievance;
        try {
            jsonObject.put(GMSConstants.KEY_GRIEVANCE_ID, id);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_GRIEVANCE_MESSAGE;
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
                loadMembersList(response.getJSONArray("message_details"));
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
                cellParams.setMargins(20,10,20,10);

                LinearLayout.LayoutParams msgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                msgParams.setMargins(20,20,20,20);

                LinearLayout.LayoutParams sentDateParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                sentDateParams.setMargins(0,0,20,20);

                LinearLayout.LayoutParams sentByParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                sentByParams.setMargins(20,20,20,20);

                TextView txtMessage = new TextView(this);
                txtMessage.setTextColor(ContextCompat.getColor(this, R.color.black));
                txtMessage.setLayoutParams(msgParams);
                txtMessage.setText(memberCount.getJSONObject(c1).getString("sms_text"));
                txtMessage.setTextSize(14.0f);
                txtMessage.setTypeface(null, Typeface.BOLD);

                TextView sentAt = new TextView(this);
                sentAt.setTextColor(ContextCompat.getColor(this, R.color.msg_by_grey));
                sentAt.setLayoutParams(sentDateParams);
                sentAt.setGravity(Gravity.END);
                sentAt.setTextSize(12.0f);

                String date = memberCount.getJSONObject(c1).getString("created_at");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date testDate = null;
                try {
                    testDate = formatter.parse(date);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd MMM yyyy");

                sentAt.setText(sdf.format(testDate));

                TextView sentBy = new TextView(this);
                sentBy.setTextColor(ContextCompat.getColor(this, R.color.msg_by_grey));
                sentBy.setLayoutParams(sentByParams);
                sentBy.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sent_by, 0, 0, 0);
                sentBy.setCompoundDrawablePadding(20);
                sentBy.setText("Sent by " + memberCount.getJSONObject(c1).getString("created_by"));
                sentBy.setTextSize(14.0f);

                LinearLayout cell = new LinearLayout(this);
                cell.setOrientation(LinearLayout.VERTICAL);
                cell.setLayoutParams(cellParams);
                cell.setElevation(10.0f);
                cell.setBackground(ContextCompat.getDrawable(this, R.drawable.shadow_round));

                cell.addView(txtMessage);
                cell.addView(sentBy);

                LinearLayout mainLayout = new LinearLayout(this);
                mainLayout.setOrientation(LinearLayout.VERTICAL);
                mainLayout.setLayoutParams(mainLayoutParams);
                mainLayout.addView(cell);
                mainLayout.addView(sentAt);

                listView.addView(mainLayout);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}