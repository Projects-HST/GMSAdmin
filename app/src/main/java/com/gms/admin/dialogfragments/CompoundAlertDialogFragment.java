package com.gms.admin.dialogfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;

import androidx.annotation.StyleRes;

import com.gms.admin.R;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.utils.GMSConstants;

/**
 * Created by Admin on 20-10-2017.
 */

public class CompoundAlertDialogFragment extends DialogFragment {

    private int tag;
    private int theme;
    AlertDialog dialog;
    DialogClickListener dialogActions;

    public static CompoundAlertDialogFragment newInstance(String title, String message, String posButton, String negButton, int tag, @StyleRes int themeResId) {
        CompoundAlertDialogFragment frag = new CompoundAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString(GMSConstants.ALERT_DIALOG_TITLE, title);
        args.putString(GMSConstants.ALERT_DIALOG_MESSAGE, message);
        args.putString(GMSConstants.ALERT_DIALOG_POS_BUTTON, posButton);
        args.putString(GMSConstants.ALERT_DIALOG_NEG_BUTTON, negButton);
        args.putInt(GMSConstants.ALERT_DIALOG_TAG, tag);
        args.putInt(GMSConstants.ALERT_DIALOG_THEME, themeResId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogActions = (DialogClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling activity must implement DialogClickListener interface");
        }
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.alertDialogueTheme);
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String message = args.getString(GMSConstants.ALERT_DIALOG_MESSAGE, "");
        String title = args.getString(GMSConstants.ALERT_DIALOG_TITLE);
        tag = args.getInt(GMSConstants.ALERT_DIALOG_TAG, 0);
//        theme = args.getInt(GMSConstants.ALERT_DIALOG_THEME, R.style.datePickerTheme);

        String posButton = args.getString(GMSConstants.ALERT_DIALOG_POS_BUTTON, getActivity().getString(R.string.alert_button_ok));
        String negButton = args.getString(GMSConstants.ALERT_DIALOG_NEG_BUTTON, getActivity().getString(R.string.alert_button_cancel));
        return new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.alertDialogueTheme))
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(posButton, mListener)
                .setNegativeButton(negButton, mListener).create();
    }

    DialogInterface.OnClickListener mListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (which == -1) {
                dialog.cancel();
                if (CompoundAlertDialogFragment.this.dialogActions != null)
                    CompoundAlertDialogFragment.this.dialogActions
                            .onAlertPositiveClicked(tag);

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPreferences.edit().clear().commit();


//                Intent navigationIntent = new Intent(getActivity(), SplashScreenActivity.class);
//                navigationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(navigationIntent);
                getActivity().finish();

            } else {
                dialog.cancel();
                if (CompoundAlertDialogFragment.this.dialogActions != null)
                    CompoundAlertDialogFragment.this.dialogActions
                            .onAlertNegativeClicked(tag);
            }
        }

    };
}
