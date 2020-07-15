package com.gms.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gms.admin.R;
import com.gms.admin.activity.MeetingDetailActivity;
import com.gms.admin.activity.SearchResultActivity;
import com.gms.admin.activity.StaffDetailsActivity;
import com.gms.admin.adapter.MeetingListAdapter;
import com.gms.admin.adapter.StaffListAdapter;
import com.gms.admin.bean.support.Meeting;
import com.gms.admin.bean.support.MeetingList;
import com.gms.admin.bean.support.Paguthi;
import com.gms.admin.bean.support.Staff;
import com.gms.admin.bean.support.StaffList;
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

public class StaffFragment extends Fragment implements IServiceListener, DialogClickListener, StaffListAdapter.OnItemClickListener {

    private static final String TAG = MeetingsFragment.class.getName();
    private static ArrayList<Paguthi> paguthis = new ArrayList<>();
    private View view;
    private ServiceHelper serviceHelper;
    private int totalCount = 0, checkrun = 0;
    private boolean isLoadingForFirstTime = true;
    private ProgressDialogHelper progressDialogHelper;
    private int val;
    private LinearLayout loadMoreListView;
    private String paguthiID = "";
    private TextView constituentCount;
    private SearchView searchView;
    int cc = 0;
    private RecyclerView recyclerView;
//    SwipeRefreshLayout swipeRefreshLayout;
    int listcount = 0;
    StaffList staffList;
    ArrayList<Staff> staffArrayList = new ArrayList<>();
    StaffListAdapter mAdapter;

    public static MeetingsFragment newInstance(int position) {
        MeetingsFragment frag = new MeetingsFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_staff, container, false);
        serviceHelper = new ServiceHelper(view.getContext());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(view.getContext());
        constituentCount = view.findViewById(R.id.frag_meeting_count);

        recyclerView = view.findViewById(R.id.recycler_view);
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.list_refresh);
//        swipeRefreshLayout.setRefreshing(true);

//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
//
//                    if (!recyclerView.canScrollVertically(1)) {
//
////                        swipeRefreshLayout.setRefreshing(true);
//
////                        loadmore();
//
//                    }
//                }
//                return false;
//            }
//        });

        getStaffList(String.valueOf(listcount));

        return view;
    }
    private void loadmore() {
        listcount = listcount + 50;
        getStaffList(String.valueOf(listcount));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        inflater.inflate(R.menu.right_menu, menu);

        searchView = (SearchView) menu.findItem(R.id.menu_item).getActionView();
        searchView.setIconified(true);
        searchView.setQueryHint("Search for Staff");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

//                if (categoryArrayList.contains(query)) {
//                    preferenceAdatper.getFilter().filter(query);
//                } else {
//                    Toast.makeText(getActivity(), "No Match found", Toast.LENGTH_LONG).show();
//                }
//                    if (query != null) {
//                        makeSearch(query);
//                    }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);

                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        //Workaround for SearchView close listener
//        if (id == R.id.menu_item) {
//            startActivity(new Intent(getActivity(), NotificationActivity.class));
//        }

        return super.onOptionsItemSelected(item);
    }

    private void makeSearch(String eventname) {
        PreferenceStorage.setSearchFor(getActivity(), eventname);
        startActivity(new Intent(getActivity(), SearchResultActivity.class));
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            try {
                constituentCount.setText(response.getString("staff_count") + " Staffs");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            staffList = gson.fromJson(response.toString(), StaffList.class);
            staffArrayList.addAll(staffList.getStaffArrayList());
            mAdapter = new StaffListAdapter(staffArrayList, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            recyclerView.scrollToPosition(listcount);
//            swipeRefreshLayout.setRefreshing(false);

        }
    }

    private void getStaffList(String count) {
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            JSONObject jsonObject = new JSONObject();
            try {
//                if (val == 0) {
//                    paguthiID = "ALL";
//                }
                jsonObject.put(GMSConstants.KEY_CONSTITUENT_ID, PreferenceStorage.getConstituencyID(getActivity()));
//                jsonObject.put(GMSConstants.KEY_OFFSET, count);
//                jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_STAFF_LIST;
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
//                            swipeRefreshLayout.setVisibility(View.GONE);
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

    @Override
    public void onItemClick(View view, int position) {
        Staff meeting = null;
        meeting = staffArrayList.get(position);
        Intent intent = new Intent(getActivity(), StaffDetailsActivity.class);
        intent.putExtra("meetingObj", meeting.getid());
        startActivity(intent);
    }
}