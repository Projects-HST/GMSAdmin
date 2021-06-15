package com.gms.admin.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import com.gms.admin.R;
import com.gms.admin.bean.support.SpinnerData;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.GMSValidator;
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

public class ReportSubCategoryActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = ReportStatusActivity.class.getName();
    private String checkRes = "", paguthiId = "0";
    private SearchView searchView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ArrayList<SpinnerData> spinnerData;
    private ArrayAdapter<SpinnerData> spinnerDataArrayAdapter = null;
    private TextView dateFrom, dateTo, subcategory;
    private LinearLayout selectSubCategory;
    private SimpleDateFormat mDateFormatter;
    private DatePickerDialog mDatePicker;
    boolean fr = false, t = false;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sub_category);

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

        dateFrom = findViewById(R.id.from_date);
        dateTo = findViewById(R.id.to_date);
        subcategory = findViewById(R.id.report_sub_category);
        selectSubCategory = findViewById(R.id.status_sub_category);

        search = findViewById(R.id.search);

        dateFrom.setOnClickListener(this);
        dateTo.setOnClickListener(this);
        selectSubCategory.setOnClickListener(this);
        search.setOnClickListener(this);

        mDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        getCategory();

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
        if (v == selectSubCategory) {
            showSpinnerData();
        }
        if (v == search) {
            if (validateFields()) {
                sendSearch();
            }
        }
    }

    private void showSpinnerData() {
        Log.d(TAG, "Show Spinner Data");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.spinner_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.spinner_header);
        header.setText(getString(R.string.select_sub_category));
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(spinnerDataArrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpinnerData spinnerDatas = spinnerData.get(which);
                        subcategory.setText(spinnerDatas.getName());
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

    private boolean validateFields() {

        if (dateFrom.getText().toString().equalsIgnoreCase("From Date")) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Select from date");
            return false;
        }
        if (dateTo.getText().toString().equalsIgnoreCase("To Date")) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Select to date");
            return false;
        } if (paguthiId.equalsIgnoreCase("0")) {
            AlertDialogHelper.showSimpleAlertDialog(this, "Select category");
            return false;
        }if (!checkTime()) {
            AlertDialogHelper.showSimpleAlertDialog(this, "End date cannot be before start date");
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

    private void getCategory() {
        checkRes = "sub_category";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_USER_ID, PreferenceStorage.getUserId(this));
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_SUB_CATEGORY_LIST;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void sendSearch() {
        PreferenceStorage.saveFromDate(this, getserverdateformat(dateFrom.getText().toString()));
        PreferenceStorage.saveToDate(this, getserverdateformat(dateTo.getText().toString()));
        PreferenceStorage.saveReportSubCategory(this, paguthiId);
        Intent intt = new Intent(this, ReportGrievanceListActivity.class);
        intt.putExtra("page", "sub_category");
        startActivity(intt);
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
                mDatePicker = new DatePickerDialog(this,this, year, month, day);
                mDatePicker.show();
            }
        } else {
            Log.d(TAG, "show default date");

            mDatePicker = new DatePickerDialog(this,this, year, month, day);
            mDatePicker.show();
        }
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
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);
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
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
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

                if (checkRes.equalsIgnoreCase("sub_category")) {
                    JSONArray getData = response.getJSONArray("sub_category_details");
                    int getLength = getData.length();
                    String id = "";
                    String name = "";
                    spinnerData = new ArrayList<>();
                    spinnerData.add(new SpinnerData("ALL", "All"));

                    for (int i = 0; i < getLength; i++) {
                        id = getData.getJSONObject(i).getString("id");
                        name = capitalizeString(getData.getJSONObject(i).getString("sub_category_name"));
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
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}