package com.gms.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IndividualGrievanceActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = IndividualGrievanceActivity.class.getName();
    private View rootView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ListView listView;
    private int ab = 0;
    private ArrayList<Grievance> grievances = new ArrayList<>();
    private GrievanceListAdapter grievanceListAdapter;
    private TextView grievanceCount, noGrievance;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_grievance);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        user = (User) getIntent().getSerializableExtra("userObj");

        listView = (ListView) findViewById(R.id.list_petition);
        listView.setOnItemClickListener(this);

        noGrievance = (TextView) findViewById(R.id.no_grievance);
        grievanceCount = (TextView) findViewById(R.id.individual_grievance_count);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        getGrievances();

    }

    private void getGrievances() {
        JSONObject jsonObject = new JSONObject();
        String id = "";
        id = user.getid();
        try {
            jsonObject.put(GMSConstants.KEY_CONSTITUENT_ID, id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_CONSTITUENT_GRIEVANCE;
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
                        noGrievance.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
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
                int cou = Integer.parseInt(response.getString("grievance_count"));
                if (cou > 1) {
                    grievanceCount.setText(cou + " Grievances");
                } else {
                    grievanceCount.setText(cou + " Grievance");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            IndividualGrievanceList individualGrievanceList = gson.fromJson(response.toString(), IndividualGrievanceList.class);
            if (individualGrievanceList.getGrievanceArrayList() != null && individualGrievanceList.getGrievanceArrayList().size() > 0) {
//                    this.ongoingServiceArrayList.addAll(ongoingServiceList.getserviceArrayList());
                updateListAdapter(individualGrievanceList.getGrievanceArrayList());
            } else {
                if (grievances != null) {
                    grievances.clear();
                    updateListAdapter(individualGrievanceList.getGrievanceArrayList());
                }
            }
        }
    }

    protected void updateListAdapter(ArrayList<Grievance> grievanceArrayList) {
        grievances.clear();
        grievances.addAll(grievanceArrayList);
        if (grievanceListAdapter == null) {
            grievanceListAdapter = new GrievanceListAdapter(this, grievances);
            listView.setAdapter(grievanceListAdapter);
        } else {
            grievanceListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onEvent list item clicked" + position);
        Grievance grievance = null;
        if ((grievanceListAdapter != null) && (grievanceListAdapter.ismSearching())) {
            Log.d(TAG, "while searching");
            int actualindex = grievanceListAdapter.getActualEventPos(position);
            Log.d(TAG, "actual index" + actualindex);
            grievance = grievances.get(actualindex);
        } else {
            grievance = grievances.get(position);
        }
//        PreferenceStorage.saveUserId(this, news.getid());
        Intent intent = new Intent(this, IndividualGrievanceDetailActivity.class);
        intent.putExtra("serviceObj", grievance);
        startActivity(intent);
    }
}
