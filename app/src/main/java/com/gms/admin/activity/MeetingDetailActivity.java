package com.gms.admin.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

public class MeetingDetailActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = IndividualMeetingDetailActivity.class.getName();

    private String meetingID;
    private ImageView meetingImage;
    private TextView meetingTitle, meetingDetail, meetingDate, meetingStatus, meetinConstName, meetingArea;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private LinearLayout selectStatus;
    private Button update;
    private ArrayList<String> spinnerData = new ArrayList<>();
    private ArrayAdapter<String> spinnerDataArrayAdapter = null;
    String chkRes = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        meetingID = getIntent().getStringExtra("meetingObj");

        meetingImage = findViewById(R.id.img_drop);
        meetingTitle = (TextView) findViewById(R.id.meeting_title);
        meetingDetail = (TextView) findViewById(R.id.meeting_details);
        meetingDate = (TextView) findViewById(R.id.meeting_date);
        meetingStatus = (TextView) findViewById(R.id.meeting_status);
        meetinConstName = (TextView) findViewById(R.id.constituent_name);
        meetingArea = (TextView) findViewById(R.id.meeting_area);
        selectStatus = (LinearLayout) findViewById(R.id.status_select);
        selectStatus.setOnClickListener(this);
        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(this);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        spinnerData.add("REQUESTED");
        spinnerData.add("COMPLETED");

        spinnerDataArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_data_layout, R.id.data_name, spinnerData) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.d(TAG, "getview called" + position);
                View view = getLayoutInflater().inflate(R.layout.spinner_data_layout, parent, false);
                TextView gendername = (TextView) view.findViewById(R.id.data_name);
                gendername.setText(spinnerData.get(position));

                // ... Fill in other views ...
                return view;
            }
        };

        getMeetingDetail();
    }

    private void rotate(float from, float to) {
        final RotateAnimation rotateAnim = new RotateAnimation(from, to,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(100);
        rotateAnim.setFillAfter(true);
        meetingImage.startAnimation(rotateAnim);
    }

    private void getMeetingDetail() {
        chkRes = "detail";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(GMSConstants.KEY_MEETING_ID, meetingID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_MEETINGS_DETAILS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void updateMeeting() {
        chkRes = "update";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(GMSConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(GMSConstants.KEY_STATUS, meetingStatus.getText());
            jsonObject.put(GMSConstants.KEY_MEETING_ID, meetingID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.UPDATE_MEETING;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {
        if (v == selectStatus) {
            rotate(0.0f, 90.0f);
            showSpinnerData();
        }
        if (v == update) {
            updateMeeting();
        }
    }

    private void showSpinnerData() {
        Log.d(TAG, "Show Spinner Data");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.spinner_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.spinner_header);
        header.setText("Select Status");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(spinnerDataArrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = spinnerData.get(which);
                        meetingStatus.setText(strName);
                        rotate(90.0f, 0.0f);
                    }
                });
        builderSingle.show();
        builderSingle.setOnDismissListener(new AlertDialog.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                rotate(90.0f, 0.0f);
            }
        });
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
                if (chkRes.equalsIgnoreCase("update")) {
                    Toast.makeText(this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray data = response.getJSONArray("meeting_details");
                    meetinConstName.setText(data.getJSONObject(0).getString("full_name"));
                    meetingDate.setText(data.getJSONObject(0).getString("meeting_date"));
                    meetingArea.setText(data.getJSONObject(0).getString("paguthi_name"));
                    meetingTitle.setText(data.getJSONObject(0).getString("meeting_title"));
                    meetingDetail.setText(data.getJSONObject(0).getString("meeting_detail"));
                    meetingStatus.setText(data.getJSONObject(0).getString("meeting_status"));
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