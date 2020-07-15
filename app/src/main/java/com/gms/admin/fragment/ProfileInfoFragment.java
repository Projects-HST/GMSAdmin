package com.gms.admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gms.admin.R;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.utils.PreferenceStorage;

public class ProfileInfoFragment extends Fragment implements DialogClickListener, View.OnClickListener {

    private View rootView;
    private TextView address, pincode, fatherHusbandName, mobileNo, whatsappNo, emailId, religionName, aadhaarNo,
            voterIdNo, dob, gender, changeUser, aboutUs, terms, shareApp, signout;


    public static ProfileInfoFragment newInstance(int position) {
        ProfileInfoFragment frag = new ProfileInfoFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile_info, container, false);

        fatherHusbandName = (TextView) rootView.findViewById(R.id.father_husband_name);
        emailId = (TextView) rootView.findViewById(R.id.email_id);
        mobileNo = (TextView) rootView.findViewById(R.id.phone_no);
        whatsappNo = (TextView) rootView.findViewById(R.id.whatsapp_no);
        dob = (TextView) rootView.findViewById(R.id.dob);
        gender = (TextView) rootView.findViewById(R.id.gender);
        religionName = (TextView) rootView.findViewById(R.id.religion);
        address = (TextView) rootView.findViewById(R.id.address);
        pincode = (TextView) rootView.findViewById(R.id.pincode);
        voterIdNo = (TextView) rootView.findViewById(R.id.voter_id);
        aadhaarNo = (TextView) rootView.findViewById(R.id.aadhaar);

        fatherHusbandName.setText(PreferenceStorage.getfatherORhusband(getActivity()));
        emailId.setText(PreferenceStorage.getEmail(getActivity()));
        mobileNo.setText(PreferenceStorage.getMobileNo(getActivity()));
        whatsappNo.setText(PreferenceStorage.getWhatsappNo(getActivity()));
        dob.setText(PreferenceStorage.getDob(getActivity()));
        gender.setText(PreferenceStorage.getGender(getActivity()));
        religionName.setText(PreferenceStorage.getReligionName(getActivity()));
        address.setText(PreferenceStorage.getAddress(getActivity()));
        pincode.setText(PreferenceStorage.getPincode(getActivity()));
        voterIdNo.setText(PreferenceStorage.getVoterId(getActivity()));
        aadhaarNo.setText(PreferenceStorage.getAadhaarNo(getActivity()));


        return rootView;
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

}
