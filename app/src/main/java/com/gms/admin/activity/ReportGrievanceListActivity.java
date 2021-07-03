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
import com.gms.admin.adapter.ReportGrievanceListAdapter;
import com.gms.admin.adapter.ReportMeetingListAdapter;
import com.gms.admin.adapter.ReportStatusListAdapter;
import com.gms.admin.adapter.ReportWishesListAdapter;
import com.gms.admin.bean.support.ReportGrievance;
import com.gms.admin.bean.support.ReportGrievanceList;
import com.gms.admin.bean.support.ReportMeetingList;
import com.gms.admin.bean.support.ReportMeetings;
import com.gms.admin.bean.support.ReportStatusList;
import com.gms.admin.bean.support.ReportWishes;
import com.gms.admin.bean.support.ReportWishesList;
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

public class ReportGrievanceListActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener,
        ReportGrievanceListAdapter.OnItemClickListener, ReportMeetingListAdapter.OnItemClickListener, ReportStatusListAdapter.OnItemClickListener, ReportWishesListAdapter.OnItemClickListener {

    private static final String TAG = ReportStatusActivity.class.getName();

    private TextView reportCount;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    int listcount = 0;
    private ServiceHelper serviceHelper;
    private int totalCount = 0, checkrun = 0;
    private boolean isLoadingForFirstTime = true;
    private ProgressDialogHelper progressDialogHelper;
    ReportGrievanceList reportGrievanceList;
    ArrayList<ReportGrievance> reportStatusArrayList = new ArrayList<>();
    ArrayList<ReportGrievance> reportGrievanceArrayList = new ArrayList<>();
    ArrayList<ReportMeetings> reportMeetingsArrayList = new ArrayList<>();
    ArrayList<ReportWishes> reportWishesArrayList = new ArrayList<>();
    String whichReport;
    String page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_grievance_list);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);


        page = getIntent().getStringExtra("page");

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

        if (page.equalsIgnoreCase("status")) {
            toolbar.setTitle(getString(R.string.report_status_title));
            getReportStatusList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("grievance")) {
            toolbar.setTitle(getString(R.string.report_grievance_title));
            getReportGrievanceList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("meeting")) {
            toolbar.setTitle(getString(R.string.report_meeting_title));
            getMeetingReportList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("birthday")) {
            toolbar.setTitle(getString(R.string.report_birthday_title));
            getBirthdayReportList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("festival")) {
            toolbar.setTitle(getString(R.string.report_festival_title));
            getFestivalReportList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("constituent")) {
            toolbar.setTitle(getString(R.string.report_constituent_title));
            getContituentReportList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("video")) {
            toolbar.setTitle(getString(R.string.report_video_title));
            getReportVideoList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("staff")) {
            toolbar.setTitle(getString(R.string.report_staff_title));
            getReportStaffList(String.valueOf(listcount));
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
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
        Intent intent = new Intent(this, SearchResultReportGrievanceActivity.class);
        intent.putExtra("page", page);
        startActivity(intent);
    }


    private void loadmore() {
        listcount = listcount + 50;
        if (page.equalsIgnoreCase("status")) {
            getReportStatusList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("grievance")) {
            getReportGrievanceList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("meeting")) {
            getMeetingReportList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("birthday")) {
            getBirthdayReportList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("festival")) {
            getFestivalReportList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("constituent")) {
            getContituentReportList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("video")) {
            getReportVideoList(String.valueOf(listcount));
        } else if (page.equalsIgnoreCase("staff")) {
            getReportStaffList(String.valueOf(listcount));
        }
    }

    private void getReportStatusList(String count) {
        whichReport = "status";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_FROM_DATE, PreferenceStorage.getFromDate(this));
            jsonObject.put(GMSConstants.KEY_TO_DATE, PreferenceStorage.getToDate(this));
            jsonObject.put(GMSConstants.KEY_STATUS, PreferenceStorage.getReportStatus(this));

            jsonObject.put(GMSConstants.PAGUTHI, PreferenceStorage.getPaguthiID(this));
            jsonObject.put(GMSConstants.OFFICE, PreferenceStorage.getOfficeID(this));
            jsonObject.put(GMSConstants.KEY_OFFSET, count);
            jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_REPORT_STATUS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getReportGrievanceList(String count) {
        whichReport = "grievance";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_FROM_DATE, PreferenceStorage.getFromDate(this));
            jsonObject.put(GMSConstants.KEY_TO_DATE, PreferenceStorage.getToDate(this));
            jsonObject.put(GMSConstants.KEY_SEEKER_ID, PreferenceStorage.getReportSeeker(this));
            jsonObject.put(GMSConstants.KEY_GRIEVANCE_TYPE_ID, PreferenceStorage.getReportCategory(this));
            jsonObject.put(GMSConstants.KEY_SUB_CATEGORY_ID, PreferenceStorage.getReportSubCategory(this));
            jsonObject.put(GMSConstants.PAGUTHI, PreferenceStorage.getPaguthiID(this));
            jsonObject.put(GMSConstants.OFFICE, PreferenceStorage.getOfficeID(this));
            jsonObject.put(GMSConstants.KEY_OFFSET, count);
            jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_REPORT_GRIEVANCE;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getMeetingReportList(String count) {
        whichReport = "meeting";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_FROM_DATE, PreferenceStorage.getFromDate(this));
            jsonObject.put(GMSConstants.KEY_TO_DATE, PreferenceStorage.getToDate(this));
            jsonObject.put(GMSConstants.KEY_STATUS, PreferenceStorage.getReportStatus(this));
            jsonObject.put(GMSConstants.PAGUTHI, PreferenceStorage.getPaguthiID(this));
            jsonObject.put(GMSConstants.OFFICE, PreferenceStorage.getOfficeID(this));
            jsonObject.put(GMSConstants.KEY_OFFSET, count);
            jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_REPORT_MEETING;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getBirthdayReportList(String count) {
        whichReport = "birthdayWish";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_FROM_DATE, PreferenceStorage.getFromDate(this));
            jsonObject.put(GMSConstants.KEY_TO_DATE, PreferenceStorage.getToDate(this));
            jsonObject.put(GMSConstants.KEY_MONTH, PreferenceStorage.getReportStatus(this));
            jsonObject.put(GMSConstants.PAGUTHI, PreferenceStorage.getPaguthiID(this));
            jsonObject.put(GMSConstants.OFFICE, PreferenceStorage.getOfficeID(this));
            jsonObject.put(GMSConstants.KEY_OFFSET, count);
            jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_REPORT_BIRTHDAY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getFestivalReportList(String count) {
        whichReport = "festivalWish";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_FROM_DATE, PreferenceStorage.getFromDate(this));
            jsonObject.put(GMSConstants.KEY_TO_DATE, PreferenceStorage.getToDate(this));
            jsonObject.put(GMSConstants.KEY_FESTIVAL, PreferenceStorage.getReportStatus(this));
            jsonObject.put(GMSConstants.PAGUTHI, PreferenceStorage.getPaguthiID(this));
            jsonObject.put(GMSConstants.OFFICE, PreferenceStorage.getOfficeID(this));
            jsonObject.put(GMSConstants.KEY_OFFSET, count);
            jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_REPORT_FESTIVAL;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getContituentReportList(String count) {
        whichReport = "constituent";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_FROM_DATE, PreferenceStorage.getFromDate(this));
            jsonObject.put(GMSConstants.KEY_TO_DATE, PreferenceStorage.getToDate(this));
            jsonObject.put(GMSConstants.PAGUTHI, PreferenceStorage.getPaguthiID(this));
            jsonObject.put(GMSConstants.OFFICE, PreferenceStorage.getOfficeID(this));
            jsonObject.put(GMSConstants.KEY_WHATSAPP_NO, getIntent().getStringExtra("wh"));
            jsonObject.put(GMSConstants.KEY_MOBILE_NUMBER, getIntent().getStringExtra("ph"));
            jsonObject.put(GMSConstants.KEY_MAIL_ID, getIntent().getStringExtra("email"));
            jsonObject.put(GMSConstants.KEY_DOB, getIntent().getStringExtra("dob"));
            jsonObject.put(GMSConstants.KEY_VOTER_ID, getIntent().getStringExtra("vote"));
            jsonObject.put(GMSConstants.KEY_OFFSET, count);
            jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_REPORT_CONSTITUENT;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getReportVideoList(String count) {
        whichReport = "video";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, PreferenceStorage.getPaguthiID(this));
            jsonObject.put(GMSConstants.OFFICE, PreferenceStorage.getOfficeID(this));
            jsonObject.put(GMSConstants.KEY_OFFSET, count);
            jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_REPORT_VIDEO;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getReportStaffList(String count) {
        whichReport = "staff";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(GMSConstants.KEY_FROM_DATE, PreferenceStorage.getFromDate(this));
            jsonObject.put(GMSConstants.KEY_TO_DATE, PreferenceStorage.getToDate(this));
            jsonObject.put(GMSConstants.KEY_OFFSET, count);
            jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_REPORT_STAFF;
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
            if (page.equalsIgnoreCase("status")){
                Gson gson = new Gson();
                ReportStatusList reportStatusList = gson.fromJson(response.toString(), ReportStatusList.class);
                reportStatusArrayList.addAll(reportStatusList.getReportGrievanceArrayList());
                ReportStatusListAdapter mAdapter = new ReportStatusListAdapter(reportStatusArrayList, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mAdapter);
                recyclerView.scrollToPosition(listcount);
                swipeRefreshLayout.setRefreshing(false);
            }
            if (page.equalsIgnoreCase("grievance")) {
                Gson gson = new Gson();
                reportGrievanceList = gson.fromJson(response.toString(), ReportGrievanceList.class);
                reportGrievanceArrayList.addAll(reportGrievanceList.getReportGrievanceArrayList());
                ReportGrievanceListAdapter mAdapter = new ReportGrievanceListAdapter(reportGrievanceArrayList, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mAdapter);
                recyclerView.scrollToPosition(listcount);
                swipeRefreshLayout.setRefreshing(false);
            }
            if (page.equalsIgnoreCase("meeting")){
                Gson gson = new Gson();
                ReportMeetingList reportMeetingList = gson.fromJson(response.toString(), ReportMeetingList.class);
                reportMeetingsArrayList.addAll(reportMeetingList.getMeetingArrayList());
                ReportMeetingListAdapter mAdapter = new ReportMeetingListAdapter(reportMeetingsArrayList, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mAdapter);
                recyclerView.scrollToPosition(listcount);
                swipeRefreshLayout.setRefreshing(false);
            }
            if (page.equalsIgnoreCase("birthday")){
                Gson gson = new Gson();
                ReportWishesList reportWishesList = gson.fromJson(response.toString(), ReportWishesList.class);
                reportWishesArrayList.addAll(reportWishesList.getReportWishesArrayList());
                ReportWishesListAdapter mAdapter = new ReportWishesListAdapter(reportWishesArrayList, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mAdapter);
                recyclerView.scrollToPosition(listcount);
                swipeRefreshLayout.setRefreshing(false);
            }
            if (page.equalsIgnoreCase("festival")){
                Gson gson = new Gson();
                ReportWishesList reportWishesList = gson.fromJson(response.toString(), ReportWishesList.class);
                reportWishesArrayList.addAll(reportWishesList.getReportWishesArrayList());
                ReportWishesListAdapter mAdapter = new ReportWishesListAdapter(reportWishesArrayList, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(mAdapter);
                recyclerView.scrollToPosition(listcount);
                swipeRefreshLayout.setRefreshing(false);
            }
            if (page.equalsIgnoreCase("constituent")){

            }
            if (page.equalsIgnoreCase("video")){

            }
            if (page.equalsIgnoreCase("staff")){

            }
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onItemGrievanceClick(View view, int position) {
        ReportGrievance reportGrievance = null;
        reportGrievance = reportGrievanceArrayList.get(position);
        Intent intent = new Intent(this, GrievanceDetailActivity.class);
        intent.putExtra("grievanceObj", reportGrievance.getid());
        startActivity(intent);
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

    @Override
    public void onItemMeetingClick(View view, int position) {

    }

    @Override
    public void onItemStatusClick(View view, int position) {

    }

    @Override
    public void onItemWishesClick(View view, int position) {

    }
}