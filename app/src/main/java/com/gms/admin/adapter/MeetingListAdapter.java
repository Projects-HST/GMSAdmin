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
import com.gms.admin.utils.GMSValidator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.MyViewHolder> {

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


    public MeetingListAdapter(ArrayList<Meeting> meetingArrayList, OnItemClickListener onItemClickListener) {
        this.meetingArrayList = meetingArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    @Override
    public MeetingListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_meeting, parent, false);

        return new MeetingListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeetingListAdapter.MyViewHolder holder, int position) {
        Meeting meeting = meetingArrayList.get(position);

        if (GMSValidator.checkNullString(meeting.getfull_name())) {
            holder.txtConstituentName.setText(capitalizeString(meeting.getfull_name()));
        }
        if (GMSValidator.checkNullString(meeting.getpaguthi_name())){
            holder.txtPaguthi.setText(capitalizeString(meeting.getpaguthi_name()));
        }
        if (GMSValidator.checkNullString(meeting.getmeeting_title())){
            holder.txtMeetingTitle.setText(capitalizeString(meeting.getmeeting_title()));
        }
        if (GMSValidator.checkNullString(meeting.getmeeting_date())){
            holder.txtMeetingDate.setText(getserverdateformat(meeting.getmeeting_date()));
        }
        if (GMSValidator.checkNullString(meeting.getmeeting_status())){
            holder.txtMeetingStatus.setText(capitalizeString(meeting.getmeeting_status()));
        }
        if (GMSValidator.checkNullString(meeting.getcreated_by())){
            holder.txtCreatedBy.setText(("Created by - " + capitalizeString(meeting.getcreated_by())));
        }
        if (meeting.getmeeting_status().equalsIgnoreCase("COMPLETED")) {
            holder.txtMeetingStatus.setBackground(ContextCompat.getDrawable(holder.txtMeetingStatus.getContext(), R.drawable.btn_round_completed));
        } else {
            holder.txtMeetingStatus.setBackground(ContextCompat.getDrawable(holder.txtMeetingStatus.getContext(), R.drawable.btn_round_pending));
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