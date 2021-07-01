package com.gms.admin.dialogfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.annotation.StyleRes;

import com.gms.admin.R;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.utils.GMSConstants;

/**
 * Created by Admin on 17-10-2017.
 */

public class SimpleAlertDialogFragment extends DialogFragment {

    private int tag;
    private int theme;
    DialogClickListener dialogActions;

    public static SimpleAlertDialogFragment newInstance(int title, String message, @StyleRes int themeResId) {
        SimpleAlertDialogFragment frag = new SimpleAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(GMSConstants.ALERT_DIALOG_TITLE, title);
        args.putString(GMSConstants.ALERT_DIALOG_MESSAGE, message);
        args.putInt(GMSConstants.ALERT_DIALOG_THEME, themeResId);
        frag.setArguments(args);
        return frag;
    }

    public static SimpleAlertDialogFragment newInstance(int title, String message, int tag, @StyleRes int themeResId) {
        SimpleAlertDialogFragment frag = new SimpleAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(GMSConstants.ALERT_DIALOG_TITLE, title);
        args.putString(GMSConstants.ALERT_DIALOG_MESSAGE, message);
        args.putInt(GMSConstants.ALERT_DIALOG_TAG, tag);
        args.putInt(GMSConstants.ALERT_DIALOG_THEME, themeResId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.alertDialogueTheme);
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
        int title = args.getInt(GMSConstants.ALERT_DIALOG_TITLE);
        tag = args.getInt(GMSConstants.ALERT_DIALOG_TAG, 0);
//        theme = args.getInt(GMSConstants.ALERT_DIALOG_THEME, R.style.alertDialogueTheme);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.alertDialogueTheme);

        return new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.alertDialogueTheme))
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_button_ok, mListener).create();

//        AlertDialog dialog = builder.create();
//        dialog.show();
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.text_black));
    }

    DialogInterface.OnClickListener mListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (which == -1) {
                dialog.cancel();
                if (SimpleAlertDialogFragment.this.dialogActions != null)
                    SimpleAlertDialogFragment.this.dialogActions
                            .onAlertPositiveClicked(tag);

            } else {
                dialog.cancel();
                if (SimpleAlertDialogFragment.this.dialogActions != null)
                    SimpleAlertDialogFragment.this.dialogActions
                            .onAlertNegativeClicked(tag);
            }
        }
    };
}
