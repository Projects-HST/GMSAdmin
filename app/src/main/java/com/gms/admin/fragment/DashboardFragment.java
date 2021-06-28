package com.gms.admin.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.gms.admin.R;
import com.gms.admin.activity.FootfallActivity;
import com.gms.admin.activity.GrievanceActivity;
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DashboardFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = DashboardFragment.class.getName();
    private View rootView;
    private SearchView searchView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private int ab = 0;
    private String checkRes = "", paguthiId = "ALL", officeId = "ALL";
    String from_date, to_date;
    boolean fr = false, to = false;
    private DatePickerDialog mDatePicker;
    private SimpleDateFormat mDateFormatter;
    private ArrayList<SpinnerData> spinnerData;
    private ArrayAdapter<SpinnerData> spinnerDataArrayAdapter ;
    private ArrayList<SpinnerData> officespinnerData;
    private ArrayAdapter<SpinnerData> officespinnerDataArrayAdapter;
    private ScrollView scrollView;
    private RelativeLayout selectArea, selectOffice, constituencyLayout, consituentPopup, grievancePopup, footfallPopup, meetingPopup, volunteerPopup, greetingsPopup, videoPopup;
    private LinearLayout dateLayout, grievanceLayout, footfallLayout, meetingLayout, volunteerLayout, greetingsLayout, videoLayout;
    private ImageView closeConstitutePop, closeGrievancePop, closeFootfallPop, closeMeetingPop, closeVolunteerPop, closeGreetingsPop, closeVideoPop;
    private TextView fromDate, toDate;
    private TextView area, office, constituentCount, grievanceCount, footfallCount, meetingCount, volunteerCount, greetingsCount, videoCount, viewMore;
    private TextView constiPopupCount, perMale, maleCount, perFemale, femaleCount, perOthers, othersCount, perPhone, phoneCount,
            perWhatsApp, whatsAppCount, perBroadcast, broadcastCount, perMail, mailCount, perVoter, voterCount, perDob, dobCount;
    private TextView grievPopupCount, totGrievances, enquiryCount, petitionCount, onlinePetition, civicPetition, onlineEnq, civicEnq, grievMore;
    private TextView footPopupCount, uinqueCount, totFoot, paguthiCount, otherPaguthi, footMore;
    private TextView meetPopupCount, perRequest, requestedmeetingCount, perComplete, completedmeetingCount;
    private TextView volPopupCount, perVol, volCount, perNonVol, nonVolCount;
    private TextView greetPopupCount, perBirth, birthCount, perFest, festCount, fest1, perFest1, festCount1, fest2, perFest2, festCount2, fest3, perFest3, festCount3;
    private TextView vidPopupCount, off1, perVid1, vidCount1, off2, perVid2, vidCount2, off3, perVid3, vidCount3, off4, perVid4, vidCount4, off5, perVid5, vidCount5,
            off6, perVid6, vidCount6, off7, perVid7, vidCount7, off8, perVid8, vidCount8, off9, perVid9, vidCount9;
    BarChart barChart;
    LineChart lineChart;
    float groupSpace = 0.31f;
    float barSpace = 0.03f; // x3 DataSet
    float barWidth = 0.2f; // x3 DataSet
    // (0.2 + 0.03) * 3 + 0.31 = 1.00 -> interval per "group"

    int groupCount = 12;
    private TextView showdata, findData, clearData;
    private RelativeLayout dataSelectionLayout;
    private boolean submenuVisible = false;

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

        fromDate = rootView.findViewById(R.id.from_date);
        toDate = rootView.findViewById(R.id.to_date);

        if (fromDate.getText().toString() != null) {
            from_date = PreferenceStorage.getFromDate(getActivity());
        }
        if (toDate.getText().toString() != null) {
            to_date = PreferenceStorage.getToDate(getActivity());
        }

        dataSelectionLayout = rootView.findViewById(R.id.data_layout);
        selectArea = rootView.findViewById(R.id.select_area);
        selectOffice = rootView.findViewById(R.id.select_office);
        showdata = rootView.findViewById(R.id.show_data);
        findData = rootView.findViewById(R.id.find_data);
        clearData = rootView.findViewById(R.id.clear_data);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(10);
        drawable.setColor(Color.parseColor(PreferenceStorage.getAppBaseColor(getActivity())));

        findData.setBackground(drawable);

        scrollView = rootView.findViewById(R.id.scrolView);
        constituencyLayout = rootView.findViewById(R.id.constituents);
        grievanceLayout = rootView.findViewById(R.id.grievance_layout);
        footfallLayout = rootView.findViewById(R.id.footfall_layout);
        meetingLayout = rootView.findViewById(R.id.meeting_layout);
        volunteerLayout = rootView.findViewById(R.id.volunteer_layout);
        greetingsLayout = rootView.findViewById(R.id.greeting_layout);
        videoLayout = rootView.findViewById(R.id.video_layout);

        fromDate.setOnClickListener(this);
        toDate.setOnClickListener(this);
        selectArea.setOnClickListener(this);
        selectOffice.setOnClickListener(this);
        showdata.setOnClickListener(this);
        findData.setOnClickListener(this);
        clearData.setOnClickListener(this);

        constituencyLayout.setOnClickListener(this);
        grievanceLayout.setOnClickListener(this);
        footfallLayout.setOnClickListener(this);
        meetingLayout.setOnClickListener(this);
        volunteerLayout.setOnClickListener(this);
        greetingsLayout.setOnClickListener(this);
        videoLayout.setOnClickListener(this);


        consituentPopup = rootView.findViewById(R.id.constituent_count_popup);
        grievancePopup = rootView.findViewById(R.id.grievance_count_popup);
        footfallPopup = rootView.findViewById(R.id.footfall_count_popup);
        meetingPopup = rootView.findViewById(R.id.meeting_count_popup);
        volunteerPopup = rootView.findViewById(R.id.volunteer_count_popup);
        greetingsPopup = rootView.findViewById(R.id.greetings_count_popup);
        videoPopup = rootView.findViewById(R.id.video_count_popup);

        closeConstitutePop = rootView.findViewById(R.id.close_constituent);
        closeGrievancePop = rootView.findViewById(R.id.close_grievance);
        closeFootfallPop = rootView.findViewById(R.id.close_footfall);
        closeMeetingPop = rootView.findViewById(R.id.close_meeting);
        closeVolunteerPop = rootView.findViewById(R.id.close_volunteer);
        closeGreetingsPop = rootView.findViewById(R.id.close_greeting);
        closeVideoPop = rootView.findViewById(R.id.close_video);

        closeConstitutePop.setOnClickListener(this);
        closeGrievancePop.setOnClickListener(this);
        closeFootfallPop.setOnClickListener(this);
        closeMeetingPop.setOnClickListener(this);
        closeVolunteerPop.setOnClickListener(this);
        closeGreetingsPop.setOnClickListener(this);
        closeVideoPop.setOnClickListener(this);

        constiPopupCount = rootView.findViewById(R.id.constituent_count);
        grievPopupCount = rootView.findViewById(R.id.grievance_count);
        footPopupCount = rootView.findViewById(R.id.footfall_count);
        meetPopupCount = rootView.findViewById(R.id.meetings_count);
        volPopupCount = rootView.findViewById(R.id.volunteer_count);
        greetPopupCount = rootView.findViewById(R.id.greetings_count);
        vidPopupCount = rootView.findViewById(R.id.video_count);

        perMale = rootView.findViewById(R.id.per_male);
        maleCount = rootView.findViewById(R.id.male_count);
        perFemale = rootView.findViewById(R.id.per_female);
        femaleCount = rootView.findViewById(R.id.female_count);
        perOthers = rootView.findViewById(R.id.per_trans);
        othersCount = rootView.findViewById(R.id.trans_count);
        perPhone = rootView.findViewById(R.id.per_phone);
        phoneCount = rootView.findViewById(R.id.phone_count);
        perWhatsApp = rootView.findViewById(R.id.per_whatsapp);
        whatsAppCount = rootView.findViewById(R.id.whatsapp_count);
        perBroadcast = rootView.findViewById(R.id.per_wh_bCast);
        broadcastCount = rootView.findViewById(R.id.wh_bCast_count);
        perMail = rootView.findViewById(R.id.per_mailId);
        mailCount = rootView.findViewById(R.id.mailId_count);
        perVoter = rootView.findViewById(R.id.per_voter);
        voterCount = rootView.findViewById(R.id.voter_count);
        perDob = rootView.findViewById(R.id.per_dob_updated);
        dobCount = rootView.findViewById(R.id.dob_count);

        totGrievances = rootView.findViewById(R.id.tot_grievance);
        petitionCount = rootView.findViewById(R.id.petition_count);
        onlinePetition = rootView.findViewById(R.id.online_count_1);
        civicPetition = rootView.findViewById(R.id.civic_count_1);
        enquiryCount = rootView.findViewById(R.id.enquiry_count);
        onlineEnq = rootView.findViewById(R.id.online_count_2);
        civicEnq = rootView.findViewById(R.id.civic_count_2);
        grievMore = rootView.findViewById(R.id.griev_more);
        grievMore.setOnClickListener(this);

        uinqueCount = rootView.findViewById(R.id.unique_count);
        totFoot = rootView.findViewById(R.id.tot_footFall);
        paguthiCount = rootView.findViewById(R.id.paguthi_count);
        otherPaguthi = rootView.findViewById(R.id.other_count);
        footMore = rootView.findViewById(R.id.foot_more);
        footMore.setOnClickListener(this);

        perRequest = rootView.findViewById(R.id.per_request);
        requestedmeetingCount = rootView.findViewById(R.id.request_count);
        perComplete = rootView.findViewById(R.id.per_complete);
        completedmeetingCount = rootView.findViewById(R.id.complete_count);

        perVol = rootView.findViewById(R.id.per_constituency);
        volCount = rootView.findViewById(R.id.constituency_count);
        perNonVol = rootView.findViewById(R.id.per_non);
        nonVolCount = rootView.findViewById(R.id.nonConstituency_count);

        perBirth = rootView.findViewById(R.id.per_bday);
        birthCount = rootView.findViewById(R.id.birthday_count);
        perFest = rootView.findViewById(R.id.per_fest);
        festCount = rootView.findViewById(R.id.festival_count);
        fest1 = rootView.findViewById(R.id.fest_1);
        perFest1 = rootView.findViewById(R.id.per_fest_1);
        festCount1 = rootView.findViewById(R.id.fest_count_1);
        fest2 = rootView.findViewById(R.id.fest_2);
        perFest2 = rootView.findViewById(R.id.per_fest_2);
        festCount2 = rootView.findViewById(R.id.fest_count_2);
        fest3 = rootView.findViewById(R.id.fest_3);
        perFest3 = rootView.findViewById(R.id.per_fest_3);
        festCount3 = rootView.findViewById(R.id.fest_count_3);
        off4 = rootView.findViewById(R.id.office_4);
        perVid4 = rootView.findViewById(R.id.per_off_4);
        vidCount4 = rootView.findViewById(R.id.office_count_4);
        off2 = rootView.findViewById(R.id.office_2);
        perVid2 = rootView.findViewById(R.id.per_off_2);
        vidCount2 = rootView.findViewById(R.id.office_count_2);
        off3 = rootView.findViewById(R.id.office_3);
        perVid3 = rootView.findViewById(R.id.per_off_3);
        vidCount3 = rootView.findViewById(R.id.office_count_3);
        off1 = rootView.findViewById(R.id.office_1);
        perVid1 = rootView.findViewById(R.id.per_off_1);
        vidCount1 = rootView.findViewById(R.id.office_count_1);
        off5 = rootView.findViewById(R.id.office_5);
        perVid5 = rootView.findViewById(R.id.per_off_5);
        vidCount5 = rootView.findViewById(R.id.office_count_5);
        off6 = rootView.findViewById(R.id.office_6);
        perVid6 = rootView.findViewById(R.id.per_off_6);
        vidCount6 = rootView.findViewById(R.id.office_count_6);
        off7 = rootView.findViewById(R.id.office_7);
        perVid7 = rootView.findViewById(R.id.per_off_7);
        vidCount7 = rootView.findViewById(R.id.office_count_7);
        off8 = rootView.findViewById(R.id.office_8);
        perVid8 = rootView.findViewById(R.id.per_off_8);
        vidCount8 = rootView.findViewById(R.id.office_count_8);
        off9 = rootView.findViewById(R.id.office_9);
        perVid9 = rootView.findViewById(R.id.per_off_9);
        vidCount9 = rootView.findViewById(R.id.office_count_9);

        area = rootView.findViewById(R.id.text_area);
        office = rootView.findViewById(R.id.text_office);
        constituentCount = rootView.findViewById(R.id.overall_constituent_count);
        grievanceCount = rootView.findViewById(R.id.overall_grievance_count);
        footfallCount = rootView.findViewById(R.id.overall_footfall_count);
        meetingCount = rootView.findViewById(R.id.overall_meeting_count);
        volunteerCount = rootView.findViewById(R.id.overall_volunteer_count);
        greetingsCount = rootView.findViewById(R.id.overall_greeting_count);
        videoCount = rootView.findViewById(R.id.overall_video_count);
