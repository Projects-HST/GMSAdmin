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
import com.gms.admin.bean.support.Meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportMeetingListAdapter extends RecyclerView.Adapter<ReportMeetingListAdapter.MyViewHolder> {

    private ArrayList<Meeting> meetingArrayList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtConstituentName, txtPaguthi, txtMeetingTitle, txtCreatedBy, txtMeetingDate, txtMeetingStatus;
        public LinearLayout meetingLayout;
        public MyViewHolder(View view) {
            super(view);
            meetingLayout = (LinearLayout) view.findViewById(R.id.meeting_layout);
            meetingLayout.setOnClickListener(this);
            txtConstituentName = (TextView) view.findViewById(R.id.constituent_name);
            txtPaguthi = (TextView) view.findViewById(R.id.meeting_location);
            txtMeetingTitle = (TextView) view.findViewById(R.id.meeting_title);
            txtCreatedBy = (TextView) view.findViewById(R.id.meeting_created_by);
            txtMeetingDate = (TextView) view.findViewById(R.id.meeting_date);
            txtMeetingStatus = (TextView) view.findViewById(R.id.meeting_status);

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


    public ReportMeetingListAdapter(ArrayList<Meeting> meetingArrayList, ReportMeetingListAdapter.OnItemClickListener onItemClickListener) {
        this.meetingArrayList = meetingArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    @Override
    public ReportMeetingListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_meeting, parent, false);

        return new ReportMeetingListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportMeetingListAdapter.MyViewHolder holder, int position) {
        Meeting meeting = meetingArrayList.get(position);
        holder.txtConstituentName.setText(capitalizeString(meeting.getfull_name()));
        holder.txtPaguthi.setText(capitalizeString(meeting.getpaguthi_name()));
        holder.txtMeetingTitle.setText(capitalizeString(meeting.getmeeting_title()));
        holder.txtMeetingDate.setText(getserverdateformat(meeting.getmeeting_date()));
        holder.txtMeetingStatus.setText(capitalizeString(meeting.getmeeting_status()));
        holder.txtCreatedBy.setText("Created by " +capitalizeString(meeting.getcreated_by()));
        if (meeting.getmeeting_status().equalsIgnoreCase("COMPLETED")) {
            holder.txtMeetingStatus.setTextColor(ContextCompat.getColor(holder.txtMeetingStatus.getContext(), R.color.completed_meeting));
        } else {
            holder.txtMeetingStatus.setTextColor(ContextCompat.getColor(holder.txtMeetingStatus.getContext(), R.color.requested));
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
        return meetingArrayList.size();
    }
}