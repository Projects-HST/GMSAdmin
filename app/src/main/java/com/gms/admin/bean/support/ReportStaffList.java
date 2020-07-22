package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReportStaffList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("staff_report")
    @Expose
    private ArrayList<ReportStaff> reportStaffArrayList = new ArrayList<>();

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
     * @return The reportStaffArrayList
     */
    public ArrayList<ReportStaff> getReportStaffArrayList() {
        return reportStaffArrayList;
    }

    /**
     * @param reportStaffArrayList The reportStaffArrayList
     */
    public void setReportStaffArrayList(ArrayList<ReportStaff> reportStaffArrayList) {
        this.reportStaffArrayList = reportStaffArrayList;
    }
}