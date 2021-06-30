package com.gms.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.gms.admin.R;
import com.gms.admin.activity.ChangePasswordActivity;
import com.gms.admin.activity.EditProfileActivity;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.serviceinterfaces.IServiceListener;

import org.json.JSONObject;

public class SettingsFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {

    private View rootView;
    private ImageView editProfile, changePassword, about, terms, privacy, support;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        editProfile = rootView.findViewById(R.id.profileClick);
        changePassword = rootView.findViewById(R.id.passwordClick);
        about = rootView.findViewById(R.id.abtClick);
        terms = rootView.findViewById(R.id.termsClick);
        privacy = rootView.findViewById(R.id.privacyClick);
        support = rootView.findViewById(R.id.helpClick);

        editProfile.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        about.setOnClickListener(this);
        terms.setOnClickListener(this);
        privacy.setOnClickListener(this);
        support.setOnClickListener(this);

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
//            Intent i = new Intent (getActivity(), SampleClassasa.class);
//            startActivity(i);
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
