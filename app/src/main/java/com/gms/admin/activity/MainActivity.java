package com.gms.admin.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gms.admin.R;
import com.gms.admin.fragment.ConstituentFragment;
import com.gms.admin.fragment.DashboardFragment;
import com.gms.admin.fragment.GrievanceFragment;
import com.gms.admin.fragment.MeetingsFragment;
import com.gms.admin.fragment.ReportFragment;
import com.gms.admin.fragment.SettingsFragment;
import com.gms.admin.fragment.StaffFragment;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.CommonUtils;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.GMSValidator;
import com.gms.admin.utils.PreferenceStorage;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements IServiceListener, DialogClickListener, View.OnClickListener {

    Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private static final String TAG = LoginActivity.class.getName();
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private ImageView profilePic;
    private TextView name, area;
    private boolean doubleBackToExitPressedOnce = false;
    NavigationView navigationView;
    private boolean submenuVisible = false;
    String page;
    private LinearLayout vConstituentLayout;
    private RelativeLayout vHome, vConstituents, vConstituent, vMeetings, vGrievance, vUsers, vReport, vSettings, vLogout;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        page = getIntent().getExtras().getString("page");

        initializeNavigationDrawer();
        initializeService();
        initializeIDs();
    }

    private void initializeService() {
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
    }

    private void initializeIDs() {

        navigationView = findViewById(R.id.nav_view);
        profilePic = navigationView.getHeaderView(0).findViewById(R.id.profile_img);
        name = navigationView.getHeaderView(0).findViewById(R.id.full_name);
        name.setText(PreferenceStorage.getAdminName(this));
        area = navigationView.getHeaderView(0).findViewById(R.id.area);
        area.setText(PreferenceStorage.getUserConstituencyName(this));

        if (GMSValidator.checkNullString(PreferenceStorage.getProfilePic(this))) {
            Picasso.get().load(PreferenceStorage.getProfilePic(this)).into(profilePic);
        } else {
            profilePic.setImageResource(R.drawable.ic_profile);
        }

//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//
//            // This method will trigger on item Click of navigation menu
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
//
//                            case R.id.nav_dash:
//                                changePage(0);
//                                break;
//                            case R.id.nav_constituents:
//                                showItems();
//                                break;
//                            case R.id.nav_constituent_sub_one:
//                                changePage(1);
//                                break;
//                            case R.id.nav_constituent_sub_two:
//                                changePage(2);
//                                break;
//                            case R.id.nav_grievance:
//                                changePage(3);
//                                break;
//                            case R.id.nav_staffs:
//                                changePage(4);
//                                break;
//                            case R.id.nav_report:
//                                changePage(5);
//                                break;
//                            case R.id.nav_settings:
//                                changePage(6);
//                                break;
//                            case R.id.nav_logout:
//                                logout();
//                                break;
//                        }
//                        return true;
//                    }
//                });
//        if (page.equalsIgnoreCase("settings")) {
//            changePage(6);
//            navigationView.getMenu().getItem(7).setChecked(true);
//        } else {
//            changePage(0);
//            navigationView.getMenu().getItem(0).setChecked(true);
//        }
//
//        if (PreferenceStorage.getUserRole(this).equalsIgnoreCase("1")) {
//            navigationView.getMenu().getItem(5).setVisible(true);
//        } else {
//            navigationView.getMenu().getItem(5).setVisible(false);
//        }
        vHome = navigationView.getHeaderView(0).findViewById(R.id.home_img);
        vConstituentLayout = navigationView.getHeaderView(0).findViewById(R.id.sub_layout);
        vConstituents = navigationView.getHeaderView(0).findViewById(R.id.constituents);
        vConstituent = navigationView.getHeaderView(0).findViewById(R.id.constituent);
        vMeetings = navigationView.getHeaderView(0).findViewById(R.id.meetings);
        vGrievance = navigationView.getHeaderView(0).findViewById(R.id.grievance_layout);
        vUsers = navigationView.getHeaderView(0).findViewById(R.id.users_layout);
        vReport = navigationView.getHeaderView(0).findViewById(R.id.report_layout);
        vSettings = navigationView.getHeaderView(0).findViewById(R.id.settings_layout);
        vLogout = navigationView.getHeaderView(0).findViewById(R.id.sign_out_img);

        vHome.setOnClickListener(this);
        vConstituents.setOnClickListener(this);
        vConstituent.setOnClickListener(this);
        vMeetings.setOnClickListener(this);
        vGrievance.setOnClickListener(this);
        vUsers.setOnClickListener(this);
        vReport.setOnClickListener(this);
        vSettings.setOnClickListener(this);
        vLogout.setOnClickListener(this);

        changePage(0);

        callGetSubCategoryService();

    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

    public void changePage(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            toolbar.setTitle(getString(R.string.side_menu_dash));
            mDrawerLayout.closeDrawers();
            newFragment = new DashboardFragment();
        } else if (position == 1) {
            newFragment = new ConstituentFragment();
            toolbar.setTitle(getString(R.string.constituent_title));
            mDrawerLayout.closeDrawers();
        } else if (position == 2) {
            newFragment = new MeetingsFragment();
            toolbar.setTitle(getString(R.string.meeting));
            mDrawerLayout.closeDrawers();
        } else if (position == 3) {
            newFragment = new GrievanceFragment();
            toolbar.setTitle(getString(R.string.grievance_title));
            mDrawerLayout.closeDrawers();
        } else if (position == 4) {
            newFragment = new StaffFragment();
            toolbar.setTitle(getString(R.string.side_menu_staff));
            mDrawerLayout.closeDrawers();
        } else if (position == 5) {
            newFragment = new ReportFragment();
            toolbar.setTitle(getString(R.string.side_menu_report));
            mDrawerLayout.closeDrawers();
        } else if (position == 6) {
            newFragment = new SettingsFragment();
            toolbar.setTitle(getString(R.string.side_menu_settings));
            mDrawerLayout.closeDrawers();
        }

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer, newFragment)
                .commit();
    }

    private void showItems() {
        if (!submenuVisible) {
            vConstituentLayout.setVisibility(View.VISIBLE);
        } else {
            vConstituentLayout.setVisibility(View.GONE);
        }
        submenuVisible = !submenuVisible;
    }


    private void initializeNavigationDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {
//                String userProfileName = PreferenceStorage.getName(getApplicationContext());
//                String url = PreferenceStorage.getUserPicture(ParentDashBoardActivity.this);
//
//                Log.d(TAG, "user name value" + userProfileName);
//                if ((userProfileName != null) && !userProfileName.isEmpty()) {
//                    String[] splitStr = userProfileName.split("\\s+");
//                    navUserProfileName.setText("Hi, " + splitStr[0]);
//                }
//
//                if (((url != null) && !(url.isEmpty())) && !(url.equalsIgnoreCase(mCurrentUserProfileUrl))) {
//                    Log.d(TAG, "image url is " + url);
//                    mCurrentUserProfileUrl = url;
//                    Picasso.with(ParentDashBoardActivity.this).load(url).noPlaceholder().error(R.drawable.ab_logo).into(imgNavProfileImage);
//                }
            }
        };
