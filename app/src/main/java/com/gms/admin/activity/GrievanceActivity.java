package com.gms.admin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gms.admin.R;
import com.gms.admin.fragment.DashboardFragment;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.PreferenceStorage;

import org.json.JSONException;
import org.json.JSONObject;

public class GrievanceActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener {

    public static final String TAG = GrievanceActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper dialogHelper;
    private String paguthiId = "";
    private TextView grieveCnt, enqCnt, petitionCnt, perComp, compCnt, perPend, pendCnt, perRej, rejCnt, petition,
            perOnline1, online_1, perCivic1, civic_1, onPetition, perOnComp, onCompCnt, perOnPen, onPendCnt, perOnRej, onRejCnt, civPetition,
            perCivComp, civComCnt, perCivPend, civPendCnt, perCivRej, civRejCnt, enquiry, perOnline2, online_2, perCivic2, civic_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance);

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

        grieveCnt = findViewById(R.id.overall_count_1);
        enqCnt = findViewById(R.id.enquiry_count);
        petitionCnt = findViewById(R.id.petition_count);
        perComp = findViewById(R.id.per_complete_1);
        compCnt = findViewById(R.id.complete_count_1);
        perPend = findViewById(R.id.per_pending_1);
        pendCnt = findViewById(R.id.pending_count_1);
        perRej = findViewById(R.id.per_reject_1);
        rejCnt = findViewById(R.id.reject_count_1);
        petition = findViewById(R.id.overall_count_2);
        perOnline1 = findViewById(R.id.per_online_1);
        online_1 = findViewById(R.id.online_count_1);
        perCivic1 = findViewById(R.id.per_civic_1);
        civic_1 = findViewById(R.id.civic_count_1);
        onPetition = findViewById(R.id.on_petition_count);
        perOnComp = findViewById(R.id.per_complete_2);
        onCompCnt = findViewById(R.id.complete_count_2);
        perOnPen = findViewById(R.id.per_pending_2);
        onPendCnt = findViewById(R.id.pending_count_2);
        perOnRej = findViewById(R.id.per_reject_2);
        onRejCnt = findViewById(R.id.reject_count_2);
        civPetition = findViewById(R.id.civic_petition_count);
        perCivComp = findViewById(R.id.per_complete_3);
        civComCnt = findViewById(R.id.complete_count_3);
        perCivPend = findViewById(R.id.per_pending_3);
        civPendCnt = findViewById(R.id.pending_count_3);
        perCivRej = findViewById(R.id.per_reject_3);
        civRejCnt = findViewById(R.id.reject_count_3);
        enquiry = findViewById(R.id.overall_count_3);
        perOnline2 = findViewById(R.id.per_online_2);
        online_2 = findViewById(R.id.online_count_2);
        perCivic2 = findViewById(R.id.per_civic_2);
        civic_2 = findViewById(R.id.civic_count_2);

        paguthiId = getIntent().getStringExtra("paguthi");

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        dialogHelper = new ProgressDialogHelper(this);

        getGrievanceData();
    }

    private void getGrievanceData(){

        String date_1 = PreferenceStorage.getFromDate(this);
        String date_2 = PreferenceStorage.getToDate(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI_ID, paguthiId);
            jsonObject.put(GMSConstants.KEY_FROM_DATE, date_1);
            jsonObject.put(GMSConstants.KEY_TO_DATE, date_2);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_WIDGET_GRIEVANCE;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
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
        dialogHelper.hideProgressDialog();
        if (validateResponse(response)){
            try{
                if (response.getString("status").equalsIgnoreCase("Success")) {
                    grieveCnt.setText(response.getString("tot_grive_count"));
                    enqCnt.setText(response.getString("enquiry_count"));
                    petitionCnt.setText(response.getString("petition_count"));
                    perComp.setText("( " + response.getJSONObject("petition_status").getString("petition_completed_percentage") + "%" + " )");
                    compCnt.setText(response.getJSONObject("petition_status").getString("petition_completed"));
                    perPend.setText("( " + response.getJSONObject("petition_status").getString("petition_pending_percentage") + "%" + " )");
                    pendCnt.setText(response.getJSONObject("petition_status").getString("petition_pending"));
                    perRej.setText("( " + response.getJSONObject("petition_status").getString("petition_rejected_percentage") + "%" + " )");
                    rejCnt.setText(response.getJSONObject("petition_status").getString("petition_rejected"));
                    petition.setText(response.getString("petition_count"));
                    perOnline1.setText("( " + response.getJSONObject("petition_list").getString("no_of_online_percentage") + "%" + " )");
                    online_1.setText(response.getJSONObject("petition_list").getString("no_of_online"));
                    perCivic1.setText("( " + response.getJSONObject("petition_list").getString("no_of_civic_percentage") + "%" + " )");
                    civic_1.setText(response.getJSONObject("petition_list").getString("no_of_civic"));
                    onPetition.setText(response.getString("online_petition_count"));
                    perOnComp.setText("( " + response.getJSONObject("online_petition_status").getString("petition_completed_percentage") + "%" + " )");
                    onCompCnt.setText(response.getJSONObject("online_petition_status").getString("petition_completed"));
                    perOnPen.setText("( " + response.getJSONObject("online_petition_status").getString("petition_pending_percentage") + "%" + " )");
                    onPendCnt.setText(response.getJSONObject("online_petition_status").getString("petition_pending"));
                    perOnRej.setText("( " + response.getJSONObject("online_petition_status").getString("petition_rejected_percentage") + "%" + " )");
                    onRejCnt.setText(response.getJSONObject("online_petition_status").getString("petition_rejected"));
                    civPetition.setText(response.getString("civic_petition_count"));
                    perCivComp.setText("( " + response.getJSONObject("civic_petition_status").getString("petition_completed_percentage") + "%" + " )");
                    civComCnt.setText(response.getJSONObject("online_petition_status").getString("petition_completed"));
                    perCivPend.setText("( " + response.getJSONObject("civic_petition_status").getString("petition_pending_percentage") + "%" + " )");
                    civPendCnt.setText(response.getJSONObject("online_petition_status").getString("petition_pending"));
                    perCivRej.setText("( " + response.getJSONObject("civic_petition_status").getString("petition_rejected_percentage") + "%" + " )");
                    civRejCnt.setText(response.getJSONObject("online_petition_status").getString("petition_rejected"));
                    enquiry.setText(response.getString("enquiry_count"));
                    perOnline2.setText("( " + response.getJSONObject("enquiry_list").getString("no_of_online_percentage") + "%" + " )");
                    online_2.setText(response.getJSONObject("enquiry_list").getString("no_of_online"));
                    perCivic2.setText("( " + response.getJSONObject("enquiry_list").getString("no_of_civic_percentage") + "%" + " )");
                    civic_2.setText(response.getJSONObject("enquiry_list").getString("no_of_civic"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onClick(View v) {
    }
}