package com.gms.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gms.admin.R;
import com.gms.admin.activity.StaffDetailsActivity;
import com.gms.admin.bean.support.Staff;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.GMSValidator;
import com.gms.admin.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Staff> staffArrayList;
    private ArrayList<Staff> staffArrayListFiltered;
    private ArrayList<Staff> og;
    Context mContext;
    int indexPos;
    private OnItemClickListener onItemClickListener;

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    staffArrayListFiltered = og;
                    //filteredCUG = CUG;
                } else {
                    ArrayList<Staff> filtered = new ArrayList<>();
                    for (Staff cug : staffArrayList) {
                        if( cug.getfull_name().toLowerCase().contains(charString)){
                            filtered.add(cug);
                        }
                    }
                    staffArrayListFiltered = filtered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = staffArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //filteredCUG.clear();
                staffArrayList = (ArrayList<Staff>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtstaffName, txtstaffMail, txtPaguthi, txtStatus;
        private ImageView userImage, btnNext;
        public RelativeLayout staffLay;
        public MyViewHolder(View view) {
            super(view);
            staffLay = (RelativeLayout) view.findViewById(R.id.staff_layout);
            staffLay.setOnClickListener(this);
            txtstaffName = (TextView) view.findViewById(R.id.staff_name);
            txtPaguthi = (TextView) view.findViewById(R.id.staff_paguthi);
            txtstaffMail = (TextView) view.findViewById(R.id.staff_mail);
//            txtStatus = (TextView) view.findViewById(R.id.staff_status);
            userImage = (ImageView) view.findViewById(R.id.staff_image);
            btnNext = (ImageView) view.findViewById(R.id.btn_nxt);
            btnNext.setOnClickListener(this);

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    indexPos = getAdapterPosition();
                    Staff staffList = staffArrayList.get(indexPos);
                    Intent staffIntent = new Intent(mContext, StaffDetailsActivity.class);
//                    Bundle bundle = new Bundle();
                    staffIntent.putExtra("meetingObj", staffList.getid());
                    mContext.startActivity(staffIntent);
                }
            });
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


    public StaffListAdapter(Context context, ArrayList<Staff> staffArrayList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.staffArrayList = staffArrayList;
        this.og = staffArrayList;
        this.staffArrayListFiltered = staffArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    @Override
    public StaffListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_staff, parent, false);

        return new StaffListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StaffListAdapter.MyViewHolder holder, int position) {
        Staff staff = staffArrayList.get(position);
        holder.txtstaffName.setText(capitalizeString(staff.getfull_name()));
        holder.txtPaguthi.setText(capitalizeString(staff.getpaguthi_name()));
        holder.txtstaffMail.setText(capitalizeString(staff.getemail_id()));
//        holder.txtStatus.setText(capitalizeString(staff.getStatus()));
//        if (staff.getStatus().equalsIgnoreCase("INACTIVE")) {
//            holder.staffStatus.setBackground(ContextCompat.getDrawable(holder.txtStatus.getContext(), R.drawable.inactive_dot));
//            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.txtStatus.getContext(), R.color.inactive));
//        } else {
//            holder.staffStatus.setBackground(ContextCompat.getDrawable(holder.txtStatus.getContext(), R.drawable.active_dot));
//            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.txtStatus.getContext(), R.color.active));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.totalLayout.setForeground(ContextCompat.getDrawable(context, R.drawable.shadow_foreground));
//            }
//        }

        String urrl = PreferenceStorage.getClientUrl(holder.userImage.getContext()) + GMSConstants.KEY_STAFFPIC_URL + staff.getprofile_picture();

        if (GMSValidator.checkNullString(staff.getprofile_picture())) {
            Picasso.get().load(urrl).into(holder.userImage);
        } else {
            holder.userImage.setImageResource(R.drawable.ic_profile);
        }

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
        return staffArrayListFiltered.size();
    }
}
