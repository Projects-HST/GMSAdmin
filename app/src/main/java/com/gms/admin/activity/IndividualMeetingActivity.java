package com.gms.admin.activity;

import android.app.ActionBar;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gms.admin.R;
import com.gms.admin.adapter.IndividualMeetingListAdapter;
import com.gms.admin.adapter.IndividualMeetingListAdapterNew;
import com.gms.admin.adapter.MeetingListAdapter;
import com.gms.admin.bean.support.IndividualMeeting;
import com.gms.admin.bean.support.IndividualMeetingList;
import com.gms.admin.bean.support.Meeting;
import com.gms.admin.bean.support.MeetingList;
import com.gms.admin.bean.support.User;
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

public class IndividualMeetingActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener, IndividualMeetingListAdapterNew.OnItemClickListener {

    private static final String TAG = IndividualMeetingActivity.class.getName();
    private User user;


    private String whatRes = "";
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ListView listView;
    private ArrayList<IndividualMeeting> meetings = new ArrayList<>();
//    private IndividualMeetingListAdapter meetingListAdapter;
IndividualMeetingListAdapterNew mAdapter;
    private RelativeLayout toolBar;
    private RecyclerView recyclerView;
    public SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_meetings);
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

        user = (User) getIntent().getSerializableExtra("userObj");

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
//        swipeRefreshLayout.setRefreshing(true);

        getMeetingList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
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

    private void getMeetingList() {
        JSONObject jsonObject = new JSONObject();
        String id = "";
        id = user.getid();
        try {
            jsonObject.put(GMSConstants.KEY_CONSTITUENT_ID, id);
            jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(this));
//            jsonObject.put(GMSConstants.KEY_USER_ID, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_CONSTITUENT_MEETINGS;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onClick(View v) {

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

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
//            Gson gson = new Gson();
//            IndividualMeetingList meetingList = gson.fromJson(response.toString(), IndividualMeetingList.class);
//            if (meetingList.getMeetingArrayList() != null && meetingList.getMeetingArrayList().size() > 0) {
////                    this.ongoingServiceArrayList.addAll(ongoingServiceList.getserviceArrayList());
//                updateListAdapter(meetingList.getMeetingArrayList());
//            } else {
//                if (meetings != null) {
//                    meetings.clear();
//                    updateListAdapter(meetingList.getMeetingArrayList());
//                }
//            }

            Gson gson = new Gson();
            IndividualMeetingList meetingList = gson.fromJson(response.toString(), IndividualMeetingList.class);
            meetings.addAll(meetingList.getMeetingArrayList());
            mAdapter = new IndividualMeetingListAdapterNew(meetings, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);

        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }
    @Override
    public void onItemClick(View view, int position) {
        IndividualMeeting meeting = null;
        meeting = meetings.get(position);
        Intent intent = new Intent(this, IndividualMeetingDetailActivity.class);
        intent.putExtra("meetingObj", meeting);
        startActivity(intent);
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.d(TAG, "onEvent list item clicked" + position);
//        IndividualMeeting meeting = null;
//        if ((meetingListAdapter != null) && (meetingListAdapter.ismSearching())) {
//            Log.d(TAG, "while searching");
//            int actualindex = meetingListAdapter.getActualEventPos(position);
//            Log.d(TAG, "actual index" + actualindex);
//            meeting = meetings.get(actualindex);
//        } else {
//            meeting = meetings.get(position);
//        }
////        PreferenceStorage.saveUserId(this, meeting.getid());
//        Intent intent = new Intent(this, IndividualMeetingDetailActivity.class);
//        intent.putExtra("serviceObj", meeting);
//        startActivity(intent);
//    }
}