package com.gms.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gms.admin.R;
import com.gms.admin.bean.support.ReportWishes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportWishesListAdapter extends RecyclerView.Adapter<ReportWishesListAdapter.MyViewHolder>{

    private ArrayList<ReportWishes> wishesArrayList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtSurname, txtUser, txtdate, txtMobileNumber, txtGrievanceName, txtAddress, txtWishesStatus;
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
            txtWishesStatus = (TextView) view.findViewById(R.id.grievance_status);

        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemWishesClick(v, getAdapterPosition());
            }
//            else {
//                onClickListener.onClick(Selecttick);
//            }
        }

    }

    public interface OnItemClickListener {
        public void onItemWishesClick(View view, int position);
    }

    public ReportWishesListAdapter(ArrayList<ReportWishes> wishesArrayList, OnItemClickListener onItemClickListener) {
        this.wishesArrayList = wishesArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ReportWishesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_report_wishes, parent, false);

        return new ReportWishesListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportWishesListAdapter.MyViewHolder holder, int position) {
        ReportWishes reportWishes = wishesArrayList.get(position);

        holder.txtMobileNumber.setText((reportWishes.getMobile_no()));
        holder.txtdate.setText(getserverdateformat(reportWishes.getDob()));
        holder.txtUser.setText(capitalizeString(reportWishes.getFull_name()));
        holder.txtSurname.setText(capitalizeString(reportWishes.getFather_husband_name()));
        holder.txtWishesStatus.setText(capitalizeString(reportWishes.getSend_on()));
        holder.txtAddress.setText((capitalizeString(reportWishes.getDoor_no()) + (reportWishes.getAddress()) + (reportWishes.getPin_code())));

//        if (Grievance.getstatus().equalsIgnoreCase("COMPLETED")) {
//            holder.txtGrievanceStatus.setTextColor(ContextCompat.getColor(holder.txtGrievanceStatus.getContext(), R.color.completed_grievance));
//        } else {
//            holder.txtGrievanceStatus.setTextColor(ContextCompat.getColor(holder.txtGrievanceStatus.getContext(), R.color.requested));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.totalLayout.setForeground(ContextCompat.getDrawable(context, R.drawable.shadow_foreground));
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
        return wishesArrayList.size();
    }
}
