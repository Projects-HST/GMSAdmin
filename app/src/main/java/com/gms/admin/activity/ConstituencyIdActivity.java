package com.gms.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gms.admin.R;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.PreferenceStorage;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class ConstituencyIdActivity extends AppCompatActivity implements IServiceListener, View.OnClickListener {

    private static final String TAG = ConstituencyIdActivity.class.getName();
    private TextInputEditText constituency_code;
    private TextView next;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constituency_id);

        constituency_code = (TextInputEditText)findViewById(R.id.txt_code);
        next = (TextView)findViewById(R.id.next);
        next.setOnClickListener(this);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

    }

    public void checkConstituencyCode(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_CONSTITUENCY_CODE, constituency_code.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = GMSConstants.CHECK_CONSTITUENCY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(),url);
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

        if (validateResponse(response)){

            try {
//                String constituencyName = response.getJSONArray("userData").getJSONObject(0).getString("constituency_name");
                String dynamicDB = response.getJSONObject("dynamic_db").getString("dynamic_db");

//                PreferenceStorage.saveConstituencyName(getApplicationContext(), constituencyName);
                PreferenceStorage.setDynamicDb(getApplicationContext(), dynamicDB);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onClick(View view) {

        if (view == next){
            checkConstituencyCode();
        }
    }
}