package com.gms.admin.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gms.admin.R;
import com.gms.admin.adapter.ReportBirthdayListAdapter;
import com.gms.admin.bean.support.Birthday;
import com.gms.admin.bean.support.BirthdayList;
import com.gms.admin.bean.support.SpinnerData;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ReportFestivalActivity  extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener, ReportBirthdayListAdapter.OnItemClickListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = ReportStatusActivity.class.getName();
    private String checkRes = "", monthId = "0";
    private String paguthiId = "0", officeId = "0";
    private SearchView searchView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ArrayList<SpinnerData> paguthispinnerData;
    private ArrayAdapter<SpinnerData> paguthispinnerDataArrayAdapter = null;
    private ArrayList<SpinnerData> officespinnerData;
    private ArrayAdapter<SpinnerData> officespinnerDataArrayAdapter;
    private ArrayList<SpinnerData> spinnerData;
    private ArrayAdapter<SpinnerData> spinnerDataArrayAdapter = null;
    private TextView month;
    private LinearLayout selectMonth;
    private SimpleDateFormat mDateFormatter;
    private DatePickerDialog mDatePicker;
    boolean fr = false, t = false;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    int listcount = 0;
    private LinearLayout listLay;
    private TextView reportCount;
    private BirthdayList birthdayList;
    private ArrayList<Birthday> birthdayArrayList = new ArrayList<>();
    private int totalCount = 0, checkrun = 0;
    private MenuItem searchItem ;
    private ReportBirthdayListAdapter mAdapter ;
    private TextView dateFrom, dateTo, status,office, paguthi;
    private LinearLayout selectOffice,selectPaguthi, selectStatus;
    private TextView search, clearData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_festival);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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
        toolbar.setTitle(getString(R.string.report_festival_title));

        dateFrom = findViewById(R.id.from_date);
        dateTo = findViewById(R.id.to_date);
        status = findViewById(R.id.report_month);
        paguthi = findViewById(R.id.report_paguthi);
        selectStatus = findViewById(R.id.status_month);
        selectPaguthi = findViewById(R.id.paguthi_select);

        office = findViewById(R.id.text_office);
        selectOffice = findViewById(R.id.select_office);
        search = findViewById(R.id.search);
        clearData = findViewById(R.id.clear_data);
        clearData.setOnClickListener(this);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(10);
        drawable.setColor(Color.parseColor(PreferenceStorage.getAppBaseColor(this)));
        search.setBackground(drawable);
        search.setOnClickListener(this);
        selectOffice.setOnClickListener(this);

        selectStatus.setOnClickListener(this);
        dateFrom.setOnClickListener(this);
        dateTo.setOnClickListener(this);
        selectPaguthi.setOnClickListener(this);

        mDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        getFestival();
    }

    private void getFestivalList() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_FESTIVAL;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {

        if (v == dateFrom) {
            fr = true;
            t = false;
            showBirthdayDate();
        }
        if (v == dateTo) {
            fr = false;
            t = true;
            showBirthdayDate();
        }
        if (v == selectStatus) {
            showSpinnerData();
        }
        if (v == selectPaguthi) {
            showPaguthiSpinnerData();
        }
        if (v == selectOffice) {
            showOfficeSpinnerData();
        }
        if (v == search) {
            if (validateFields()) {
                sendSearch();
            }
        }
        if (v == clearData) {
            dateFrom.setText("");
            dateFrom.setHint(R.string.from_date);

            dateTo.setText("");
            dateTo.setHint(R.string.to_date);

            paguthiId = "0";
            officeId = "0";
            paguthi.setText("Select Paguthi");
            office.setText("Select Office");
        }
    }

    private void showBirthdayDate() {
        Log.d(TAG, "Show the birthday date");
        Calendar newCalendar = Calendar.getInstance();
        String currentdate = "";
        if (fr) {
            currentdate = dateFrom.getText().toString();
        } else {
            currentdate = dateTo.getText().toString();
        }
        Log.d(TAG, "current date is" + currentdate);
        int month = newCalendar.get(Calendar.MONTH);
        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
        int year = newCalendar.get(Calendar.YEAR);
        if ((currentdate != null) && !(currentdate.isEmpty())) {
            //extract the date/month and year
            try {
                Date startDate = mDateFormatter.parse(currentdate);
                Calendar newDate = Calendar.getInstance();

                newDate.setTime(startDate);
                month = newDate.get(Calendar.MONTH);
                day = newDate.get(Calendar.DAY_OF_MONTH);
                year = newDate.get(Calendar.YEAR);
                Log.d(TAG, "month" + month + "day" + day + "year" + year);

            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                mDatePicker = new DatePickerDialog(this, R.style.datePickerTheme, this, year, month, day);
                mDatePicker.show();
            }
        } else {
            Log.d(TAG, "show default date");

            mDatePicker = new DatePickerDialog(this, R.style.datePickerTheme, this, year, month, day);
            mDatePicker.show();
        }
    }

    private void showPaguthiSpinnerData() {
        Log.d(TAG, "Show Spinner Data");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.spinner_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.spinner_header);
        header.setText("Select Paguthi");
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(paguthispinnerDataArrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpinnerData spinnerDatas = paguthispinnerData.get(which);
                        paguthi.setText(spinnerDatas.getName());
                        paguthiId = spinnerDatas.getId();
//                        rotate(90.0f, 0.0f);
                    }
                });
        builderSingle.show();
        builderSingle.setOnDismissListener(new AlertDialog.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                rotate(90.0f, 0.0f);
            }
        });
    }

    private void showOfficeSpinnerData() {
        Log.d(TAG, "Show timing lists");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.spinner_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.spinner_header);
        header.setText(getString(R.string.select_office));
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(officespinnerDataArrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpinnerData spinnerDatas = officespinnerData.get(which);
                        office.setText(spinnerDatas.getName());
                        officeId = spinnerDatas.getId();
//                        getDashboard();
                    }
                });
        builderSingle.show();
    }

    private void showSpinnerData() {
        Log.d(TAG, "Show Spinner Data");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.spinner_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.spinner_header);
        header.setText(getString(R.string.select_month));
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(spinnerDataArrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpinnerData spinnerDatas = spinnerData.get(which);
                        month.setText(spinnerDatas.getName());
                        monthId = spinnerDatas.getId();
                        PreferenceStorage.saveMonth(getApplicationContext(), monthId);
//                        rotate(90.0f, 0.0f);
                    }
                });
        builderSingle.show();
        builderSingle.setOnDismissListener(new AlertDialog.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                rotate(90.0f, 0.0f);
            }
        });
    }

    private boolean validateFields() {

        if (dateFrom.getText().toString().equalsIgnoreCase("From Date")) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Select from date", R.style.alertDialogueTheme);
            return false;
        }
        if (dateTo.getText().toString().equalsIgnoreCase("To Date")) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Select to date", R.style.alertDialogueTheme);
            return false;
        }
        if (paguthiId.equalsIgnoreCase("0")) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Select paguthi", R.style.alertDialogueTheme);
            return false;
        }
        if (officeId.equalsIgnoreCase("0")) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Select office", R.style.alertDialogueTheme);
            return false;
        }
        if (status.getText().toString().trim().equalsIgnoreCase("Select Status")) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Select status", R.style.alertDialogueTheme);
            return false;
        }
        if (!checkTime()) {
            AlertDialogHelper.showSimpleAlertDialog(this, "End date cannot be before start date", R.style.alertDialogueTheme);
            return false;
        }
        return true;
    }

    private boolean checkTime() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String dateString1 = dateFrom.getText().toString();
            String dateString2 = dateTo.getText().toString();
            Date date1 = null;
            Date date2 = null;
            date1 = sdf.parse(dateString1);
            date2 = sdf.parse(dateString2);
            if (date2.before(date1)) {
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.i(TAG, "parce exception");
        }
        return true;

    }

    private void getFestival() {
        checkRes = "festival";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_FESTIVAL;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getPaguthi() {
        checkRes = "paguthi";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_CONSTITUENCY_ID, PreferenceStorage.getConstituencyID(this));
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_PAGUTHI;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getOffice() {
        checkRes = "office";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_CONSTITUENCY_ID, PreferenceStorage.getConstituencyID(this));
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_OFFICE;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void sendSearch() {
        PreferenceStorage.saveFromDate(this, getserverdateformat(dateFrom.getText().toString()));
        PreferenceStorage.saveToDate(this, getserverdateformat(dateTo.getText().toString()));
        PreferenceStorage.saveReportStatus(this, monthId);
        PreferenceStorage.savePaguthiID(this, paguthiId);
        PreferenceStorage.saveOfficeID(this, officeId);
        Intent intt = new Intent(this, ReportGrievanceListActivity.class);
        intt.putExtra("page", "festival");
        startActivity(intt);
    }

    private String getserverdateformat(String dd) {
        String serverFormatDate = "";
        if (dd != null && dd != "") {

            String date = dd;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date testDate = null;
            try {
                testDate = sdf.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            serverFormatDate = formatter.format(testDate);
            System.out.println(".....Date..." + serverFormatDate);
        }
        return serverFormatDate;
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
                        AlertDialogHelper.showSimpleAlertDialog(this, msg, R.style.alertDialogueTheme);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            try {

                if (checkRes.equalsIgnoreCase("paguthi")) {
                    JSONArray getData = response.getJSONArray("paguthi_details");
                    int getLength = getData.length();
                    String id = "";
                    String name = "";
                    paguthispinnerData = new ArrayList<>();
                    paguthispinnerData.add(new SpinnerData("ALL", "All"));

                    for (int i = 0; i < getLength; i++) {
                        id = getData.getJSONObject(i).getString("id");
                        name = capitalizeString(getData.getJSONObject(i).getString("paguthi_name"));
                        paguthispinnerData.add(new SpinnerData(id, name));
                    }

                    //fill data in spinner
                    paguthispinnerDataArrayAdapter = new ArrayAdapter<SpinnerData>(this, R.layout.spinner_data_layout, R.id.data_name, paguthispinnerData) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            Log.d(TAG, "getview called" + position);
                            View view = getLayoutInflater().inflate(R.layout.spinner_data_layout, parent, false);
                            TextView gendername = (TextView) view.findViewById(R.id.data_name);
                            gendername.setText(paguthispinnerData.get(position).getName());

                            // ... Fill in other views ...
                            return view;
                        }
                    };
                    getOffice();
                }if (checkRes.equalsIgnoreCase("office")) {
                    JSONArray getData = response.getJSONArray("list_details");
                    int getLength = getData.length();
                    String id = "";
                    String name = "";
                    officespinnerData = new ArrayList<>();
                    officespinnerData.add(new SpinnerData("ALL", "All"));

                    for (int i = 0; i < getLength; i++) {
                        id = getData.getJSONObject(i).getString("id");
                        name = capitalizeString(getData.getJSONObject(i).getString("office_name"));
                        officespinnerData.add(new SpinnerData(id, name));
                    }
                    //fill data in spinner
                    officespinnerDataArrayAdapter = new ArrayAdapter<SpinnerData>(this, R.layout.spinner_data_layout, R.id.data_name, officespinnerData) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            Log.d(TAG, "getview called" + position);
                            View view = getLayoutInflater().inflate(R.layout.spinner_data_layout, parent, false);
                            TextView gendername = (TextView) view.findViewById(R.id.data_name);
                            gendername.setText(officespinnerData.get(position).getName());

                            // ... Fill in other views ...
                            return view;
                        }
                    };
                }if (checkRes.equalsIgnoreCase("festival")) {
                    JSONArray getData = response.getJSONArray("festivals");
                    int getLength = getData.length();
                    String id = "";
                    String name = "";
                    spinnerData = new ArrayList<>();
                    spinnerData.add(new SpinnerData("ALL", "All"));

                    for (int i = 0; i < getLength; i++) {
                        id = getData.getJSONObject(i).getString("id");
                        name = capitalizeString(getData.getJSONObject(i).getString("festival_name"));
                        spinnerData.add(new SpinnerData(id, name));
                    }
                    //fill data in spinner
                    spinnerDataArrayAdapter = new ArrayAdapter<SpinnerData>(this, R.layout.spinner_data_layout, R.id.data_name, spinnerData) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            Log.d(TAG, "getview called" + position);
                            View view = getLayoutInflater().inflate(R.layout.spinner_data_layout, parent, false);
                            TextView gendername = (TextView) view.findViewById(R.id.data_name);
                            gendername.setText(spinnerData.get(position).getName());

                            // ... Fill in other views ...
                            return view;
                        }
                    };
                    getPaguthi();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

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
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar date = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        if (fr) {
            dateFrom.setText(mDateFormatter.format(date.getTime()));
        } else {
            dateTo.setText(mDateFormatter.format(date.getTime()));
        }
        fr = false;
        t = false;
    }
}
