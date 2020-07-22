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
import com.gms.admin.bean.support.Grievance;
import com.gms.admin.bean.support.ReportGrievance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportGrievanceListAdapter extends RecyclerView.Adapter<ReportGrievanceListAdapter.MyViewHolder> {

    private ArrayList<ReportGrievance> GrievancesList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtPetitionEnquiryNo, txtUser, txtdate, txtMobileNumber, txtGrievanceName, txtGrievanceSubCategory, txtGrievanceStatus;
        public LinearLayout grievanceLayout;

        public MyViewHolder(View view) {
            super(view);
            grievanceLayout = (LinearLayout) view.findViewById(R.id.grievance_layout);
            grievanceLayout.setOnClickListener(this);
            txtPetitionEnquiryNo = (TextView) view.findViewById(R.id.petition_enquiry_number);
            txtUser = (TextView) view.findViewById(R.id.full_name);
            txtdate = (TextView) view.findViewById(R.id.grievance_date);
            txtMobileNumber = (TextView) view.findViewById(R.id.mobile_number);
            txtGrievanceName = (TextView) view.findViewById(R.id.grievance_name);
            txtGrievanceSubCategory = (TextView) view.findViewById(R.id.grievance_sub_category);
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

    public ReportGrievanceListAdapter(ArrayList<ReportGrievance> GrievancesList, OnItemClickListener onItemClickListener) {
        this.GrievancesList = GrievancesList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ReportGrievanceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_report_grievance, parent, false);

        return new ReportGrievanceListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportGrievanceListAdapter.MyViewHolder holder, int position) {
        ReportGrievance Grievance = GrievancesList.get(position);
        if (Grievance.getgrievance_type().equalsIgnoreCase("P")) {
            holder.txtPetitionEnquiryNo.setText("Petition Number - " + Grievance.getpetition_enquiry_no());
        } else{
            holder.txtPetitionEnquiryNo.setText("Enquiry Number - " + Grievance.getpetition_enquiry_no());
        }

        holder.txtMobileNumber.setText(capitalizeString(Grievance.getmobile_no()));
        holder.txtdate.setText(getserverdateformat(Grievance.getgrievance_date()));
        holder.txtUser.setText(capitalizeString(Grievance.getFull_name()));
        holder.txtGrievanceName.setText(capitalizeString(Grievance.getgrievance_name()));
        holder.txtGrievanceStatus.setText(capitalizeString(Grievance.getstatus()));
        holder.txtGrievanceSubCategory.setText("Created by " + capitalizeString(Grievance.getcreated_by()) +"("+ capitalizeString(Grievance.getrole_name())+")");

        if (Grievance.getstatus().equalsIgnoreCase("COMPLETED")) {
            holder.txtGrievanceStatus.setTextColor(ContextCompat.getColor(holder.txtGrievanceStatus.getContext(), R.color.completed_grievance));
        } else {
            holder.txtGrievanceStatus.setTextColor(ContextCompat.getColor(holder.txtGrievanceStatus.getContext(), R.color.requested));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.totalLayout.setForeground(ContextCompat.getDrawable(context, R.drawable.shadow_foreground));
//            }
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
        return GrievancesList.size();
    }
}