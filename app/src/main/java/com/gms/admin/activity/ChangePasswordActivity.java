package com.gms.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gms.admin.R;
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

import org.json.JSONException;
import org.json.JSONObject;


public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = ChangePasswordActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private TextInputEditText edtOld, edtNew, edtConfirm;
    private TextInputLayout txtOld, txtNew, txtConfirm;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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

        txtOld = (TextInputLayout) findViewById(R.id.ti_old_password);
        txtNew = (TextInputLayout) findViewById(R.id.ti_new_password);
        txtConfirm = (TextInputLayout) findViewById(R.id.ti_confirm_password);
        edtOld = (TextInputEditText) findViewById(R.id.old_password);
        edtNew = (TextInputEditText) findViewById(R.id.new_password);
        edtConfirm = (TextInputEditText) findViewById(R.id.confirm_password);

        edtOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtOld.setError(null);
            }
        });
        edtNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNew.setError(null);
            }
        });
        edtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtConfirm.setError(null);
            }
        });

        save = (Button) findViewById(R.id.btn_save);

        save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == save) {
            if (validateFields()) {
                sendProfile();
            }
        }
    }

    private boolean validateFields() {
        if (!GMSValidator.checkNullString(this.edtOld.getText().toString().trim())) {
            txtOld.setError(getString(R.string.empty_entry));
            requestFocus(txtOld);
            return false;
        }
        if (!GMSValidator.checkNullString(this.edtNew.getText().toString().trim())) {
            txtNew.setError(getString(R.string.empty_entry));
            requestFocus(txtNew);
            return false;
        }
        if (!GMSValidator.checkStringMinLength(6, this.edtNew.getText().toString().trim())) {
            txtNew.setError("Password should be minimum of 6 characters");
            requestFocus(txtNew);
            return false;
        }
        if (!GMSValidator.checkStringMinLength(6, this.edtConfirm.getText().toString().trim())) {
            txtConfirm.setError("Password should be minimum of 6 characters");
            requestFocus(txtConfirm);
            return false;
        }
        if (!GMSValidator.checkStringMinLength(6, this.edtConfirm.getText().toString().trim())) {
            txtConfirm.setError("Password should be minimum of 6 characters");
            requestFocus(txtConfirm);
            return false;
        }
        if (!edtNew.getText().toString().equalsIgnoreCase(edtConfirm.getText().toString())) {
            txtNew.setError(getString(R.string.mismatch_entry));
            txtConfirm.setError(getString(R.string.mismatch_entry));
            requestFocus(txtConfirm);
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

    private void sendProfile() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(GMSConstants.KEY_OLD_PASSWORD, edtOld.getText());
            jsonObject.put(GMSConstants.KEY_NEW_PASSWORD, edtNew.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.CHANGE_PASSWORD;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }


    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
            try {
                Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onError(String error) {

    }
}
