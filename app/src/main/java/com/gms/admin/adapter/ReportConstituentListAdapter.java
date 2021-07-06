package com.gms.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gms.admin.R;
import com.gms.admin.bean.support.ReportConstituent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportConstituentListAdapter extends RecyclerView.Adapter<ReportConstituentListAdapter.MyViewHolder> {

    private ArrayList<ReportConstituent> reportConstituentArrayList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtSurname, txtUser, txtdate, txtMobileNumber, txtAddress, txtGrievanceStatus;
        public LinearLayout grievanceLayout;

        public MyViewHolder(View view) {
            super(view);
            grievanceLayout = (LinearLayout) view.findViewById(R.id.grievance_layout);
            grievanceLayout.setOnClickListener(this);
            txtUser = (TextView) view.findViewById(R.id.full_name);
            txtSurname = (TextView) view.findViewById(R.id.father_husband_name);
            txtdate = (TextView) view.findViewById(R.id.dob);
            txtMobileNumber = (TextView) view.findViewById(R.id.mobile_number);
            txtAddress = (TextView) view.findViewById(R.id.address);
            txtGrievanceStatus = (TextView) view.findViewById(R.id.grievance_status);


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

    public ReportConstituentListAdapter (ArrayList<ReportConstituent> reportConstituentArrayList, OnItemClickListener onItemClickListener) {
        this.reportConstituentArrayList = reportConstituentArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_report_status, parent, false);

        return new ReportConstituentListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportConstituentListAdapter.MyViewHolder holder, int position) {
        ReportConstituent reportConstituent = reportConstituentArrayList.get(position);

        holder.txtMobileNumber.setText((reportConstituent.getMobile_no()));
        holder.txtSurname.setText(capitalizeString("Father Name" + " : " + reportConstituent.getFather_husband_name()));
        holder.txtdate.setText(("Date of Birth" + " : " +(getserverdateformat(reportConstituent.getDob()))));
        holder.txtUser.setText(capitalizeString(reportConstituent.getFull_name()));
        holder.txtAddress.setText((capitalizeString(reportConstituent.getDoor_no()) + (reportConstituent.getAddress()) + (reportConstituent.getPin_code())));

//        if (reportConstituent.getStatus().equalsIgnoreCase("COMPLETED")) {
//            holder.txtGrievanceStatus.setTextColor(ContextCompat.getColor(holder.txtGrievanceStatus.getContext(), R.color.completed_grievance));
//        } else {
//                  holder.txtGrievanceStatus.setTextColor(ContextCompat.getColor(holder.txtGrievanceStatus.getContext(), R.color.requested));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                  holder.totalLayout.setForeground(ContextCompat.getDrawable(context, R.drawable.shadow_foreground));
//            }
//        }
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
        return reportConstituentArrayList.size();
    }
}
