package com.gms.admin.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gms.admin.R;
import com.gms.admin.bean.support.Grievance;

import java.util.ArrayList;

public class GrievanceListAdapter extends BaseAdapter {

    //    private final Transformation transformation;
    private Context context;
    private ArrayList<Grievance> grievances;
    private boolean mSearching = false;
    private boolean mAnimateSearch = false;
    Boolean click = false;
    private ArrayList<Integer> mValidSearchIndices = new ArrayList<Integer>();


    public GrievanceListAdapter(Context context, ArrayList<Grievance> grievances) {
        this.context = context;
        this.grievances = grievances;
//        Collections.reverse(services);
//        transformation = new RoundedTransformationBuilder()
//                .cornerRadiusDp(0)
//                .oval(false)
//                .build();
        mSearching = false;
    }

    @Override
    public int getCount() {
        if (mSearching) {
            if (!mAnimateSearch) {
                mAnimateSearch = true;
            }
            return mValidSearchIndices.size();
        } else {
            return grievances.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearching) {
            return grievances.get(mValidSearchIndices.get(position));
        } else {
            return grievances.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GrievanceListAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_grievance, parent, false);
            holder = new GrievanceListAdapter.ViewHolder();
            holder.txtPetitionEnquiryNo = (TextView) convertView.findViewById(R.id.petition_enquiry_number);
            holder.txtSeekerType = (TextView) convertView.findViewById(R.id.seeker_type);
            holder.txtGrievanceName = (TextView) convertView.findViewById(R.id.grievance_name);
            holder.txtGrievanceSubCategory = (TextView) convertView.findViewById(R.id.grievance_sub_category);
            holder.txtGrievanceStatus = (TextView) convertView.findViewById(R.id.grievance_status);
            convertView.setTag(holder);
        } else {
            holder = (GrievanceListAdapter.ViewHolder) convertView.getTag();
            holder.txtPetitionEnquiryNo = (TextView) convertView.findViewById(R.id.petition_enquiry_number);
            holder.txtSeekerType = (TextView) convertView.findViewById(R.id.seeker_type);
            holder.txtGrievanceName = (TextView) convertView.findViewById(R.id.grievance_name);
            holder.txtGrievanceSubCategory = (TextView) convertView.findViewById(R.id.grievance_sub_category);
            holder.txtGrievanceStatus = (TextView) convertView.findViewById(R.id.grievance_status);
        }
        if (grievances.get(position).getgrievance_type().equalsIgnoreCase("P")) {
            holder.txtPetitionEnquiryNo.setText("Petition Number - " + grievances.get(position).getpetition_enquiry_no());
        } else{
            holder.txtPetitionEnquiryNo.setText("Enquiry Number - " + grievances.get(position).getpetition_enquiry_no());
        }

        holder.txtSeekerType.setText(grievances.get(position).getseeker_info());
        holder.txtGrievanceName.setText(grievances.get(position).getgrievance_name());
        holder.txtGrievanceStatus.setText(grievances.get(position).getstatus());
        holder.txtGrievanceSubCategory.setText(grievances.get(position).getSub_category_name());

        if (grievances.get(position).getstatus().equalsIgnoreCase("COMPLETED")) {
            holder.txtGrievanceStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.completed_grievance));
        } else {
            holder.txtGrievanceStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.requested));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.totalLayout.setForeground(ContextCompat.getDrawable(context, R.drawable.shadow_foreground));
//            }
        }


        if (mSearching) {
            position = mValidSearchIndices.get(position);

        } else {
            Log.d("Event List Adapter", "getview pos called" + position);
        }

        return convertView;
    }

    public void startSearch(String eventName) {
        mSearching = true;
        mAnimateSearch = false;
        Log.d("EventListAdapter", "serach for event" + eventName);
        mValidSearchIndices.clear();
        for (int i = 0; i < grievances.size(); i++) {
            String homeWorkTitle = grievances.get(i).getid();
            if ((homeWorkTitle != null) && !(homeWorkTitle.isEmpty())) {
                if (homeWorkTitle.toLowerCase().contains(eventName.toLowerCase())) {
                    mValidSearchIndices.add(i);
                }
            }
        }
        Log.d("Event List Adapter", "notify" + mValidSearchIndices.size());
    }

    public void exitSearch() {
        mSearching = false;
        mValidSearchIndices.clear();
        mAnimateSearch = false;
    }

    public void clearSearchFlag() {
        mSearching = false;
    }

    public class ViewHolder {
        public TextView txtPetitionEnquiryNo, txtSeekerType, txtGrievanceName, txtGrievanceSubCategory, txtGrievanceStatus;
    }

    public boolean ismSearching() {
        return mSearching;
    }

    public int getActualEventPos(int selectedSearchpos) {
        if (selectedSearchpos < mValidSearchIndices.size()) {
            return mValidSearchIndices.get(selectedSearchpos);
        } else {
            return 0;
        }
    }

    private void getStat(String stat) {

    }

}