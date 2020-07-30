package com.gms.admin.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gms.admin.R;
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

public class InteractionFragment extends Fragment  implements IServiceListener, DialogClickListener, View.OnClickListener {

    private static final String TAG = InteractionFragment.class.getName();

    private View rootView;
    LinearLayout listView;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

    public static ConstituencyInfoFragment newInstance(int position) {
        ConstituencyInfoFragment frag = new ConstituencyInfoFragment();
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

        rootView = inflater.inflate(R.layout.activity_individual_interaction, container, false);
        rootView.findViewById(R.id.activity_toolbar).setVisibility(View.GONE);
        listView = (LinearLayout) rootView.findViewById(R.id.list_view_interaction);

        serviceHelper = new ServiceHelper(getActivity());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());

        getInteraction();

        return rootView;
    }

    private void getInteraction() {
        JSONObject jsonObject = new JSONObject();
        String id = "";
        id = PreferenceStorage.getConstituentID(getActivity());
        try {
            jsonObject.put(GMSConstants.KEY_CONSTITUENT_ID, id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_CONSTITUENT_INTERACTION;
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
                loadMembersList(response.getJSONArray("interaction_details"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(getActivity(), error);
    }

    private void loadMembersList(JSONArray memberCount) {

        try {

            for (int c1 = 0; c1 < memberCount.length(); c1++) {
                LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mainLayoutParams.setMargins(0,0,0,0);

                LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cellParams.setMargins(40,10,40,10);

                LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
                lineParams.setMargins(40,20,40,0);

                LinearLayout.LayoutParams textQuestionParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textQuestionParams.setMargins(10,10,10,10);

                LinearLayout.LayoutParams textAnswerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textAnswerParams.setMargins(40,20,40,10);

                LinearLayout.LayoutParams yesParams = new LinearLayout.LayoutParams(140, 60);
                yesParams.setMargins(0,0,40,0);

                LinearLayout.LayoutParams noParams = new LinearLayout.LayoutParams(140, 60);
                noParams.setMargins(40,0,0,0);

                TextView textQuestion = new TextView(getActivity());
                textQuestion.setId(R.id.interaction_question);
                textQuestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                textQuestion.setGravity(Gravity.CENTER_HORIZONTAL);
                textQuestion.setText(memberCount.getJSONObject(c1).getString("interaction_text"));
                textQuestion.setLayoutParams(textQuestionParams);

                TextView yes = new TextView(getActivity());
                TextView no = new TextView(getActivity());
                yes.setGravity(Gravity.CENTER);
                no.setGravity(Gravity.CENTER);
                yes.setLayoutParams(yesParams);
                no.setLayoutParams(noParams);
                yes.setText(R.string.alert_button_yes);
                no.setText(R.string.alert_button_no);
                yes.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_grey));
                yes.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_inactive));
                no.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_grey));
                no.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_inactive));

                if (memberCount.getJSONObject(c1).getString("status").equalsIgnoreCase("No")) {
                    yes.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_grey));
                    yes.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_inactive));
                    no.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    no.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_no));
                } else {
                    yes.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    yes.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_yes));
                    no.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_grey));
                    no.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.btn_inactive));
                }

                LinearLayout answerLayout = new LinearLayout(getActivity());
                answerLayout.setOrientation(LinearLayout.HORIZONTAL);
                answerLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                answerLayout.setLayoutParams(textAnswerParams);

                answerLayout.addView(yes);
                answerLayout.addView(no);

                View view = new View(getActivity());
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.line_interaction));
                view.setLayoutParams(lineParams);

                LinearLayout cell = new LinearLayout(getActivity());
                cell.setOrientation(LinearLayout.VERTICAL);
                cell.setLayoutParams(cellParams);
                cell.addView(textQuestion);
                cell.addView(answerLayout);
                cell.addView(view);

                LinearLayout mainLayout = new LinearLayout(getActivity());
                mainLayout.setOrientation(LinearLayout.VERTICAL);
                mainLayout.setLayoutParams(mainLayoutParams);
                mainLayout.addView(cell);

                listView.addView(mainLayout);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}