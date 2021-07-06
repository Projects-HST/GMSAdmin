package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReportVideoList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("video_report")
    @Expose
    private ArrayList<ReportConstituent> reportConstituentArrayList = new ArrayList<>();

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
    public ArrayList<ReportConstituent> getReportConstituentArrayList() {
        return reportConstituentArrayList;
    }

    /**
     * @param reportGrievanceArrayList The reportGrievanceArrayList
     */
    public void setReportConstituentArrayList(ArrayList<ReportConstituent> reportGrievanceArrayList) {
        this.reportConstituentArrayList = reportGrievanceArrayList;
    }
}
