package com.gms.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gms.admin.R;
import com.gms.admin.bean.support.Grievance;
import com.gms.admin.bean.support.Grievance;
import com.gms.admin.bean.support.IndividualMeeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GrievanceListAdapter extends RecyclerView.Adapter<GrievanceListAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Grievance> grievancesList;
    private ArrayList<Grievance> grievancesListFiltered;
    private ArrayList<Grievance> og;
    Context context;
    private OnItemClickListener onItemClickListener;

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    grievancesListFiltered = og;
                    //filteredCUG = CUG;
                } else {
                    ArrayList<Grievance> filtered = new ArrayList<>();
                    for (Grievance cug : grievancesList) {
                        if( cug.getgrievance_name().toLowerCase().contains(charString) || cug.getpetition_enquiry_no().toLowerCase().contains(charString)
                                || cug.getgrievance_date().toLowerCase().contains(charString)|| cug.getSub_category_name().toLowerCase().contains(charString)){
                            filtered.add(cug);
                        }
                    }
                    grievancesListFiltered = filtered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = grievancesListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //filteredCUG.clear();
                grievancesList = (ArrayList<Grievance>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtPetitionEnquiryNo, txtUser, txtdate, txtSeekerType, txtGrievanceName, txtGrievanceSubCategory, txtGrievanceStatus;
        public LinearLayout grievanceLayout;

        public MyViewHolder(View view) {
            super(view);
            grievanceLayout = (LinearLayout) view.findViewById(R.id.grievance_layout);
            grievanceLayout.setOnClickListener(this);
            txtPetitionEnquiryNo = (TextView) view.findViewById(R.id.petition_enquiry_number);
            txtUser = (TextView) view.findViewById(R.id.full_name);
            txtUser.setVisibility(View.GONE);
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

    public GrievanceListAdapter(ArrayList<Grievance> GrievancesList, OnItemClickListener onItemClickListener) {
        this.grievancesList = GrievancesList;
        this.grievancesListFiltered = GrievancesList;
        this.og = GrievancesList;
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
        Grievance grievance = grievancesList.get(position);
        if (grievance.getgrievance_type().equalsIgnoreCase("P")) {
            holder.txtPetitionEnquiryNo.setText("Petition Number - " + grievance.getpetition_enquiry_no());
        } else{
            holder.txtPetitionEnquiryNo.setText("Enquiry Number - " + grievance.getpetition_enquiry_no());
        }

        holder.txtSeekerType.setText(capitalizeString(grievance.getseeker_info()));
        holder.txtdate.setText(getserverdateformat(grievance.getgrievance_date()));
        holder.txtGrievanceName.setText(capitalizeString(grievance.getgrievance_name()));
        holder.txtGrievanceStatus.setText(capitalizeString(grievance.getstatus()));
        holder.txtGrievanceSubCategory.setText(capitalizeString(grievance.getSub_category_name()));
        if (grievance.getstatus().equalsIgnoreCase("COMPLETED")) {
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
        return grievancesListFiltered.size();
    }
}