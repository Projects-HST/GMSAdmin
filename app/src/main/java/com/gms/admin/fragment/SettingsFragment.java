package com.gms.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gms.admin.R;
import com.gms.admin.activity.ChangePasswordActivity;
import com.gms.admin.activity.EditProfileActivity;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.serviceinterfaces.IServiceListener;

import org.json.JSONObject;

public class SettingsFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {

    private View rootView;
    private TextView editProfile, changePassword, about, terms, privacy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        editProfile = rootView.findViewById(R.id.edit_profile);
        changePassword = rootView.findViewById(R.id.change_password);
        about = rootView.findViewById(R.id.about);
        terms = rootView.findViewById(R.id.terms);
        privacy = rootView.findViewById(R.id.priv);

        editProfile.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        about.setOnClickListener(this);
        terms.setOnClickListener(this);
        privacy.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v == editProfile) {
            Intent i = new Intent (getActivity(), EditProfileActivity.class);
            startActivity(i);
            getActivity().finish();
        }if (v == changePassword) {
            Intent i = new Intent (getActivity(), ChangePasswordActivity.class);
            startActivity(i);
        }if (v == about) {

        }if (v == terms) {

        }if (v == privacy) {

        }
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
