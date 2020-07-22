package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchResultReportList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("report_status")
    @Expose
    private ArrayList<ReportGrievance> reportGrievanceArrayList = new ArrayList<>();

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
     * @return The reportGrievanceArrayList
     */
    public ArrayList<ReportGrievance> getReportGrievanceArrayList() {
        return reportGrievanceArrayList;
    }

    /**
     * @param reportGrievanceArrayList The reportGrievanceArrayList
     */
    public void setReportGrievanceArrayList(ArrayList<ReportGrievance> reportGrievanceArrayList) {
        this.reportGrievanceArrayList = reportGrievanceArrayList;
    }
}