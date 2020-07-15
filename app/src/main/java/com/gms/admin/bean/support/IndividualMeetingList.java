package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class IndividualMeetingList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("meeting_details")
    @Expose
    private ArrayList<IndividualMeeting> meetingArrayList = new ArrayList<>();

    /**
     * @return The count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return The meetingArrayList
     */
    public ArrayList<IndividualMeeting> getMeetingArrayList() {
        return meetingArrayList;
    }

    /**
     * @param meetingArrayList The meetingArrayList
     */
    public void setMeetingArrayList(ArrayList<IndividualMeeting> meetingArrayList) {
        this.meetingArrayList = meetingArrayList;
    }
}