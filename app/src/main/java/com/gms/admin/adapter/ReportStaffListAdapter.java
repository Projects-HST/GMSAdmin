package com.gms.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gms.admin.R;
import com.gms.admin.bean.support.Grievance;
import com.gms.admin.bean.support.ReportStaff;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportStaffListAdapter extends RecyclerView.Adapter<ReportStaffListAdapter.MyViewHolder> implements Filterable {

    private ArrayList<ReportStaff> reportStaffArrayList;
    private ArrayList<ReportStaff> reportStaffArrayListFiltered;
    private ArrayList<ReportStaff> og;
    Context context;
    private OnItemClickListener onItemClickListener;

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    reportStaffArrayListFiltered = og;
                    //filteredCUG = CUG;
                } else {
                    ArrayList<ReportStaff> filtered = new ArrayList<>();
                    for (ReportStaff cug : reportStaffArrayList) {
                        if( cug.getFull_name().toLowerCase().contains(charString)){
                            filtered.add(cug);
                        }
                    }
                    reportStaffArrayListFiltered = filtered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = reportStaffArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //filteredCUG.clear();
                reportStaffArrayList = (ArrayList<ReportStaff>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtStaffName, txtTotalCount, txtActiveCount, txtInactiveCount;
        public LinearLayout staffLayout;

        public MyViewHolder(View view) {
            super(view);
            staffLayout = (LinearLayout) view.findViewById(R.id.staff_layout);
            staffLayout.setOnClickListener(this);
            txtStaffName = (TextView) view.findViewById(R.id.staff_name);
            txtTotalCount = (TextView) view.findViewById(R.id.total_count);
            txtActiveCount = (TextView) view.findViewById(R.id.active_count);
            txtInactiveCount = (TextView) view.findViewById(R.id.inactive_count);
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

    public ReportStaffListAdapter(ArrayList<ReportStaff> reportStaffArrayList, ReportStaffListAdapter.OnItemClickListener onItemClickListener) {
        this.reportStaffArrayList = reportStaffArrayList;
        this.og = reportStaffArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ReportStaffListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_report_staff, parent, false);

        return new ReportStaffListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportStaffListAdapter.MyViewHolder holder, int position) {
        ReportStaff Grievance = reportStaffArrayList.get(position);

        holder.txtStaffName.setText(capitalizeString(Grievance.getFull_name()));
        holder.txtTotalCount.setText((Grievance.gettotal()));
        holder.txtActiveCount.setText((Grievance.getactive()));
        holder.txtInactiveCount.setText((Grievance.getinactive()));
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
        return reportStaffArrayListFiltered.size();
    }
}