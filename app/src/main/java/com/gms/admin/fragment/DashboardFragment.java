package com.gms.admin.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.gms.admin.R;
import com.gms.admin.activity.GraphActivity;
import com.gms.admin.activity.SearchResultActivity;
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

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = DashboardFragment.class.getName();
    private View rootView;
    private SearchView searchView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private int ab = 0;
    private String checkRes = "", paguthiId = "ALL";
    private ArrayList<SpinnerData> spinnerData;
    private ArrayAdapter<SpinnerData> spinnerDataArrayAdapter = null;
    private RelativeLayout selectArea, consituentPopup, meetingPopup, grievancePopup, interactionPopup;
    private LinearLayout constituencyLayout, meetingLayout, grievanceLayout, interactionLayout;
    private ImageView closeConstitutePop, closeGrievancePop, closeMeetingPop, closeInteractionPop;
    private TextView area, constituentCount, meetingCount, grievanceCount, questionCount, viewMore;
    private TextView constiPopupCount, maleCount, femaleCount, voterCount, aadhaarcount;
    private TextView grievPopupCount, enquiryCount, petitionCount, processinggriveCount, completedgrivecount;
    private TextView meetPopupCount, requestedmeetingCount, completedmeetingCount;
    private TextView interPopupCount, knowCount, interCount, nrcCount, citezeincount, crossCount;
    BarChart barChart;
    float groupSpace = 0.31f;
    float barSpace = 0.03f; // x3 DataSet
    float barWidth = 0.2f; // x3 DataSet
    // (0.2 + 0.03) * 3 + 0.31 = 1.00 -> interval per "group"

    int groupCount = 12;

    public static DashboardFragment newInstance(int position) {
        DashboardFragment frag = new DashboardFragment();
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

        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        selectArea = rootView.findViewById(R.id.select_area);

        constituencyLayout = rootView.findViewById(R.id.constituency_layout);
        meetingLayout = rootView.findViewById(R.id.meeting_layout);
        grievanceLayout = rootView.findViewById(R.id.grievance_layout);
        interactionLayout = rootView.findViewById(R.id.interaction_layout);

        selectArea.setOnClickListener(this);
        constituencyLayout.setOnClickListener(this);
        meetingLayout.setOnClickListener(this);
        grievanceLayout.setOnClickListener(this);
        interactionLayout.setOnClickListener(this);


        consituentPopup = rootView.findViewById(R.id.constituent_count_popup);
        meetingPopup = rootView.findViewById(R.id.meeting_count_popup);
        grievancePopup = rootView.findViewById(R.id.grievance_count_popup);
        interactionPopup = rootView.findViewById(R.id.interaction_count_popup);

        closeConstitutePop = rootView.findViewById(R.id.close_constituent);
        closeMeetingPop = rootView.findViewById(R.id.close_meeting);
        closeGrievancePop = rootView.findViewById(R.id.close_grievance);
        closeInteractionPop = rootView.findViewById(R.id.close_inteaction);

        closeConstitutePop.setOnClickListener(this);
        closeMeetingPop.setOnClickListener(this);
        closeGrievancePop.setOnClickListener(this);
        closeInteractionPop.setOnClickListener(this);

        constiPopupCount = rootView.findViewById(R.id.constituent_popup_count);
        grievPopupCount = rootView.findViewById(R.id.grievance_popup_count);
        meetPopupCount = rootView.findViewById(R.id.meeting_count);
        interPopupCount = rootView.findViewById(R.id.inteaction_count);

        maleCount = rootView.findViewById(R.id.male_count);
        femaleCount = rootView.findViewById(R.id.female_count);
        voterCount = rootView.findViewById(R.id.voter_count);
        aadhaarcount = rootView.findViewById(R.id.aadhaar_count);

        enquiryCount = rootView.findViewById(R.id.enquiry_count);
        petitionCount = rootView.findViewById(R.id.petition_count);
        processinggriveCount = rootView.findViewById(R.id.processing_grievance_count);
        completedgrivecount = rootView.findViewById(R.id.completed_grievance_count);

        requestedmeetingCount = rootView.findViewById(R.id.requested_count);
        completedmeetingCount = rootView.findViewById(R.id.completed_count);

        knowCount = rootView.findViewById(R.id.known_count);
        interCount = rootView.findViewById(R.id.gms_interaction_count);
        nrcCount = rootView.findViewById(R.id.nrc_count);
        citezeincount = rootView.findViewById(R.id.citizen_count);
        crossCount = rootView.findViewById(R.id.cross_count);


        area = rootView.findViewById(R.id.text_area);
        constituentCount = rootView.findViewById(R.id.constituent_count);
        meetingCount = rootView.findViewById(R.id.meetings_count);
        grievanceCount = rootView.findViewById(R.id.grievances_count);
        questionCount = rootView.findViewById(R.id.question_count);
        viewMore = rootView.findViewById(R.id.view_more);
        viewMore.setOnClickListener(this);

        barChart = rootView.findViewById(R.id.BarChart);

        searchView = rootView.findViewById(R.id.search_cat_list);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setQueryHint("Search for constituent");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

//                if (categoryArrayList.contains(query)) {
//                    preferenceAdatper.getFilter().filter(query);
//                } else {
//                    Toast.makeText(getActivity(), "No Match found", Toast.LENGTH_LONG).show();
//                }
                if (query != null) {
                    makeSearch(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });


        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());

        getPaguthi();

        return rootView;
    }

    private void makeSearch(String eventname) {
        PreferenceStorage.setSearchFor(getActivity(), eventname);
        startActivity(new Intent(getActivity(), SearchResultActivity.class));
    }

    private void getPaguthi() {
        checkRes = "paguthi";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_CONSTITUENCY_ID, PreferenceStorage.getConstituencyID(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_PAGUTHI;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getDashboard() {
        checkRes = "dashboard";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_DASHBOARD_DETAIL;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getConstiWidgetData() {
        checkRes = "widget_consti";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_WIDGET_CONSTITUTENT;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getGrievWidgetData() {
        checkRes = "widget_grie";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_WIDGET_GRIEVANCE;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }
    private void getMeetWidgetData() {
        checkRes = "widget_meet";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_WIDGET_MEETING;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getInterWidgetData() {
        checkRes = "widget_inter";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_WIDGET_INTERACTION;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {
        if (v == selectArea) {
            showSpinnerData();
        }
        if (v == constituencyLayout) {
            getConstiWidgetData();
        }
        if (v == meetingLayout) {
            getMeetWidgetData();
        }
        if (v == grievanceLayout) {
            getGrievWidgetData();
        }
        if (v == interactionLayout) {
            getInterWidgetData();
        }
        if (v == viewMore) {
            Intent i = new Intent(getActivity(), GraphActivity.class);
            i.putExtra("paguthi", paguthiId);
            startActivity(i);
        }
        if (v == closeConstitutePop) {
            consituentPopup.setVisibility(View.GONE);
            constituencyLayout.setClickable(true);
            meetingLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            interactionLayout.setClickable(true);
        }
        if (v == closeGrievancePop) {
            grievancePopup.setVisibility(View.GONE);
            constituencyLayout.setClickable(true);
            meetingLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            interactionLayout.setClickable(true);
        }
        if (v == closeMeetingPop) {
            meetingPopup.setVisibility(View.GONE);
            constituencyLayout.setClickable(true);
            meetingLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            interactionLayout.setClickable(true);
        }
        if (v == closeInteractionPop) {
            interactionPopup.setVisibility(View.GONE);
            constituencyLayout.setClickable(true);
            meetingLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            interactionLayout.setClickable(true);
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
                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), msg);
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

                if (checkRes.equalsIgnoreCase("paguthi")) {
                    JSONArray getData = response.getJSONArray("paguthi_details");
                    int getLength = getData.length();
                    String id = "";
                    String name = "";
                    spinnerData = new ArrayList<>();
                    spinnerData.add(new SpinnerData("ALL", "All"));

                    for (int i = 0; i < getLength; i++) {
                        id = getData.getJSONObject(i).getString("id");
                        name = capitalizeString(getData.getJSONObject(i).getString("paguthi_name"));
                        spinnerData.add(new SpinnerData(id, name));
                    }

                    //fill data in spinner
                    spinnerDataArrayAdapter = new ArrayAdapter<SpinnerData>(getActivity(), R.layout.spinner_data_layout, R.id.data_name, spinnerData) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
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
                    getDashboard();
                } else if (checkRes.equalsIgnoreCase("dashboard")) {
                    JSONObject getData = response.getJSONObject("widgets_count");
                    constituentCount.setText(getData.getString("constituent_count"));
                    meetingCount.setText(getData.getString("meeting_count"));
                    grievanceCount.setText(getData.getString("grievance_count"));
                    questionCount.setText(getData.getString("interaction_count"));

                    JSONArray data = response.getJSONArray("footfall_graph");

                    ArrayList<BarEntry> totalGrievance = new ArrayList();
                    ArrayList<BarEntry> newGrievance = new ArrayList();
                    ArrayList<BarEntry> repGrievance = new ArrayList();
                    ArrayList year = new ArrayList();
                    ArrayList dataSets = new ArrayList();

                    for (int i = 0; i < data.length(); i++) {
                        if (i!=4) {
//                        float[] totalG = {Float.parseFloat(data.getJSONObject(i).getString("total")),Float.parseFloat(data.getJSONObject(i).getString("new_grev")),Float.parseFloat(data.getJSONObject(i).getString("repeated_grev"))};
                            float totalG = Float.parseFloat(data.getJSONObject(i).getString("total"));
                            float newG = Float.parseFloat(data.getJSONObject(i).getString("new_grev"));
                            float ff2 = Float.parseFloat(data.getJSONObject(i).getString("repeated_grev"));
                            totalGrievance.add(new BarEntry((float) i, totalG));
                            newGrievance.add(new BarEntry((float) i, newG));
                            repGrievance.add(new BarEntry((float) i, ff2));
                        }
                    }
                    year.add("Jan 2020");
                    year.add("Feb 2020");
                    year.add("Mar 2020");
                    year.add("May 2020");
                    year.add("June 2020");
                    year.add("July 2020");
                    year.add("Aug 2020");
                    year.add("Sept 2020");
                    year.add("Oct 2020");
                    year.add("Nov 2020");
                    year.add("Dec 2020");

                    BarDataSet bardataset = new BarDataSet(totalGrievance, "Total grievance");
                    bardataset.setColor(ContextCompat.getColor(getActivity(), R.color.chart_one));
                    BarDataSet bardataset1 = new BarDataSet(newGrievance, "New grievance");
                    bardataset1.setColor(ContextCompat.getColor(getActivity(), R.color.chart_two));
                    BarDataSet bardataset2 = new BarDataSet(repGrievance, "Repeated grievance");
                    bardataset2.setColor(ContextCompat.getColor(getActivity(), R.color.chart_three));
                    barChart.animateY(1000);


                    BarData barData = new BarData(bardataset, bardataset1, bardataset2);
                    barData.setValueFormatter(new LargeValueFormatter());
                    barChart.setData(barData);
                    barChart.getDescription().setText("");
                    barChart.getBarData().setBarWidth(barWidth);

                    // restrict the x-axis range
                    barChart.getXAxis().setAxisMinimum(-(0.5f));
                    barChart.getXAxis().setAxisMaximum(12);

                    Legend l = barChart.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setDrawInside(true);
                    l.setYOffset(0f);
                    l.setXOffset(10f);
                    l.setYEntrySpace(0f);
                    l.setTextSize(8f);

                    YAxis leftAxis = barChart.getAxisLeft();
//                    leftAxis.setValueFormatter(new LargeValueFormatter());
                    leftAxis.setDrawGridLines(false);
                    leftAxis.setSpaceTop(35f);
                    leftAxis.setAxisMinimum(0f);
//                    leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                    barChart.getAxisRight().setEnabled(false);

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setGranularity(1f); // only intervals of 1 day
                    xAxis.setLabelCount(12);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(year));

//                    barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
                    barChart.groupBars(-0, groupSpace, barSpace);
                    barChart.setVisibleXRangeMaximum(3);
                    barChart.invalidate();
                } else if (checkRes.equalsIgnoreCase("widget_consti")) {
                    constiPopupCount.setText(getString(R.string.constituent_members)+" - "+response.getJSONObject("constituent_details").getString("member_count"));
                    maleCount.setText(response.getJSONObject("constituent_details").getString("male_count"));
                    femaleCount.setText(response.getJSONObject("constituent_details").getString("female_count"));
                    voterCount.setText(response.getJSONObject("constituent_details").getString("voterid_count"));
                    aadhaarcount.setText(response.getJSONObject("constituent_details").getString("aadhaar_count"));
                    consituentPopup.setVisibility(View.VISIBLE);
                    constituencyLayout.setClickable(false);
                    meetingLayout.setClickable(false);
                    grievanceLayout.setClickable(false);
                    interactionLayout.setClickable(false);
                } else if (checkRes.equalsIgnoreCase("widget_grie")) {
                    grievPopupCount.setText(getString(R.string.total_grievances)+" - "+response.getJSONObject("grievances_details").getString("grievance_count"));
                    enquiryCount.setText(response.getJSONObject("grievances_details").getString("enquiry_count"));
                    petitionCount.setText(response.getJSONObject("grievances_details").getString("petition_count"));
                    processinggriveCount.setText(response.getJSONObject("grievances_details").getString("processing_count"));
                    completedgrivecount.setText(response.getJSONObject("grievances_details").getString("completed_count"));
                    grievancePopup.setVisibility(View.VISIBLE);
                    constituencyLayout.setClickable(false);
                    meetingLayout.setClickable(false);
                    grievanceLayout.setClickable(false);
                    interactionLayout.setClickable(false);
                } else if (checkRes.equalsIgnoreCase("widget_meet")) {
                    meetPopupCount.setText(getString(R.string.total_meetings)+" - "+response.getJSONObject("meeting_details").getString("meeting_count"));
                    requestedmeetingCount.setText(response.getJSONObject("meeting_details").getString("requested_count"));
                    completedmeetingCount.setText(response.getJSONObject("meeting_details").getString("completed_count"));
                    meetingPopup.setVisibility(View.VISIBLE);
                    constituencyLayout.setClickable(false);
                    meetingLayout.setClickable(false);
                    grievanceLayout.setClickable(false);
                    interactionLayout.setClickable(false);
                } else if (checkRes.equalsIgnoreCase("widget_inter")) {
                    interPopupCount.setText(getString(R.string.constituent_interaction)+" - "+response.getString("interaction_count"));
                    knowCount.setText(response.getJSONArray("interaction_details").getJSONObject(0).getString("tot_values"));
                    interCount.setText(response.getJSONArray("interaction_details").getJSONObject(1).getString("tot_values"));
                    nrcCount.setText(response.getJSONArray("interaction_details").getJSONObject(2).getString("tot_values"));
                    citezeincount.setText(response.getJSONArray("interaction_details").getJSONObject(3).getString("tot_values"));
                    crossCount.setText(response.getJSONArray("interaction_details").getJSONObject(4).getString("tot_values"));
                    interactionPopup.setVisibility(View.VISIBLE);
                    constituencyLayout.setClickable(false);
                    meetingLayout.setClickable(false);
                    grievanceLayout.setClickable(false);
                    interactionLayout.setClickable(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showSpinnerData() {
        Log.d(TAG, "Show timing lists");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.spinner_header_layout, null);
        TextView header = (TextView) view.findViewById(R.id.spinner_header);
        header.setText(getString(R.string.select_area));
        builderSingle.setCustomTitle(view);

        builderSingle.setAdapter(spinnerDataArrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpinnerData spinnerDatas = spinnerData.get(which);
                        area.setText(spinnerDatas.getName());
                        paguthiId = spinnerDatas.getId();
                        getDashboard();
                    }
                });
        builderSingle.show();
    }

    @Override
    public void onError(String error) {

    }
}