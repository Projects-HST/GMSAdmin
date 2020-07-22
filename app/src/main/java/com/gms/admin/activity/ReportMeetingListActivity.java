package com.gms.admin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gms.admin.R;
import com.gms.admin.adapter.MeetingListAdapter;
import com.gms.admin.adapter.ReportMeetingListAdapter;
import com.gms.admin.adapter.ReportStaffListAdapter;
import com.gms.admin.bean.support.Meeting;
import com.gms.admin.bean.support.MeetingList;
import com.gms.admin.bean.support.ReportMeetingList;
import com.gms.admin.bean.support.ReportStaff;
import com.gms.admin.bean.support.ReportStaffList;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.PreferenceStorage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportMeetingListActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener, ReportMeetingListAdapter.OnItemClickListener {
    private static final String TAG = ReportStatusActivity.class.getName();

    private TextView reportCount;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    int listcount = 0;
    private ServiceHelper serviceHelper;
    private int totalCount = 0, checkrun = 0;
    private boolean isLoadingForFirstTime = true;
    private ProgressDialogHelper progressDialogHelper;
    ReportMeetingList reportMeetingList;
    ArrayList<Meeting> meetingArrayList = new ArrayList<>();
    String page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_grievance_list);

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

        reportCount = findViewById(R.id.frag_grievance_count);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_refresh);
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

        toolbar.setTitle(getString(R.string.report_meeting_title));
        getCategoryList(String.valueOf(listcount));
    }

    private void loadmore() {
        listcount = listcount + 50;
        getCategoryList(String.valueOf(listcount));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                makeSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void makeSearch(String eventname) {
        PreferenceStorage.setSearchFor(this, eventname);
        Intent intent = new Intent(this, SearchResultReportMeetingActivity.class);
        startActivity(intent);
    }

    private void getCategoryList(String count) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(GMSConstants.KEY_FROM_DATE, PreferenceStorage.getFromDate(this));
            jsonObject.put(GMSConstants.KEY_TO_DATE, PreferenceStorage.getToDate(this));
            jsonObject.put(GMSConstants.KEY_OFFSET, count);
            jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_REPORT_MEETING;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

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
                        if (totalCount == 0) {
                            AlertDialogHelper.showSimpleAlertDialog(this, msg);
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
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            int cou = 0;
            try {
                cou = Integer.parseInt(response.getString("result_count"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (cou > 1) {
                reportCount.setText(cou + " Records");
            } else {
                reportCount.setText(cou + " Record");
            }
            totalCount = cou;
            Gson gson = new Gson();
            reportMeetingList = gson.fromJson(response.toString(), ReportMeetingList.class);
            meetingArrayList.addAll(reportMeetingList.getMeetingArrayList());
            ReportMeetingListAdapter mAdapter = new ReportMeetingListAdapter(meetingArrayList, ReportMeetingListActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            recyclerView.scrollToPosition(listcount);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {

    }
}