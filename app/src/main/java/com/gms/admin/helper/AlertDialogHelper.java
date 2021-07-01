package com.gms.admin.helper;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;

import androidx.annotation.StyleRes;

import com.gms.admin.R;
import com.gms.admin.dialogfragments.AlertDialogForFragment;
import com.gms.admin.dialogfragments.CompoundAlertDialogFragment;
import com.gms.admin.dialogfragments.SimpleAlertDialogFragment;
import com.gms.admin.interfaces.DialogClickListener;


/**
 * Created by Admin on 17-10-2017.
 */

public class AlertDialogHelper {

    public static void showSimpleAlertDialog(Context context, String message, @StyleRes int themeResId) {
        DialogFragment simpleAlertDialogFragment = SimpleAlertDialogFragment.newInstance(
                R.string.empty, message, themeResId);
        final Activity activity = (Activity) context;
        if (activity != null) {
            try {
                simpleAlertDialogFragment.setCancelable(false);
//                simpleAlertDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.alertDialogueTheme);
                simpleAlertDialogFragment.show(activity.getFragmentManager(), "dialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showSimpleAlertDialog(Context context, String message, int tag, @StyleRes int themeResId) {
        DialogFragment simpleAlertDialogFragment = SimpleAlertDialogFragment.newInstance(
                R.string.empty, message, tag, themeResId);
        final Activity activity = (Activity) context;
        if (activity != null) {
            try {
                simpleAlertDialogFragment.setCancelable(false);
//                simpleAlertDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.alertDialogueTheme);
                simpleAlertDialogFragment.show(activity.getFragmentManager(), "dialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showAlertDialogForFragment(Context context, DialogClickListener dialogClickListener, String message, int tag, @StyleRes int themeResId) {
        AlertDialogForFragment alertDialogForFragment = AlertDialogForFragment.newInstance(
                R.string.empty, message, tag, themeResId);
        alertDialogForFragment.setDialogListener(dialogClickListener);
        final Activity activity = (Activity) context;
        if (activity != null)
            try {
                alertDialogForFragment.setCancelable(false);
                alertDialogForFragment.show(activity.getFragmentManager(), "dialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void showCompoundAlertDialog(Context context, String title, String message, String posButton, String negButton, int tag, @StyleRes int themeResId) {
        CompoundAlertDialogFragment compoundDialogFragment = CompoundAlertDialogFragment.newInstance(title, message, posButton, negButton, tag, themeResId);
        Activity activity = (Activity)context;
        if(activity != null) {
            compoundDialogFragment.show(activity.getFragmentManager(), "dialog");
        }

    }
}
