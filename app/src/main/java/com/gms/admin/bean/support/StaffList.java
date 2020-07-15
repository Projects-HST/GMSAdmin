package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StaffList {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("staff_details")
    @Expose
    private ArrayList<Staff> staffArrayList = new ArrayList<>();

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
     * @return The staffArrayList
     */
    public ArrayList<Staff> getStaffArrayList() {
        return staffArrayList;
    }

    /**
     * @param staffArrayList The staffArrayList
     */
    public void setStaffArrayList(ArrayList<Staff> staffArrayList) {
        this.staffArrayList = staffArrayList;
    }
}