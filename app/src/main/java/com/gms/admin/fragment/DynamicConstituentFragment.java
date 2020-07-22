package com.gms.admin.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gms.admin.R;
import com.gms.admin.activity.ConstituentDetailsActivity;
import com.gms.admin.adapter.ConstituentListAdapter;
import com.gms.admin.bean.support.ConstituentUserList;
import com.gms.admin.bean.support.Paguthi;
import com.gms.admin.bean.support.User;
import com.gms.admin.customview.CircleImageView;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.CommonUtils;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.GMSValidator;
import com.gms.admin.utils.PreferenceStorage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.util.Log.d;

public class DynamicConstituentFragment extends Fragment implements IServiceListener, DialogClickListener, ConstituentListAdapter.OnItemClickListener{

    private static final String TAG = DynamicConstituentFragment.class.getName();
    private static ArrayList<Paguthi> paguthis = new ArrayList<>();
    public View view;
    private ServiceHelper serviceHelper;
    private int totalCount = 0, checkrun = 0;
    private boolean isLoadingForFirstTime = true;
    private ProgressDialogHelper progressDialogHelper;
    private int val;
    public LinearLayout loadMoreListView;
    private String paguthiID = "";
    ConstituentUserList userList;
    ArrayList<User> userArrayList = new ArrayList<>();
    private TextView constituentCount;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    int listcount = 0;

    public static DynamicConstituentFragment newInstance(int val, ArrayList<Paguthi> paguthiArrayList) {
        DynamicConstituentFragment fragment = new DynamicConstituentFragment();
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
        view = inflater.inflate(R.layout.fragment_dynamic_constituent_list, container, false);
        serviceHelper = new ServiceHelper(view.getContext());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(view.getContext());
        val = getArguments().getInt("someInt", 0);
        paguthiID = paguthis.get(val).getid();
        loadMoreListView = view.findViewById(R.id.list_view_user);
        constituentCount = view.findViewById(R.id.frag_constituent_count);

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

                    if (!recyclerView.canScrollVertically(1)) {

                        swipeRefreshLayout.setRefreshing(true);

                        loadmore();

                    }
                }
                return false;
            }
        });

        getUsersList(String.valueOf(listcount));

        return view;
    }

    private void loadmore() {
        listcount = listcount + 50;
        getUsersList(String.valueOf(listcount));
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            try {
                constituentCount.setText(response.getString("constituent_count") + " Constituents");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            userList = gson.fromJson(response.toString(), ConstituentUserList.class);
            userArrayList.addAll(userList.getUserArrayList());
            ConstituentListAdapter mAdapter = new ConstituentListAdapter(userArrayList, DynamicConstituentFragment.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            recyclerView.scrollToPosition(listcount);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        User user = null;
        user = userArrayList.get(position);
        Intent intent = new Intent(getActivity(), ConstituentDetailsActivity.class);
        intent.putExtra("userObj", user);
        startActivity(intent);
    }

    private void getUsersList(String count) {
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            JSONObject jsonObject = new JSONObject();
            try {
//                if (val == 0) {
//                    paguthiID = "ALL";
//                }
                jsonObject.put(GMSConstants.PAGUTHI, paguthiID);
                jsonObject.put(GMSConstants.KEY_OFFSET, count);
                jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_CONSTITUENT_LIST;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        } else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), getString(R.string.error_no_net));
        }
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
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }
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
