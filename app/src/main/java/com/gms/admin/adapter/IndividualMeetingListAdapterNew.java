package com.gms.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gms.admin.R;
import com.gms.admin.bean.support.IndividualMeeting;
import com.gms.admin.utils.PreferenceStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class IndividualMeetingListAdapterNew extends RecyclerView.Adapter<IndividualMeetingListAdapterNew.MyViewHolder> implements Filterable {

    private ArrayList<IndividualMeeting> meetingArrayList;
    private ArrayList<IndividualMeeting> og;
    private ArrayList<IndividualMeeting> meetingArrayListFiltered;
    Context context;
    private OnItemClickListener onItemClickListener;


    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    meetingArrayListFiltered = og;
                    //filteredCUG = CUG;
                } else {
                     ArrayList<IndividualMeeting> filtered = new ArrayList<>();
                    for (IndividualMeeting cug : meetingArrayList) {
                        if (cug.getmeeting_title().toLowerCase().contains(charString) || cug.getmeeting_date().toLowerCase().contains(charString)) {
                            filtered.add(cug);
                        }
                    }
                    meetingArrayListFiltered = filtered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = meetingArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //filteredCUG.clear();
                meetingArrayList = (ArrayList<IndividualMeeting>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtMeetingTitle, txtConstituencyName, txtMeetingDate, txtScheduleDate;
        private ImageView meetingImage, meetingDateImage;
        private LinearLayout meetingListLayout, scheduledLayout;
        private FrameLayout request;

        public MyViewHolder(View view) {
            super(view);
            meetingListLayout = (LinearLayout) view.findViewById(R.id.req_layout);
            scheduledLayout = (LinearLayout) view.findViewById(R.id.sch_layout);
            request = (FrameLayout) view.findViewById(R.id.meeting_lay_click);
            request.setOnClickListener(this);
            txtMeetingTitle = (TextView) view.findViewById(R.id.meeting_title);
            txtConstituencyName = (TextView) view.findViewById(R.id.constituency_name);
            txtMeetingDate = (TextView) view.findViewById(R.id.req_meeting_date);
            txtScheduleDate = (TextView) view.findViewById(R.id.schedule_date);

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


    public IndividualMeetingListAdapterNew(ArrayList<IndividualMeeting> meetingArrayList, Context ctx, IndividualMeetingListAdapterNew.OnItemClickListener onItemClickListener) {
        Collections.reverse(meetingArrayList);
        this.meetingArrayList = meetingArrayList;
        this.context = ctx;
        this.meetingArrayListFiltered = meetingArrayList;
        this.og = meetingArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    @Override
    public IndividualMeetingListAdapterNew.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_individual_meeting, parent, false);

        return new IndividualMeetingListAdapterNew.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IndividualMeetingListAdapterNew.MyViewHolder holder, int position) {
        IndividualMeeting meeting = meetingArrayList.get(position);

        if (meeting.getmeeting_status().equalsIgnoreCase("REQUESTED")) {
            holder.scheduledLayout.setVisibility(View.GONE);
            holder.txtMeetingTitle.setText(capitalizeString(meeting.getmeeting_title()));
            holder.txtConstituencyName.setText(capitalizeString(PreferenceStorage.getConstituencyName(context) + " ( " + "Paguthi" + " )"));
            holder.txtMeetingDate.setText(("Requested On" + " : " + meeting.getcreated_at()));
        }
        else {
            holder.scheduledLayout.setVisibility(View.VISIBLE);
            holder.txtMeetingTitle.setText(capitalizeString(meeting.getmeeting_title()));
            holder.txtConstituencyName.setText(capitalizeString(PreferenceStorage.getConstituencyName(context) + " ( " + "Paguthi" + " )"));
            holder.txtMeetingDate.setText(("Requested On" + " : " + meeting.getcreated_at()));
            holder.txtScheduleDate.setText(("Meeting Date" + " : " + meeting.getmeeting_date()));
        }
        if (position != 0) {
            if (checkdatapos(position)) {
//                holder.txtStatusTitle.setVisibility(View.GONE);
            } else {
//                holder.txtStatusTitle.setVisibility(View.VISIBLE);
            }
        } else {
//            holder.txtStatusTitle.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkdatapos(int position) {
        IndividualMeeting meeting = meetingArrayList.get(position);
        if (meeting.getmeeting_status().equalsIgnoreCase((meetingArrayList.get(position - 1).getmeeting_status()))) {
            return true;
        } else {
            return false;
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
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    @Override
    public int getItemCount() {
        return meetingArrayListFiltered.size();
    }
}