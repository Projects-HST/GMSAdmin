package com.gms.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gms.admin.R;
import com.gms.admin.bean.support.Birthday;
import com.gms.admin.bean.support.ReportGrievance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportBirthdayListAdapter extends RecyclerView.Adapter<ReportBirthdayListAdapter.MyViewHolder> {

    private ArrayList<Birthday> birthdayArrayList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtUser, txtdob, txtMobileNumber, txtStatus;

        public MyViewHolder(View view) {
            super(view);
            txtUser = (TextView) view.findViewById(R.id.user_name);
            txtdob = (TextView) view.findViewById(R.id.user_dob);
            txtMobileNumber = (TextView) view.findViewById(R.id.user_phone);
            txtStatus = (TextView) view.findViewById(R.id.birthday_status);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
//            else {
//                onClickListener.onClick(Selecttick);
//            }
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public ReportBirthdayListAdapter(ArrayList<Birthday> birthdayArrayList, OnItemClickListener onItemClickListener) {
        this.birthdayArrayList = birthdayArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_report_birthday, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Birthday birthday = birthdayArrayList.get(position);


        holder.txtMobileNumber.setText(capitalizeString(birthday.getmobile_no()));
        holder.txtUser.setText(capitalizeString(birthday.getfull_name()));
        holder.txtdob.setText(getserverdateformat(birthday.getdob()));

        if (birthday.getbirth_wish_status().equalsIgnoreCase("Send")) {
            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.txtStatus.getContext(), R.color.active));
            holder.txtStatus.setText("Birthday Letter Sent");
        } else {
            holder.txtStatus.setText("Birthday Letter Not Sent");
            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.txtStatus.getContext(), R.color.inactive));
        }
    }

    private String getserverdateformat(String dd) {
        String serverFormatDate = "";
        if (dd != null && dd != "") {

            String date = dd;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date testDate = null;
            try {
                testDate = formatter.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            serverFormatDate = sdf.format(testDate);
            System.out.println(".....Date..." + serverFormatDate);
        }
        return serverFormatDate;
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    @Override
    public int getItemCount() {
        return birthdayArrayList.size();
    }
}