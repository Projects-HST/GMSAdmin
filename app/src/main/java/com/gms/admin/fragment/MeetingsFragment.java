package com.gms.admin.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gms.admin.R;
import com.gms.admin.activity.ConstituentDetailsActivity;
import com.gms.admin.activity.MeetingDetailActivity;
import com.gms.admin.activity.SearchResultActivity;
import com.gms.admin.activity.SearchResultMeetingActivity;
import com.gms.admin.adapter.ConstituentListAdapter;
import com.gms.admin.adapter.MeetingListAdapter;
import com.gms.admin.bean.support.ConstituentUserList;
import com.gms.admin.bean.support.Meeting;
import com.gms.admin.bean.support.MeetingList;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MeetingsFragment extends Fragment implements IServiceListener, DialogClickListener, MeetingListAdapter.OnItemClickListener {

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
    SwipeRefreshLayout swipeRefreshLayout;
    int listcount = 0;
    MeetingList meetingList;
    ArrayList<Meeting> meetingArrayList = new ArrayList<>();
    MeetingListAdapter mAdapter;

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
        view = inflater.inflate(R.layout.fragment_meetings, container, false);
        serviceHelper = new ServiceHelper(view.getContext());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(view.getContext());
        constituentCount = view.findViewById(R.id.frag_meeting_count);

        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.list_refresh);

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

        getMeetingsList(String.valueOf(listcount));

        return view;
    }
    private void loadmore() {
        listcount = listcount + 50;
        getMeetingsList(String.valueOf(listcount));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        inflater.inflate(R.menu.right_menu, menu);

        searchView = (SearchView) menu.findItem(R.id.menu_item).getActionView();
        searchView.setIconified(true);
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
        startActivity(new Intent(getActivity(), SearchResultMeetingActivity.class));
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            try {
                constituentCount.setText(response.getString("meeting_count") + " Records");
                cc = response.getJSONArray("meeting_details").length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            meetingList = gson.fromJson(response.toString(), MeetingList.class);
            meetingArrayList.addAll(meetingList.getMeetingArrayList());
            mAdapter = new MeetingListAdapter(meetingArrayList, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            recyclerView.scrollToPosition(listcount);
            swipeRefreshLayout.setRefreshing(false);

        }
    }

    private void getMeetingsList(String count) {
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            JSONObject jsonObject = new JSONObject();
            try {
//                if (val == 0) {
//                    paguthiID = "ALL";
//                }
                jsonObject.put(GMSConstants.KEY_CONSTITUENT_ID, PreferenceStorage.getConstituencyID(getActivity()));
                jsonObject.put(GMSConstants.KEY_OFFSET, count);
                jsonObject.put(GMSConstants.KEY_ROWCOUNT, "50");
                jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_MEETINGS;
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
    public void onError(String error) {

    }

    @Override
    public void onItemClick(View view, int position) {
        Meeting meeting = null;
        meeting = meetingArrayList.get(position);
        Intent intent = new Intent(getActivity(), MeetingDetailActivity.class);
        intent.putExtra("meetingObj", meeting.getid());
        startActivity(intent);
    }
}