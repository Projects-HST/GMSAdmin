package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReportFestivalWishesList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("festival_report")
    @Expose
    private ArrayList<ReportWishes> reportWishesArrayList = new ArrayList<>();

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
    public ArrayList<ReportWishes> getReportWishesArrayList() {
        return reportWishesArrayList;
    }

    /**
     * @param reportGrievanceArrayList The reportGrievanceArrayList
     */
    public void setReportWishesArrayList(ArrayList<ReportWishes> reportGrievanceArrayList) {
        this.reportWishesArrayList = reportGrievanceArrayList;
    }
}
