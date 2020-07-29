package com.gms.admin.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gms.admin.R;
import com.gms.admin.adapter.ReportBirthdayListAdapter;
import com.gms.admin.adapter.ReportGrievanceListAdapter;
import com.gms.admin.bean.support.Birthday;
import com.gms.admin.bean.support.BirthdayList;
import com.gms.admin.bean.support.ReportGrievanceList;
import com.gms.admin.bean.support.SpinnerData;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.GMSValidator;
import com.gms.admin.utils.PreferenceStorage;
import com.google.gson.Gson;

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

public class ReportBirthdayActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener, ReportBirthdayListAdapter.OnItemClickListener {
    private static final String TAG = ReportStatusActivity.class.getName();
    private String checkRes = "", paguthiId = "0";
    private SearchView searchView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ArrayList<SpinnerData> spinnerData;
    private ArrayAdapter<SpinnerData> spinnerDataArrayAdapter = null;
    private TextView paguthi;
    private LinearLayout selectPaguthi;
    private SimpleDateFormat mDateFormatter;
    private DatePickerDialog mDatePicker;
    boolean fr = false, t = false;
    private Button search;
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    int listcount = 0;
    private LinearLayout listLay;
    private TextView reportCount;
    private BirthdayList birthdayList;
    private ArrayList<Birthday> birthdayArrayList = new ArrayList<>();
    private int totalCount = 0, checkrun = 0;
    private MenuItem searchItem ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_birthday);

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
        toolbar.setTitle(getString(R.string.report_birthday_title));

        paguthi = findViewById(R.id.report_month);
        selectPaguthi = findViewById(R.id.status_month);
        search = findViewById(R.id.search);
        reportCount = findViewById(R.id.frag_grievance_count);

        listLay = findViewById(R.id.list_layout);
        listLay.setVisibility(View.GONE);

        selectPaguthi.setOnClickListener(this);
        search.setOnClickListener(this);

        mDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        spinnerData = new ArrayList<>();
        spinnerData.add(new SpinnerData("01", "January"));
        spinnerData.add(new SpinnerData("02", "February"));
        spinnerData.add(new SpinnerData("03", "March"));
        spinnerData.add(new SpinnerData("04", "April"));
        spinnerData.add(new SpinnerData("05", "May"));
        spinnerData.add(new SpinnerData("06", "June"));
        spinnerData.add(new SpinnerData("07", "July"));
        spinnerData.add(new SpinnerData("08", "August"));
        spinnerData.add(new SpinnerData("09", "September"));
        spinnerData.add(new SpinnerData("10", "October"));
        spinnerData.add(new SpinnerData("11", "November"));
        spinnerData.add(new SpinnerData("12", "December"));

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu, menu);
        searchItem = menu.findItem(R.id.menu_item);
        searchItem.setVisible(false);
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
        if (validateFields()) {
            PreferenceStorage.setSearchFor(this, eventname);
            Intent intent = new Intent(this, SearchResultBirthdayActivity.class);
            startActivity(intent);
        }
    }

    private void loadmore() {
        listcount = listcount + 50;
        getCategoryList(String.valueOf(listcount));
    }

    private void getCategoryList(String count) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(GMSConstants.KEY_MONTH, PreferenceStorage.getMonth(this));
            jsonObject.put(GMSConstants.KEY_OFFSET, count);
            jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_REPORT_BIRTHDAY;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {

        if (v == selectPaguthi) {
            showSpinnerData();
        }
        if (v == search) {
            if (validateFields()) {
                searchItem.setVisible(true);
                sendSearch();
            }
        }
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
                        paguthi.setText(spinnerDatas.getName());
                        paguthiId = spinnerDatas.getId();
                        PreferenceStorage.saveMonth(getApplicationContext(), paguthiId);
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

        if (paguthiId.isEmpty() || paguthiId.equalsIgnoreCase("0")) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Select Month");
            return false;
        }
        return true;
    }

    private void sendSearch() {
        PreferenceStorage.saveMonth(this, paguthiId);
        getCategoryList(String.valueOf(listcount));
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
            birthdayList = gson.fromJson(response.toString(), BirthdayList.class);
            birthdayArrayList.addAll(birthdayList.getBirthdayArrayList());
            ReportBirthdayListAdapter mAdapter = new ReportBirthdayListAdapter(birthdayArrayList, ReportBirthdayActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            recyclerView.scrollToPosition(listcount);
            swipeRefreshLayout.setRefreshing(false);
            listLay.setVisibility(View.VISIBLE);

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
}