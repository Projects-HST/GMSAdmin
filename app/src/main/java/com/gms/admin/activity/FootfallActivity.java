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

public class FootfallActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    public static final String TAG = FootfallActivity.class.getName();
    private String paguthiId = "";
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper dialogHelper;
    private TextView uniqueCount, perConst, constCount, perOther, otherCount, totalCount, perUnique, uniCnt, perRpt, rptCnt,
            parConstCnt, perUniPar, uniParCnt, perParRpt, parRptCnt, otherConstCnt, perUniOth, uniOthCnt, perRptOth, othRptCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footfall);

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

        uniqueCount = findViewById(R.id.unique_ff_count);
        perConst = findViewById(R.id.per_paguthi);
        constCount = findViewById(R.id.paguthi_count);
        perOther = findViewById(R.id.per_other);
        otherCount = findViewById(R.id.other_count);
        totalCount = findViewById(R.id.total_ff_count);
        perUnique = findViewById(R.id.per_unique_1);
        uniCnt = findViewById(R.id.unique_count_1);
        perRpt = findViewById(R.id.per_repeat_1);
        rptCnt = findViewById(R.id.repeat_count_1);
        parConstCnt = findViewById(R.id.singanallur_ff_count);
        perUniPar = findViewById(R.id.per_unique_2);
        uniParCnt = findViewById(R.id.unique_count_2);
        perParRpt = findViewById(R.id.per_repeat_2);
        parRptCnt = findViewById(R.id.repeat_count_2);
        otherConstCnt = findViewById(R.id.other_ff_count);
        perUniOth = findViewById(R.id.per_unique_3);
        uniOthCnt = findViewById(R.id.unique_count_3);
        perRptOth = findViewById(R.id.per_repeat_3);
        othRptCnt = findViewById(R.id.repeat_count_3);

        paguthiId = getIntent().getStringExtra("paguthi");

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        dialogHelper = new ProgressDialogHelper(this);

        getFootFallData();
    }

    @Override
    public void onClick(View v) {

    }

    private void getFootFallData(){

        String date_1 = PreferenceStorage.getFromDate(this);
        String date_2 = PreferenceStorage.getToDate(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI_ID, paguthiId);
            jsonObject.put(GMSConstants.KEY_FROM_DATE, date_1);
            jsonObject.put(GMSConstants.KEY_TO_DATE, date_2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_WIDGET_FOOTFALL;
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
                if (response.getString("status").equalsIgnoreCase("Success")){
                    uniqueCount.setText(response.getJSONObject("footfall_details").getString("total_unique_footfall_cnt"));
                    perConst.setText("( " + response.getJSONObject("footfall_details").getString("cons_unique_footfall_cnt_presntage") + "%" + " )");
                    constCount.setText(response.getJSONObject("footfall_details").getString("cons_unique_footfall_cnt"));
                    perOther.setText("( " + response.getJSONObject("footfall_details").getString("other_unique_footfall_cnt_presntage") + "%" + " )");
                    otherCount.setText(response.getJSONObject("footfall_details").getString("other_unique_footfall_cnt"));
                    totalCount.setText(response.getJSONObject("footfall_details").getString("total_footfall_cnt"));
                    perUnique.setText("( " + response.getJSONObject("footfall_details").getString("unique_footfall_cnt_presntage") + "%" + " )");
                    uniCnt.setText(response.getJSONObject("footfall_details").getString("unique_footfall_cnt"));
                    perRpt.setText("( " + response.getJSONObject("footfall_details").getString("repeated_footfall_cnt_presntage") + "%" + " )");
                    rptCnt.setText(response.getJSONObject("footfall_details").getString("repeated_footfall_cnt"));
                    parConstCnt.setText(response.getJSONObject("footfall_details").getString("constituency_cnt"));
                    perUniPar.setText("( " + response.getJSONObject("footfall_details").getString("cons_unique_cnt_presntage") + "%" + " )");
                    uniParCnt.setText(response.getJSONObject("footfall_details").getString("cons_unique_cnt"));
                    perParRpt.setText("( " + response.getJSONObject("footfall_details").getString("cons_repeated_cnt_presntage") + "%" + " )");
                    parRptCnt.setText(response.getJSONObject("footfall_details").getString("cons_repeated_cnt"));
                    otherConstCnt.setText(response.getJSONObject("footfall_details").getString("other_cnt"));
                    perUniOth.setText("( " + response.getJSONObject("footfall_details").getString("other_unique_cnt_presntage") + "%" + " )");
                    uniOthCnt.setText(response.getJSONObject("footfall_details").getString("other_unique_cnt"));
                    perRptOth.setText("( " + response.getJSONObject("footfall_details").getString("other_repeated_cnt_presntage") + "%" + " )");
                    othRptCnt.setText(response.getJSONObject("footfall_details").getString("other_repeated_cnt"));
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
}