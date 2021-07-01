package com.gms.admin.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gms.admin.R;
import com.gms.admin.activity.GrievanceDetailActivity;
import com.gms.admin.adapter.SampleAdapter;
import com.gms.admin.bean.support.Grievance;
import com.gms.admin.bean.support.GrievanceList;
import com.gms.admin.bean.support.Paguthi;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.CommonUtils;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.PreferenceStorage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DynamicGrievanceFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener, SampleAdapter.OnItemClickListener {

    private static final String TAG = DynamicGrievanceFragment.class.getName();
    private static ArrayList<Paguthi> paguthis = new ArrayList<>();
    public View view;
    private ServiceHelper serviceHelper;
    private int totalCount = 0, checkrun = 0;
    private boolean isLoadingForFirstTime = true;
    private ProgressDialogHelper progressDialogHelper;
    private int val, colour;
    public LinearLayout loadMoreListView;
    private String paguthiID = "";
    GrievanceList grievanceList;
    private TextView grievCount, all, pet, enq;
    GradientDrawable drawable;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    int listcount = 0;
    String subtabName = "A";
    ArrayList<Grievance> grievanceArrayList = new ArrayList<>();;

    public static DynamicGrievanceFragment newInstance(int val, ArrayList<Paguthi> paguthiArrayList) {
        DynamicGrievanceFragment fragment = new DynamicGrievanceFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", val);
        fragment.setArguments(args);
        paguthis = paguthiArrayList;

        return fragment;
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dynamic_grievance_list, container, false);

        colour = Color.parseColor(PreferenceStorage.getAppBaseColor(getActivity()));

        drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(30);
        drawable.setColor(colour);

        serviceHelper = new ServiceHelper(view.getContext());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(view.getContext());
        val = getArguments().getInt("someInt", 0);
//        if (val!=0) {
//            paguthiID = paguthis.get(val - 1).getid();
//        } else {
            paguthiID = paguthis.get(val).getid();
//        }
//        loadMoreListView = view.findViewById(R.id.list_view_grievance);
        grievCount = view.findViewById(R.id.frag_grievance_count);
        all = view.findViewById(R.id.all);
        pet = view.findViewById(R.id.petition);
        enq = view.findViewById(R.id.enquiry);
        all.setOnClickListener(this);
        all.setBackground(drawable);
        pet.setOnClickListener(this);
        enq.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.list_refresh);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(false);

            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

                    if (!recyclerView.canScrollVertically(1))
                    {

                        swipeRefreshLayout.setRefreshing(true);

                        loadmore();

                    }
                }
                return false;
            }
        });
        PreferenceStorage.saveGrievanceType(getActivity(), subtabName);

        getGrievanceList(subtabName, String.valueOf(listcount));

        return view;
    }

    private void loadmore() {
        listcount = listcount + 50;
        getGrievanceList(subtabName, String.valueOf(listcount));
    }

    @Override
    public void onClick(View v) {

        if (v == all) {

            all.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            all.setBackground(drawable);
            pet.setTextColor(ContextCompat.getColor(getActivity(), R.color.msg_by_grey));
            pet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_round_outline_noclor));
            enq.setTextColor(ContextCompat.getColor(getActivity(), R.color.msg_by_grey));
            enq.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_round_outline_noclor));
            subtabName = "A";
            listcount = 0;
            swipeRefreshLayout.setRefreshing(true);
            grievanceArrayList.clear();
            PreferenceStorage.saveGrievanceType(all.getContext(), subtabName);
            getGrievanceList(subtabName, String.valueOf(listcount));
        }
        if (v == pet) {
            all.setTextColor(ContextCompat.getColor(getActivity(), R.color.msg_by_grey));
            all.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_round_outline_noclor));
            pet.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            pet.setBackground(drawable);
            enq.setTextColor(ContextCompat.getColor(getActivity(), R.color.msg_by_grey));
            enq.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_round_outline_noclor));
            subtabName = "P";
            listcount = 0;
            swipeRefreshLayout.setRefreshing(true);
            grievanceArrayList.clear();
            PreferenceStorage.saveGrievanceType(pet.getContext(), subtabName);
            getGrievanceList(subtabName, String.valueOf(listcount));
        }
        if (v == enq) {
            all.setTextColor(ContextCompat.getColor(getActivity(), R.color.msg_by_grey));
            all.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_round_outline_noclor));
            pet.setTextColor(ContextCompat.getColor(getActivity(), R.color.msg_by_grey));
            pet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_round_outline_noclor));
            enq.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            enq.setBackground(drawable);
            subtabName = "E";
            listcount = 0;
            swipeRefreshLayout.setRefreshing(true);
            grievanceArrayList.clear();
            PreferenceStorage.saveGrievanceType(enq.getContext(), subtabName);
            getGrievanceList(subtabName, String.valueOf(listcount));
        }
    }

    private void getGrievanceList(String tab, String count) {
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(GMSConstants.PAGUTHI, paguthiID);
                jsonObject.put(GMSConstants.KEY_GRIEVANCE_TYPE, tab);
                jsonObject.put(GMSConstants.KEY_OFFSET, count);
                jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");
                jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_GRIEVANCE_LIST_NEW;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        } else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), getString(R.string.error_no_net),
                    R.style.alertDialogueTheme);
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            try {
                grievCount.setText(response.getString("grievance_count") + " Grievances");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            grievanceList = gson.fromJson(response.toString(), GrievanceList.class);
            grievanceArrayList.addAll(grievanceList.getGrievanceArrayList());
            SampleAdapter mAdapter = new SampleAdapter(grievanceArrayList, DynamicGrievanceFragment.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            recyclerView.scrollToPosition(listcount);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Grievance grievance = null;
        grievance = grievanceArrayList.get(position);
        Intent intent = new Intent(getActivity(), GrievanceDetailActivity.class);
        intent.putExtra("grievanceObj", grievance.getid());
        startActivity(intent);
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
//                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), msg);
                        if (listcount == 0) {
                            view.findViewById(R.id.list_refresh).setVisibility(View.GONE);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onError(String error) {

    }
}