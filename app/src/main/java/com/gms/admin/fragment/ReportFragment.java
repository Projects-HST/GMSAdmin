package com.gms.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.gms.admin.R;
import com.gms.admin.activity.SearchResultActivity;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.PreferenceStorage;

import org.json.JSONObject;

public class ReportFragment extends Fragment implements IServiceListener, DialogClickListener, View.OnClickListener {
    private View view;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_report, container, false);

//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.list_refresh);
//        swipeRefreshLayout.setRefreshing(true);

//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
//
//                    if (!recyclerView.canScrollVertically(1)) {
//
////                        swipeRefreshLayout.setRefreshing(true);
//
////                        loadmore();
//
//                    }
//                }
//                return false;
//            }
//        });


        return view;
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

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}