//        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_hambugger);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public void callGetSubCategoryService() {
        if (CommonUtils.isNetworkAvailable(this)) {
            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            checkVersion();
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
    }

    private void checkVersion() {
        JSONObject jsonObject = new JSONObject();
        String id = "";
        try {
            jsonObject.put(GMSConstants.KEY_APP_VERSION, GMSConstants.KEY_APP_VERSION_VALUE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.CHECK_VERSION;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    private void logout() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getString(R.string.sign_out));
        alertDialogBuilder.setMessage(getString(R.string.sign_out_alert));
        alertDialogBuilder.setPositiveButton(R.string.alert_button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(alertDialogBuilder.getContext());
                sharedPreferences.edit().clear().apply();
//        TwitterUtil.getInstance().resetTwitterRequestToken();


                Intent homeIntent = new Intent(alertDialogBuilder.getContext(), SplashScreenActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.alert_button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        try {
            String status = response.getString("status");
            if (!status.equalsIgnoreCase("success")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Update");
                alertDialogBuilder.setMessage("A new version of GMS is available!");
                alertDialogBuilder.setPositiveButton("Get it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            finish();
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onClick(View view) {
        if (view == vHome) {
            changePage(0);
        }
        else if (view == vConstituents) {
            showItems();
        }
        else if (view == vConstituent) {
            changePage(1);
        }
        else if (view == vMeetings) {
            changePage(2);
        }
        else  if (view == vGrievance) {
            changePage(3);
        }
        else if (view == vUsers) {
            changePage(4);
        }
        else if (view == vReport) {
            changePage(5);
        }
        else if (view == vSettings) {
            changePage(6);
        }
        else if (view == vHome) {
            logout();
        }
    }
}