package com.gms.admin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gms.admin.R;
import com.gms.admin.adapter.ProfileFragmentAdapter;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.GMSValidator;
import com.gms.admin.utils.PreferenceStorage;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class IndividualProfileActivity extends AppCompatActivity {
    private static final String TAG = IndividualProfileActivity.class.getName();

    private int ab = 0;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabLayout.TabLayoutOnPageChangeListener tabatab;
    private TextView userName, txtSerialNo;
    private ImageView profilePic;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                viewPager.removeOnPageChangeListener(tabatab);
                finish();
            }
        });

        userName = (TextView) findViewById(R.id.user_name);
        txtSerialNo = (TextView) findViewById(R.id.serial_number);
        profilePic = (ImageView) findViewById(R.id.profile_img);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        userName.setText(PreferenceStorage.getConstituentName(this));
        txtSerialNo.setText(PreferenceStorage.getSerialNo(this));

        String urrl = PreferenceStorage.getCOnstituentProfilePic(this);

        if (GMSValidator.checkNullString(urrl)) {
            Picasso.get().load(urrl).into(profilePic);
        } else {
            profilePic.setImageResource(R.drawable.ic_profile);
        }

        initialiseTabs();
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

    private void initialiseTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.profile)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.constituency)));

        final ProfileFragmentAdapter adapter = new ProfileFragmentAdapter(getSupportFragmentManager());

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
}