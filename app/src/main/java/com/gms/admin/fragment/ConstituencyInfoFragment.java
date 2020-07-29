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

public class ConstituencyInfoFragment extends Fragment implements DialogClickListener, View.OnClickListener {

    private static final String TAG = ConstituencyInfoFragment.class.getName();

    private View rootView;
    private TextView constituencyName, ward, boothName, boothAddress, serialNo;

    public static ConstituencyInfoFragment newInstance(int position) {
        ConstituencyInfoFragment frag = new ConstituencyInfoFragment();
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

        rootView = inflater.inflate(R.layout.fragment_constituency_info, container, false);

        constituencyName = (TextView) rootView.findViewById(R.id.constituency_name);
        ward = (TextView) rootView.findViewById(R.id.ward);
        boothName = (TextView) rootView.findViewById(R.id.booth_name);
        boothAddress = (TextView) rootView.findViewById(R.id.booth_address);
        serialNo = (TextView) rootView.findViewById(R.id.serial_no);

        constituencyName.setText(PreferenceStorage.getPaguthiName(getActivity()));
        ward.setText(PreferenceStorage.getWard(getActivity()));
        boothName.setText(PreferenceStorage.getBooth(getActivity()));
        boothAddress.setText(PreferenceStorage.getBoothAddress(getActivity()));
        serialNo.setText(PreferenceStorage.getSerialNo(getActivity()));

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
