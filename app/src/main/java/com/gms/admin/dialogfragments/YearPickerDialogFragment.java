//package com.gms.admin.dialogfragments;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.NumberPicker;
//
//import androidx.fragment.app.DialogFragment;
//
//import com.gms.admin.R;
//
//import java.util.Calendar;
//
//public class YearPickerDialogFragment extends DialogFragment {
//
//
//    private static final int MAX_YEAR = 2099;
//    private DatePickerDialog.OnDateSetListener listener;
//
//    public void setListener(DatePickerDialog.OnDateSetListener listener) {
//        this.listener = listener;
//    }
//
//    @SuppressLint("ResourceAsColor")
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.datePickerTheme);
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        Calendar cal = Calendar.getInstance();
//
//        View dialog = inflater.inflate(R.layout.year_picker_dialog, null);
//        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);
//
//        int year = cal.get(Calendar.YEAR);
//        yearPicker.setMinValue(1900);
//        yearPicker.setMaxValue(3500);
//        yearPicker.setValue(year);
//
//        builder.setView(dialog).setPositiveButton(Html.fromHtml("<font color='#1F1F1F'>Ok</font>"), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                listener.onDateSet(null, yearPicker.getValue(), 0, 0);
//            }
//        }).setNegativeButton(Html.fromHtml("<font color='#1F1F1F'>Cancel</font>"), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                YearPickerDialogFragment.this.getDialog().cancel();
//            }
//        });
//        return builder.create();
//    }
//
//}
