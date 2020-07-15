package com.gms.admin.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.serviceinterfaces.IServiceListener;

import org.json.JSONObject;

public class SettingsFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}
