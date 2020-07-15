package com.gms.admin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gms.admin.R;
import com.gms.admin.adapter.DocumentFragmentAdapter;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.PreferenceStorage;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class IndividualDocumentsActivity extends AppCompatActivity implements View.OnClickListener, DialogClickListener{

    private static final String TAG = IndividualDocumentsActivity.class.getName();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabLayout.TabLayoutOnPageChangeListener tabatab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_docs);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.removeOnPageChangeListener(tabatab);
                finish();

            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        initialiseTabs();
    }

    private void initialiseTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.doc_constituent)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.doc_grievance)));

        final DocumentFragmentAdapter adapter = new DocumentFragmentAdapter
                (getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        tabatab = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        viewPager.addOnPageChangeListener(tabatab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.getCurrentItem();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.getCurrentItem();
            }
        });
//Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (tabLayout.getTabCount() <= 2) {
            tabLayout.setTabMode(TabLayout.
                    MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.
                    MODE_SCROLLABLE);
        }
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
}