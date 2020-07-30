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
import com.gms.admin.bean.support.Grievance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.MyViewHolder> {

    private ArrayList<Grievance> GrievancesList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtPetitionEnquiryNo, txtUser, txtdate, txtSeekerType, txtGrievanceName, txtGrievanceSubCategory, txtGrievanceStatus;
        public LinearLayout grievanceLayout;

        public MyViewHolder(View view) {
            super(view);
            grievanceLayout = (LinearLayout) view.findViewById(R.id.grievance_layout);
            grievanceLayout.setOnClickListener(this);
            txtPetitionEnquiryNo = (TextView) view.findViewById(R.id.petition_enquiry_number);
            txtUser = (TextView) view.findViewById(R.id.full_name);
            txtdate = (TextView) view.findViewById(R.id.grievance_date);
            txtSeekerType = (TextView) view.findViewById(R.id.seeker_type);
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

    public SampleAdapter(ArrayList<Grievance> GrievancesList, OnItemClickListener onItemClickListener) {
        this.GrievancesList = GrievancesList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_grievance_sample, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Grievance Grievance = GrievancesList.get(position);
        if (Grievance.getgrievance_type().equalsIgnoreCase("P")) {
            holder.txtPetitionEnquiryNo.setText("Petition Number - " + Grievance.getpetition_enquiry_no());
        } else{
            holder.txtPetitionEnquiryNo.setText("Enquiry Number - " + Grievance.getpetition_enquiry_no());
        }

        holder.txtSeekerType.setText(capitalizeString(Grievance.getseeker_info()));
        holder.txtdate.setText(getserverdateformat(Grievance.getgrievance_date()));
        holder.txtUser.setText(capitalizeString(Grievance.getFull_name()));
        holder.txtGrievanceName.setText(capitalizeString(Grievance.getgrievance_name()));
        holder.txtGrievanceStatus.setText(capitalizeString(Grievance.getstatus()));
        holder.txtGrievanceSubCategory.setText(capitalizeString(Grievance.getSub_category_name()));

        if (Grievance.getstatus().equalsIgnoreCase("COMPLETED")) {
            holder.txtGrievanceStatus.setBackground(ContextCompat.getDrawable(holder.txtGrievanceStatus.getContext(), R.drawable.btn_round_completed));
        } else {
            holder.txtGrievanceStatus.setBackground(ContextCompat.getDrawable(holder.txtGrievanceStatus.getContext(), R.drawable.btn_round_processing));
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