//        viewMore = rootView.findViewById(R.id.view_more);
//        viewMore.setOnClickListener(this);

        lineChart = rootView.findViewById(R.id.lineChart);

        mDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return false;
//            }
//        });

//        searchView = rootView.findViewById(R.id.search_cat_list);
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.setIconified(false);
//            }
//        });
//        searchView.setQueryHint("Search for constituent");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
////                if (categoryArrayList.contains(query)) {
////                    preferenceAdatper.getFilter().filter(query);
////                } else {
////                    Toast.makeText(getActivity(), "No Match found", Toast.LENGTH_LONG).show();
////                }
//                if (query != null) {
//                    makeSearch(query);
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //    adapter.getFilter().filter(newText);
//                return false;
//            }
//        });

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
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_PAGUTHI;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getOffice() {
        checkRes = "office";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_CONSTITUENCY_ID, PreferenceStorage.getConstituencyID(getActivity()));
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_OFFICE;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getDashboard() {
        checkRes = "dashboard";
        from_date = fromDate.getText().toString().trim();
        PreferenceStorage.saveFromDate(getActivity(), from_date);
        to_date = toDate.getText().toString().trim();
        PreferenceStorage.saveToDate(getActivity(), to_date);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);
            jsonObject.put(GMSConstants.OFFICE_ID, officeId);
            jsonObject.put(GMSConstants.KEY_FROM_DATE, from_date);
            jsonObject.put(GMSConstants.KEY_TO_DATE, to_date);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_DASHBOARD_DETAIL;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getConstiWidgetData() {
        checkRes = "widget_consti";
        from_date = PreferenceStorage.getFromDate(getActivity());
        to_date = PreferenceStorage.getToDate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);
            jsonObject.put(GMSConstants.OFFICE_ID, officeId);
            jsonObject.put(GMSConstants.KEY_FROM_DATE, from_date);
            jsonObject.put(GMSConstants.KEY_TO_DATE, to_date);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_WIDGET_CONSTITUTENT;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getGrievWidgetData() {
        checkRes = "widget_grie";
        from_date = PreferenceStorage.getFromDate(getActivity());
        to_date = PreferenceStorage.getToDate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);
            jsonObject.put(GMSConstants.OFFICE_ID, officeId);
            jsonObject.put(GMSConstants.KEY_FROM_DATE, from_date);
            jsonObject.put(GMSConstants.KEY_TO_DATE, to_date);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_WIDGET_GRIEVANCE;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getFootfallWidgetData() {
        checkRes = "widget_foot";
        from_date = PreferenceStorage.getFromDate(getActivity());
        to_date = PreferenceStorage.getToDate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);
            jsonObject.put(GMSConstants.OFFICE_ID, officeId);
            jsonObject.put(GMSConstants.KEY_FROM_DATE, from_date);
            jsonObject.put(GMSConstants.KEY_TO_DATE, to_date);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_WIDGET_FOOTFALL;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getMeetWidgetData() {
        checkRes = "widget_meet";
        from_date = PreferenceStorage.getFromDate(getActivity());
        to_date = PreferenceStorage.getToDate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);
            jsonObject.put(GMSConstants.OFFICE_ID, officeId);
            jsonObject.put(GMSConstants.KEY_FROM_DATE, from_date);
            jsonObject.put(GMSConstants.KEY_TO_DATE, to_date);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_WIDGET_MEETING;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getVolunteerWidgetData() {
        checkRes = "widget_vol";
        from_date = PreferenceStorage.getFromDate(getActivity());
        to_date = PreferenceStorage.getToDate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);
            jsonObject.put(GMSConstants.OFFICE_ID, officeId);
            jsonObject.put(GMSConstants.KEY_FROM_DATE, from_date);
            jsonObject.put(GMSConstants.KEY_TO_DATE, to_date);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_WIDGET_VOLUNTEER;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getGreetWidgetData() {
        checkRes = "widget_greet";
        from_date = PreferenceStorage.getFromDate(getActivity());
        to_date = PreferenceStorage.getToDate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);
            jsonObject.put(GMSConstants.OFFICE_ID, officeId);
            jsonObject.put(GMSConstants.KEY_FROM_DATE, from_date);
            jsonObject.put(GMSConstants.KEY_TO_DATE, to_date);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_WIDGET_GREETINGS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void getVideoWidgetData() {
        checkRes = "widget_video";
        from_date = PreferenceStorage.getFromDate(getActivity());
        to_date = PreferenceStorage.getToDate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);
            jsonObject.put(GMSConstants.OFFICE_ID, officeId);
            jsonObject.put(GMSConstants.KEY_FROM_DATE, from_date);
            jsonObject.put(GMSConstants.KEY_TO_DATE, to_date);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_WIDGET_VIDEO;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {
        if (v == fromDate) {
            fr = true;
            to = false;
            showDatePicker();
            from_date = fromDate.getText().toString();
            PreferenceStorage.saveFromDate(getActivity(), from_date);
        }
        if (v == toDate) {
            fr = false;
            to = true;
            showDatePicker();
            to_date = toDate.getText().toString();
            PreferenceStorage.saveToDate(getActivity(), to_date);
        }
        if (v == showdata) {
            showSelectorData();
        }
        if (v == findData) {
            getDashboard();
        }
        if (v == clearData) {
            fromDate.setText("");
            fromDate.setHint(R.string.from_date);
            from_date = "";
            toDate.setText("");
            toDate.setHint(R.string.to_date);
            to_date = "";
            paguthiId = "ALL";
            officeId = "ALL";
            area.setText("ALL");
            office.setText("ALL");
        }
        if (v == selectArea) {
            showSpinnerData();
        }
        if (v == selectOffice) {
            showOfficeSpinnerData();
        }
        if (v == constituencyLayout) {
            getConstiWidgetData();
        }
        if (v == grievanceLayout) {
            getGrievWidgetData();
        }
        if (v == grievMore) {
            grievancePopup.setVisibility(View.GONE);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            enableSearchView(searchView, true);
            selectArea.setClickable(true);
            selectOffice.setClickable(true);
            fromDate.setClickable(true);
            fromDate.setFocusable(true);
            toDate.setClickable(true);
            constituencyLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            footfallLayout.setClickable(true);
            meetingLayout.setClickable(true);
            volunteerLayout.setClickable(true);
            greetingsLayout.setClickable(true);
            videoLayout.setClickable(true);
            Intent griIntent = new Intent(getActivity(), GrievanceActivity.class);
            griIntent.putExtra("paguthi", paguthiId);
            griIntent.putExtra("office", officeId);
            startActivity(griIntent);
        }
        if (v == footfallLayout) {
            getFootfallWidgetData();
        }
        if (v == footMore) {
            footfallPopup.setVisibility(View.GONE);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            enableSearchView(searchView, true);
            selectArea.setClickable(true);
            selectOffice.setClickable(true);
            fromDate.setClickable(true);
            fromDate.setFocusable(true);
            toDate.setClickable(true);
            constituencyLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            footfallLayout.setClickable(true);
            meetingLayout.setClickable(true);
            volunteerLayout.setClickable(true);
            greetingsLayout.setClickable(true);
            videoLayout.setClickable(true);
            Intent footIntent = new Intent(getActivity(), FootfallActivity.class);
            footIntent.putExtra("paguthi", paguthiId);
            startActivity(footIntent);
        }
        if (v == meetingLayout) {
            getMeetWidgetData();
        }
        if (v == volunteerLayout) {
            getVolunteerWidgetData();
        }
        if (v == greetingsLayout) {
            getGreetWidgetData();
        }
        if (v == videoLayout) {
            getVideoWidgetData();
        }
//        if (v == viewMore) {
//            Intent i = new Intent(getActivity(), GraphActivity.class);
//            i.putExtra("paguthi", paguthiId);
//            startActivity(i);
//        }
        if (v == closeConstitutePop) {
            consituentPopup.setVisibility(View.GONE);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            enableSearchView(searchView, true);
            selectArea.setClickable(true);
            selectOffice.setClickable(true);
            fromDate.setClickable(true);
            fromDate.setFocusable(true);
            toDate.setClickable(true);
            constituencyLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            footfallLayout.setClickable(true);
            meetingLayout.setClickable(true);
            volunteerLayout.setClickable(true);
            greetingsLayout.setClickable(true);
            videoLayout.setClickable(true);
        }
        if (v == closeGrievancePop) {
            grievancePopup.setVisibility(View.GONE);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            enableSearchView(searchView, true);
            selectArea.setClickable(true);
            selectOffice.setClickable(true);
            fromDate.setClickable(true);
            fromDate.setFocusable(true);
            toDate.setClickable(true);
            constituencyLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            footfallLayout.setClickable(true);
            meetingLayout.setClickable(true);
            volunteerLayout.setClickable(true);
            greetingsLayout.setClickable(true);
            videoLayout.setClickable(true);
        }
        if (v == closeFootfallPop) {
            footfallPopup.setVisibility(View.GONE);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            enableSearchView(searchView, true);
            selectArea.setClickable(true);
            selectOffice.setClickable(true);
            fromDate.setClickable(true);
            fromDate.setFocusable(true);
            toDate.setClickable(true);
            constituencyLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            footfallLayout.setClickable(true);
            meetingLayout.setClickable(true);
            volunteerLayout.setClickable(true);
            greetingsLayout.setClickable(true);
            videoLayout.setClickable(true);
        }
        if (v == closeMeetingPop) {
            meetingPopup.setVisibility(View.GONE);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            enableSearchView(searchView, true);
            selectArea.setClickable(true);
            selectOffice.setClickable(true);
            fromDate.setClickable(true);
            fromDate.setFocusable(true);
            toDate.setClickable(true);
            constituencyLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            footfallLayout.setClickable(true);
            meetingLayout.setClickable(true);
            volunteerLayout.setClickable(true);
            greetingsLayout.setClickable(true);
            videoLayout.setClickable(true);
        }
        if (v == closeVolunteerPop) {
            volunteerPopup.setVisibility(View.GONE);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            enableSearchView(searchView, true);
            selectArea.setClickable(true);
            selectOffice.setClickable(true);
            fromDate.setClickable(true);
            fromDate.setFocusable(true);
            toDate.setClickable(true);
            constituencyLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            footfallLayout.setClickable(true);
            meetingLayout.setClickable(true);
            volunteerLayout.setClickable(true);
            greetingsLayout.setClickable(true);
            videoLayout.setClickable(true);
        }
        if (v == closeGreetingsPop) {
            greetingsPopup.setVisibility(View.GONE);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            enableSearchView(searchView, true);
            selectArea.setClickable(true);
            selectOffice.setClickable(true);
            fromDate.setClickable(true);
            fromDate.setFocusable(true);
            toDate.setClickable(true);
            constituencyLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            footfallLayout.setClickable(true);
            meetingLayout.setClickable(true);
            volunteerLayout.setClickable(true);
            greetingsLayout.setClickable(true);
            videoLayout.setClickable(true);
        }
        if (v == closeVideoPop) {
            videoPopup.setVisibility(View.GONE);
            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            enableSearchView(searchView, true);
            selectArea.setClickable(true);
            selectOffice.setClickable(true);
            fromDate.setClickable(true);
            fromDate.setFocusable(true);
            toDate.setClickable(true);
            constituencyLayout.setClickable(true);
            grievanceLayout.setClickable(true);
            footfallLayout.setClickable(true);
            meetingLayout.setClickable(true);
            volunteerLayout.setClickable(true);
            greetingsLayout.setClickable(true);
            videoLayout.setClickable(true);
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
                    getOffice();
                }
                if (checkRes.equalsIgnoreCase("office")) {
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
                    officespinnerDataArrayAdapter = new ArrayAdapter<SpinnerData>(getActivity(), R.layout.spinner_data_layout, R.id.data_name, officespinnerData) { // The third parameter works around ugly Android legacy. http://stackoverflow.com/a/18529511/145173
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
                    getDashboard();
                } else if (checkRes.equalsIgnoreCase("dashboard")) {
                    JSONObject getData = response.getJSONObject("widgets_count");
                    constituentCount.setText(getData.getString("constituent_count"));
                    grievanceCount.setText(getData.getString("grievance_count"));
                    footfallCount.setText(getData.getString("footfall_count"));
                    meetingCount.setText(getData.getString("meeting_count"));
                    volunteerCount.setText(getData.getString("volunter_count"));
                    greetingsCount.setText(getData.getString("geeting_count"));
                    videoCount.setText(getData.getString("video_count"));

                    JSONArray data = response.getJSONArray("graph_result");

//                    ArrayList<BarEntry> totalGrievance = new ArrayList();
//                    ArrayList<BarEntry> newGrievance = new ArrayList();
//                    ArrayList<BarEntry> repGrievance = new ArrayList();
//                    ArrayList year = new ArrayList();
//                    ArrayList dataSets = new ArrayList();

//                    for (int i = 0; i < data.length(); i++) {
//                        if (i != 4) {
//                        float[] totalG = {Float.parseFloat(data.getJSONObject(i).getString("total")),Float.parseFloat(data.getJSONObject(i).getString("new_grev")),Float.parseFloat(data.getJSONObject(i).getString("repeated_grev"))};
//                            float totalG = Float.parseFloat(data.getJSONObject(i).getString("total"));
//                            float newG = Float.parseFloat(data.getJSONObject(i).getString("new_grev"));
//                            float ff2 = Float.parseFloat(data.getJSONObject(i).getString("repeated_grev"));
//                            totalGrievance.add(new BarEntry((float) i, totalG));
//                            newGrievance.add(new BarEntry((float) i, newG));
//                            repGrievance.add(new BarEntry((float) i, ff2));
//                        }
//                    }
//                    year.add("Jan 2020");
//                    year.add("Feb 2020");
//                    year.add("Mar 2020");
//                    year.add("May 2020");
//                    year.add("June 2020");
//                    year.add("July 2020");
//                    year.add("Aug 2020");
//                    year.add("Sept 2020");
//                    year.add("Oct 2020");
//                    year.add("Nov 2020");
//                    year.add("Dec 2020");

//                    BarDataSet bardataset = new BarDataSet(totalGrievance, "Total grievance");
//                    bardataset.setColor(ContextCompat.getColor(getActivity(), R.color.chart_one));
//                    BarDataSet bardataset1 = new BarDataSet(newGrievance, "New grievance");
//                    bardataset1.setColor(ContextCompat.getColor(getActivity(), R.color.chart_two));
//                    BarDataSet bardataset2 = new BarDataSet(repGrievance, "Repeated grievance");
//                    bardataset2.setColor(ContextCompat.getColor(getActivity(), R.color.chart_three));
//                    barChart.animateY(1000);
//
//
//                    BarData barData = new BarData(bardataset, bardataset1, bardataset2);
//                    barData.setValueFormatter(new LargeValueFormatter());
//                    barChart.setData(barData);
//                    barChart.getDescription().setText("");
//                    barChart.getBarData().setBarWidth(barWidth);

                    // restrict the x-axis range
//                    barChart.getXAxis().setAxisMinimum(-(0.5f));
//                    barChart.getXAxis().setAxisMaximum(12);
//
//                    Legend l = barChart.getLegend();
//                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
//                    l.setDrawInside(true);
//                    l.setYOffset(0f);
//                    l.setXOffset(10f);
//                    l.setYEntrySpace(0f);
//                    l.setTextSize(8f);

//                    YAxis leftAxis = barChart.getAxisLeft();
//                    leftAxis.setValueFormatter(new LargeValueFormatter());
//                    leftAxis.setDrawGridLines(false);
//                    leftAxis.setSpaceTop(35f);
//                    leftAxis.setAxisMinimum(0f);
//                    leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

//                    barChart.getAxisRight().setEnabled(false);

//                    XAxis xAxis = barChart.getXAxis();
//                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                    xAxis.setDrawGridLines(false);
//                    xAxis.setGranularity(1f); // only intervals of 1 day
//                    xAxis.setLabelCount(12);
//                    xAxis.setCenterAxisLabels(true);
//                    xAxis.setValueFormatter(new IndexAxisValueFormatter(year));

//                    barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
//                    barChart.groupBars(-0, groupSpace, barSpace);
//                    barChart.setVisibleXRangeMaximum(3);
//                    barChart.invalidate();

                    ArrayList<Entry> uniqueVal = new ArrayList<>();
                    ArrayList<Entry> repeatVal = new ArrayList<>();
                    ArrayList<Entry> totalVal = new ArrayList<>();
                    ArrayList year = new ArrayList();
                    for (int i = 0; i < data.length(); i++) {
//                        float[] footFallG = {Float.parseFloat(data.getJSONObject(i).getString("day_name")),Float.parseFloat(data.getJSONObject(i).getString("unique_count")),
//                                Float.parseFloat(data.getJSONObject(i).getString("repeat_count")), Float.parseFloat(data.getJSONObject(i).getString("total"))};
                        float uniqG = Float.parseFloat(data.getJSONObject(i).getString("unique_count"));
                        float repeatG = Float.parseFloat(data.getJSONObject(i).getString("repeat_count"));
                        float totalG = Float.parseFloat(data.getJSONObject(i).getString("total"));
                        year.add(data.getJSONObject(i).getString("day_name"));
                        uniqueVal.add(new Entry((float) i, uniqG));
                        repeatVal.add(new Entry((float) i, repeatG));
                        totalVal.add(new Entry((float) i, totalG));
//                        values.add(new Entry(i,footFallG));
                    }
                    LineDataSet uniqueDataSet = new LineDataSet(uniqueVal, "Unique Count");
                    uniqueDataSet.setDrawIcons(false);
                    uniqueDataSet.setColor(Color.BLUE);
                    uniqueDataSet.setCircleColor(Color.BLUE);
                    uniqueDataSet.setLineWidth(1f);
                    uniqueDataSet.setCircleRadius(3f);
                    uniqueDataSet.setDrawCircleHole(false);
                    uniqueDataSet.setValueTextSize(9f);
                    uniqueDataSet.setDrawFilled(false);
                    uniqueDataSet.setFormLineWidth(1f);
                    uniqueDataSet.setFormSize(15.f);

                    LineDataSet repeatDataSet = new LineDataSet(repeatVal, "Repeat Count");
                    repeatDataSet.setDrawIcons(false);
                    repeatDataSet.setColor(Color.RED);
                    repeatDataSet.setCircleColor(Color.RED);
                    repeatDataSet.setLineWidth(1f);
                    repeatDataSet.setCircleRadius(3f);
                    repeatDataSet.setDrawCircleHole(false);
                    repeatDataSet.setValueTextSize(9f);
                    repeatDataSet.setDrawFilled(false);
                    repeatDataSet.setFormLineWidth(1f);
                    repeatDataSet.setFormSize(15.f);

                    LineDataSet totalDataSet = new LineDataSet(totalVal, "Total Count");
                    totalDataSet.setDrawIcons(false);
                    totalDataSet.setColor(Color.GREEN);
                    totalDataSet.setCircleColor(Color.GREEN);
                    totalDataSet.setLineWidth(1f);
                    totalDataSet.setCircleRadius(3f);
                    totalDataSet.setDrawCircleHole(false);
                    totalDataSet.setValueTextSize(9f);
                    totalDataSet.setDrawFilled(false);
                    totalDataSet.setFormLineWidth(1f);
                    totalDataSet.setFormSize(15.f);
//
                    if (Utils.getSDKInt() >= 18) {
                        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
                        uniqueDataSet.setFillDrawable(drawable);
                        repeatDataSet.setFillDrawable(drawable);
                        totalDataSet.setFillDrawable(drawable);
                    } else {
                        uniqueDataSet.setFillColor(Color.BLUE);
                        repeatDataSet.setFillColor(Color.RED);
                        totalDataSet.setFillColor(Color.GREEN);
                    }
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(uniqueDataSet);
                    dataSets.add(repeatDataSet);
                    dataSets.add(totalDataSet);
                    LineData lineData = new LineData(dataSets);
                    lineChart.setData(lineData);

                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setGranularity(1f); // only intervals of 1 day
                    xAxis.setLabelCount(12);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(year));

                    Legend ll = lineChart.getLegend();
                    ll.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    ll.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                    ll.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                    ll.setWordWrapEnabled(true);
                    ll.setDrawInside(false);
                    ll.setYOffset(0f);
                    ll.setXOffset(15f);
                    ll.setXEntrySpace(10f);
                    ll.setYEntrySpace(10f);
                    ll.setTextSize(8f);

                    lineChart.setTouchEnabled(false);
//                    lineChart.highlightValue(null);
                    lineChart.getDescription().setEnabled(false);
                    lineChart.getLegend().setWordWrapEnabled(true);
                    lineChart.invalidate();

                } else if (checkRes.equalsIgnoreCase("widget_consti")) {
                    JSONArray getData = response.getJSONArray("constituent_details");
                    JSONObject object = getData.getJSONObject(0);
                    int getLength = getData.length();

                    DecimalFormat decimal = new DecimalFormat("0.00");
                    float malePer, femalePer, otherPer, mobile, whApp, broadCast, mail, voter, dob;

                    for (int i = 0; i < getLength; i++) {
                        constiPopupCount.setText(getString(R.string.constituent_count) + " - " + object.getString("total"));
                        malePer = Float.parseFloat((object.getString("malepercenatge")));
                        perMale.setText("( " + decimal.format(malePer) + "%" + " )");
                        maleCount.setText(object.getString("malecount"));
                        femalePer = Float.parseFloat(object.getString("femalepercenatge"));
                        perFemale.setText("( " + decimal.format(femalePer) + "%" + " )");
                        femaleCount.setText(object.getString("femalecount"));
                        otherPer = Float.parseFloat(object.getString("otherpercenatge"));
                        perOthers.setText("( " + decimal.format(otherPer) + "%" + " )");
                        othersCount.setText(object.getString("others"));
                        mobile = Float.parseFloat(object.getString("mobile_percentage"));
                        perPhone.setText("( " + decimal.format(mobile) + "%" + " )");
                        phoneCount.setText(object.getString("having_mobilenumber"));
                        whApp = Float.parseFloat(object.getString("whatsapp_percentage"));
                        perWhatsApp.setText("( " + decimal.format(whApp) + "%" + " )");
                        whatsAppCount.setText(object.getString("having_whatsapp"));
                        broadCast = Float.parseFloat(object.getString("broadcast_percentage"));
                        perBroadcast.setText("( " + decimal.format(broadCast) + "%" + " )");
                        broadcastCount.setText(object.getString("having_whatsapp_broadcast"));
                        mail = Float.parseFloat(object.getString("email_percentage"));
                        perMail.setText("( " + decimal.format(mail) + "%" + " )");
                        mailCount.setText(object.getString("having_email"));
                        voter = Float.parseFloat(object.getString("having_voter_percenatge"));
                        perVoter.setText("( " + decimal.format(voter) + "%" + " )");
                        voterCount.setText(object.getString("having_vote_id"));
                        dob = Float.parseFloat(object.getString("having_dob_percentage"));
                        perDob.setText("( " + decimal.format(dob) + "%" + " )");
                        dobCount.setText(object.getString("having_dob"));
                    }
                    consituentPopup.setVisibility(View.VISIBLE);
                    scrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                    enableSearchView(searchView, false);
                    selectArea.setClickable(false);
                    selectOffice.setClickable(false);
                    fromDate.setClickable(false);
                    fromDate.setFocusable(false);
                    toDate.setClickable(false);
                    toDate.setFocusable(false);
                    constituencyLayout.setClickable(false);
                    meetingLayout.setClickable(false);
                    grievanceLayout.setClickable(false);
                    footfallLayout.setClickable(false);
                    volunteerLayout.setClickable(false);
                    greetingsLayout.setClickable(false);
                    videoLayout.setClickable(false);
                } else if (checkRes.equalsIgnoreCase("widget_grie")) {
                    grievPopupCount.setText(getString(R.string.total_grievances) + " - " + response.getString("tot_grive_count"));
                    totGrievances.setText(response.getString("tot_grive_count"));
                    enquiryCount.setText(response.getString("enquiry_count"));
                    petitionCount.setText(response.getString("petition_count"));
                    onlinePetition.setText(response.getJSONObject("petition_list").getString("no_of_online"));
                    civicPetition.setText(response.getJSONObject("petition_list").getString("no_of_civic"));
                    onlineEnq.setText(response.getJSONObject("petition_list").getString("no_of_online"));
                    civicEnq.setText(response.getJSONObject("petition_list").getString("no_of_civic"));
                    grievancePopup.setVisibility(View.VISIBLE);
                    scrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                    enableSearchView(searchView, false);
                    selectArea.setClickable(false);
                    selectOffice.setClickable(false);
                    fromDate.setClickable(false);
                    fromDate.setFocusable(false);
                    toDate.setClickable(false);
                    toDate.setFocusable(false);
                    constituencyLayout.setClickable(false);
                    meetingLayout.setClickable(false);
                    grievanceLayout.setClickable(false);
                    footfallLayout.setClickable(false);
                    volunteerLayout.setClickable(false);
                    greetingsLayout.setClickable(false);
                    videoLayout.setClickable(false);
                } else if (checkRes.equalsIgnoreCase("widget_foot")) {
                    footPopupCount.setText(getString(R.string.footfall_count) + " - " + response.getJSONObject("footfall_details").getString("total_footfall_cnt"));
                    uinqueCount.setText(response.getJSONObject("footfall_details").getString("unique_footfall_cnt"));
                    totFoot.setText(response.getJSONObject("footfall_details").getString("total_footfall_cnt"));
                    paguthiCount.setText(response.getJSONObject("footfall_details").getString("constituency_cnt"));
                    otherPaguthi.setText(response.getJSONObject("footfall_details").getString("other_cnt"));
                    footfallPopup.setVisibility(View.VISIBLE);
                    scrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                    enableSearchView(searchView, false);
                    selectArea.setClickable(false);
                    selectOffice.setClickable(false);
                    fromDate.setClickable(false);
                    fromDate.setFocusable(false);
                    toDate.setClickable(false);
                    toDate.setFocusable(false);
                    constituencyLayout.setClickable(false);
                    meetingLayout.setClickable(false);
                    grievanceLayout.setClickable(false);
                    footfallLayout.setClickable(false);
                    volunteerLayout.setClickable(false);
                    greetingsLayout.setClickable(false);
                    videoLayout.setClickable(false);
                } else if (checkRes.equalsIgnoreCase("widget_meet")) {
                    meetPopupCount.setText(getString(R.string.total_meetings) + " - " + response.getJSONObject("meeting_details").getString("total_meeting"));
                    perRequest.setText("( " + response.getJSONObject("meeting_details").getString("request_count_percentage") + "%" + " )");
                    requestedmeetingCount.setText(response.getJSONObject("meeting_details").getString("request_count"));
                    perComplete.setText("( " + response.getJSONObject("meeting_details").getString("complete_count_percentage") + "%" + " )");
                    completedmeetingCount.setText(response.getJSONObject("meeting_details").getString("complete_count"));
                    meetingPopup.setVisibility(View.VISIBLE);
                    scrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                    enableSearchView(searchView, false);
                    selectArea.setClickable(false);
                    selectOffice.setClickable(false);
                    fromDate.setClickable(false);
                    fromDate.setFocusable(false);
                    toDate.setClickable(false);
                    toDate.setFocusable(false);
                    constituencyLayout.setClickable(false);
                    meetingLayout.setClickable(false);
                    grievanceLayout.setClickable(false);
                    footfallLayout.setClickable(false);
                    volunteerLayout.setClickable(false);
                    greetingsLayout.setClickable(false);
                    videoLayout.setClickable(false);
                } else if (checkRes.equalsIgnoreCase("widget_vol")) {
                    volPopupCount.setText(getString(R.string.volunteer_count) + " - " + response.getJSONObject("volunteer_details").getString("total_volunteer"));
                    perVol.setText("( " + response.getJSONObject("volunteer_details").getString("volunteer_percentage") + "%" + " )");
                    volCount.setText(response.getJSONObject("volunteer_details").getString("no_of_volunteer"));
                    perNonVol.setText("( " + response.getJSONObject("volunteer_details").getString("nonvolunteer_percentage") + "%" + " )");
                    nonVolCount.setText(response.getJSONObject("volunteer_details").getString("no_of_nonvolunteer"));
                    volunteerPopup.setVisibility(View.VISIBLE);
                    scrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                    enableSearchView(searchView, false);
                    selectArea.setClickable(false);
                    selectOffice.setClickable(false);
                    fromDate.setClickable(false);
                    fromDate.setFocusable(false);
                    toDate.setClickable(false);
                    toDate.setFocusable(false);
                    constituencyLayout.setClickable(false);
                    meetingLayout.setClickable(false);
                    grievanceLayout.setClickable(false);
                    footfallLayout.setClickable(false);
                    volunteerLayout.setClickable(false);
                    greetingsLayout.setClickable(false);
                    videoLayout.setClickable(false);
                } else if (checkRes.equalsIgnoreCase("widget_greet")) {
                    greetPopupCount.setText(getString(R.string.total_greetings) + " - " + response.getJSONObject("greetings_details").getString("total_greetings"));
//                    perVol.setText("( " + response.getJSONObject("meeting_details").getString("volunteer_percentage") + "%" + " )");
                    birthCount.setText(response.getJSONObject("greetings_details").getString("birthday_wish_count"));
//                    perNonVol.setText("( " + response.getJSONObject("meeting_details").getString("nonvolunteer_percentage") + "%" + " )");
                    festCount.setText(response.getJSONObject("greetings_details").getString("festival_wishes_count"));

                    JSONArray getData = response.getJSONArray("festival_greetings_details");
//                    JSONObject object = getData.getJSONObject(0);
                    int getLength = getData.length();

                    for (int i = 0; i < getLength; i++) {
                        fest1.setText(getData.getJSONObject(1).getString("festival_name"));
                        perFest1.setText("( " + getData.getJSONObject(1).getString("festival_wishes_percentage") + "%" + " )");
                        festCount1.setText(getData.getJSONObject(1).getString("festival_wish_cnt"));
                        fest2.setText(getData.getJSONObject(2).getString("festival_name"));
                        perFest2.setText("( " + getData.getJSONObject(2).getString("festival_wishes_percentage") + "%" + " )");
                        festCount2.setText(getData.getJSONObject(2).getString("festival_wish_cnt"));
                    }
                    greetingsPopup.setVisibility(View.VISIBLE);
                    scrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                    enableSearchView(searchView, false);
                    selectArea.setClickable(false);
                    selectOffice.setClickable(false);
                    fromDate.setClickable(false);
                    fromDate.setFocusable(false);
                    toDate.setClickable(false);
                    toDate.setFocusable(false);
                    constituencyLayout.setClickable(false);
                    meetingLayout.setClickable(false);
                    grievanceLayout.setClickable(false);
                    footfallLayout.setClickable(false);
                    volunteerLayout.setClickable(false);
                    greetingsLayout.setClickable(false);
                    videoLayout.setClickable(false);
                } else if (checkRes.equalsIgnoreCase("widget_video")) {
                    vidPopupCount.setText(getString(R.string.total_videos) + " - " + response.getString("video_count"));
                    JSONArray getData = response.getJSONArray("video_list");
                    int getLength = getData.length();
                    for (int i = 0; i < getLength; i++) {
                        off1.setText(getData.getJSONObject(0).getString("office_name"));
                        perVid1.setText("( " + getData.getJSONObject(0).getString("video_percentage") + "%" + " )");
                        vidCount1.setText(getData.getJSONObject(0).getString("video_cnt"));
                        off2.setText(getData.getJSONObject(1).getString("office_name"));
                        perVid2.setText("( " + getData.getJSONObject(1).getString("video_percentage") + "%" + " )");
                        vidCount2.setText(getData.getJSONObject(1).getString("video_cnt"));
                        off3.setText(getData.getJSONObject(2).getString("office_name"));
                        perVid3.setText("( " + getData.getJSONObject(2).getString("video_percentage") + "%" + " )");
                        vidCount3.setText(getData.getJSONObject(2).getString("video_cnt"));
                        off4.setText(getData.getJSONObject(3).getString("office_name"));
                        perVid4.setText("( " + getData.getJSONObject(3).getString("video_percentage") + "%" + " )");
                        vidCount4.setText(getData.getJSONObject(3).getString("video_cnt"));
                        off5.setText(getData.getJSONObject(4).getString("office_name"));
                        perVid5.setText("( " + getData.getJSONObject(4).getString("video_percentage") + "%" + " )");
                        vidCount5.setText(getData.getJSONObject(4).getString("video_cnt"));
                        off6.setText(getData.getJSONObject(5).getString("office_name"));
                        perVid6.setText("( " + getData.getJSONObject(5).getString("video_percentage") + "%" + " )");
                        vidCount6.setText(getData.getJSONObject(5).getString("video_cnt"));
                        off7.setText(getData.getJSONObject(6).getString("office_name"));
                        perVid7.setText("( " + getData.getJSONObject(6).getString("video_percentage") + "%" + " )");
                        vidCount7.setText(getData.getJSONObject(6).getString("video_cnt"));
                        off8.setText(getData.getJSONObject(7).getString("office_name"));
                        perVid8.setText("( " + getData.getJSONObject(7).getString("video_percentage") + "%" + " )");
                        vidCount8.setText(getData.getJSONObject(7).getString("video_cnt"));
                        off9.setText(getData.getJSONObject(8).getString("office_name"));
                        perVid9.setText("( " + getData.getJSONObject(8).getString("video_percentage") + "%" + " )");
                        vidCount9.setText(getData.getJSONObject(8).getString("video_cnt"));
                    }
                    videoPopup.setVisibility(View.VISIBLE);
                    scrollView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                    enableSearchView(searchView, false);
                    selectArea.setClickable(false);
                    selectOffice.setClickable(false);
                    fromDate.setClickable(false);
                    fromDate.setFocusable(false);
                    toDate.setClickable(false);
                    toDate.setFocusable(false);
                    constituencyLayout.setClickable(false);
                    meetingLayout.setClickable(false);
                    grievanceLayout.setClickable(false);
                    footfallLayout.setClickable(false);
                    volunteerLayout.setClickable(false);
                    greetingsLayout.setClickable(false);
                    videoLayout.setClickable(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showSelectorData() {
        if (!submenuVisible) {
            dataSelectionLayout.setVisibility(View.VISIBLE);
            showdata.setText("-");
        } else {
            dataSelectionLayout.setVisibility(View.GONE);
            showdata.setText("+");
        }
        submenuVisible = !submenuVisible;
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
//                        getDashboard();
                    }
                });
        builderSingle.show();
    }

    private void showOfficeSpinnerData() {
        Log.d(TAG, "Show timing lists");
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
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

    @Override
    public void onError(String error) {

    }

    private void showDatePicker() {

        Log.d(TAG, "Show the birthday date");
        Calendar newCalendar = Calendar.getInstance();
        String setDate = "";
        if (fr) {
            setDate = fromDate.getText().toString();
        } else {
            setDate = toDate.getText().toString();
        }
        Log.d(TAG, "set date is" + setDate);
        int month = newCalendar.get(Calendar.MONTH);
        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
        int year = newCalendar.get(Calendar.YEAR);

        if ((setDate != null) && !(setDate.isEmpty())) {
            //extract the date/month and year
            try {
                Date startDate = mDateFormatter.parse(setDate);
                Calendar newDate = Calendar.getInstance();

                newDate.setTime(startDate);
                month = newDate.get(Calendar.MONTH);
                day = newDate.get(Calendar.DAY_OF_MONTH);
                year = newDate.get(Calendar.YEAR);
                Log.d(TAG, "month" + month + "day" + day + "year" + year);

            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                mDatePicker = new DatePickerDialog(getActivity(), R.style.datePickerTheme, this, year, month, day);
                mDatePicker.show();
            }
        } else {
            Log.d(TAG, "show default date");

            mDatePicker = new DatePickerDialog(getActivity(), R.style.datePickerTheme,this, year, month, day);
            mDatePicker.show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

        Calendar newDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        if (fr) {
            fromDate.setText(mDateFormatter.format(newDate.getTime()));
        } else {
            toDate.setText(mDateFormatter.format(newDate.getTime()));
        }
        fr = false;
        to = false;
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

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        View v = getCurrentFocus();
//
//        if (v != null &&
//                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
//                v instanceof EditText &&
//                !v.getClass().getName().startsWith("android.webkit.")) {
//            int scrcoords[] = new int[2];
//            v.getLocationOnScreen(scrcoords);
//            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
//            float y = ev.getRawY() + v.getTop() - scrcoords[1];
//
//            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
//                hideKeyboard(getActivity());
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    //    public static void hideKeyboard(Activity activity) {
//        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
//        }
//    }
    private void enableSearchView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                enableSearchView(child, enabled);
            }
        }
    }
}