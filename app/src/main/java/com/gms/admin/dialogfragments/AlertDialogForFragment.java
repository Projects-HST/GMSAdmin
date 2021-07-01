package com.gms.admin.dialogfragments;

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

public class AlertDialogForFragment extends DialogFragment {

    private int tag;
    private int theme;
    private int style;
    DialogClickListener dialogActions;

    public static AlertDialogForFragment newInstance(int title, String message, @StyleRes int themeResId) {
        AlertDialogForFragment frag = new AlertDialogForFragment();
        Bundle args = new Bundle();
        args.putInt(GMSConstants.ALERT_DIALOG_TITLE, title);
        args.putString(GMSConstants.ALERT_DIALOG_MESSAGE, message);
        args.putInt(GMSConstants.ALERT_DIALOG_THEME, themeResId);
        frag.setArguments(args);
        return frag;
    }

    public static AlertDialogForFragment newInstance(int title, String message, int tag, @StyleRes int themeResId) {
        AlertDialogForFragment frag = new AlertDialogForFragment();
        Bundle args = new Bundle();
        args.putInt(GMSConstants.ALERT_DIALOG_TITLE, title);
        args.putString(GMSConstants.ALERT_DIALOG_MESSAGE, message);
        args.putInt(GMSConstants.ALERT_DIALOG_TAG, tag);
        args.putInt(GMSConstants.ALERT_DIALOG_THEME, themeResId);
        frag.setArguments(args);
        return frag;
    }

    public void setDialogListener(DialogClickListener dialogActions) {
        this.dialogActions = dialogActions;
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
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.alertDialogueTheme);
//        theme = args.getInt(GMSConstants.ALERT_DIALOG_THEME, R.style.alertDialogueTheme);
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
                dialogActions.onAlertPositiveClicked(tag);

            } else {
                dialog.cancel();
                dialogActions.onAlertNegativeClicked(tag);
            }
        }

    };
}
