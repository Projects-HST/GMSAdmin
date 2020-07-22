package com.gms.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gms.admin.R;
import com.gms.admin.activity.SearchResultActivity;
import com.gms.admin.activity.SearchResultGrievanceActivity;
import com.gms.admin.adapter.ConstituentPaguthiTabAdapter;
import com.gms.admin.adapter.GrievancePaguthiTabAdapter;
import com.gms.admin.bean.support.Paguthi;
import com.gms.admin.bean.support.PaguthiList;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.PreferenceStorage;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GrievanceFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = ConstituentFragment.class.getName();
    private View rootView;
    public SearchView searchView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private String checkRes = "", paguthiId = "ALL";
    TabLayout tab;
    ViewPager viewPager;
    ArrayList<Paguthi> paguthiArrayList = new ArrayList<>();;
    DynamicGrievanceFragment dynamicPaguthiFragment;

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

        rootView = inflater.inflate(R.layout.fragment_constituent, container, false);



        tab = (TabLayout) rootView.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());

        getPaguthi();

        return rootView;

    }

    private void getPaguthi() {
        checkRes = "paguthi";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.KEY_CONSTITUENCY_ID, PreferenceStorage.getConstituencyID(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_PAGUTHI;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
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
                String searchtext = capitalizeString(newText);
//                dynamicPaguthiFragment.getMyString(searchtext);

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
        String id = "", name = "";
        id = paguthiArrayList.get(tab.getSelectedTabPosition()).getid();
        name = paguthiArrayList.get(tab.getSelectedTabPosition()).getpaguthi_name();
        PreferenceStorage.savePaguthiID(getActivity(), id);
        PreferenceStorage.savePaguthiName(getActivity(), name);
        startActivity(new Intent(getActivity(), SearchResultGrievanceActivity.class));
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
                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), msg);
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
            try {
                if (checkRes.equalsIgnoreCase("paguthi")) {
                    JSONArray getData = response.getJSONArray("paguthi_details");
                    Gson gson = new Gson();
                    PaguthiList paguthiList = gson.fromJson(response.toString(), PaguthiList.class);
                    if (paguthiList.getPaguthiArrayList() != null && paguthiList.getPaguthiArrayList().size() > 0) {
                        this.paguthiArrayList.add(new Paguthi("ALL", "All"));
                        this.paguthiArrayList.addAll(paguthiList.getPaguthiArrayList());
                    }
                    initialiseTabs(getData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    private void initialiseTabs(JSONArray subCategory) {
        tab.addTab(tab.newTab().setText("All"));
        for (int k = 0; k < subCategory.length(); k++) {
            try {
                String abc = capitalizeString(subCategory.getJSONObject(k).getString("paguthi_name"));
                tab.addTab(tab.newTab().setText(abc));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        PreferenceStorage.saveSubCatClick(getApplicationContext(), categoryArrayList.get(0).getSub_cat_id());
        GrievancePaguthiTabAdapter adapter = new GrievancePaguthiTabAdapter
                (getChildFragmentManager(), tab.getTabCount(), paguthiArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        dynamicPaguthiFragment = (DynamicGrievanceFragment) adapter.getRegisteredFragment(tab.getSelectedTabPosition());
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String id = "";
                id = paguthiArrayList.get(tab.getPosition()).getid();
                PreferenceStorage.savePaguthiID(getActivity(), id);
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.performClick();
                dynamicPaguthiFragment = (DynamicGrievanceFragment) adapter.getRegisteredFragment(tab.getPosition());
                searchView.setQuery("", false); // clear the text
                searchView.setIconified(true);
                searchView.clearFocus();
//                dynamicPaguthiFragment.clearMyString();
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                 recreate();
                String id = "";
                id = paguthiArrayList.get(tab.getPosition()).getid();
                PreferenceStorage.savePaguthiID(getActivity(), id);
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.performClick();
                dynamicPaguthiFragment = (DynamicGrievanceFragment) adapter.getRegisteredFragment(tab.getPosition());
                searchView.setQuery("", false); // clear the text
                searchView.setIconified(true);
                searchView.clearFocus();
//                dynamicPaguthiFragment.clearMyString();
            }
        });
//        tab.removeOnTabSelectedListener(TabLayout.OnTabSelectedListener);
//Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (tab.getTabCount() <= 2) {
            tab.setTabMode(TabLayout.
                    MODE_FIXED);
        } else {
            tab.setTabMode(TabLayout.
                    MODE_SCROLLABLE);
        }
    }

    @Override
    public void onError(String error) {

    }
}
