package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportStaff implements Serializable {
    
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("active")
    @Expose
    private String active;

    @SerializedName("inactive")
    @Expose
    private String inactive;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    /**
     * @return The id
     */
    public String getid() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setid(String id) {
        this.id = id;
    }

    /**
     * @return The total
     */
    public String gettotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void settotal(String total) {
        this.total = total;
    }

    /**
     * @return The active
     */
    public String getactive() {
        return active;
    }

    /**
     * @param active The active
     */
    public void setactive(String active) {
        this.active = active;
    }

    /**
     * @return The inactive
     */
    public String getinactive() {
        return inactive;
    }

    /**
     * @param inactive The inactive
     */
    public void setinactive(String inactive) {
        this.inactive = inactive;
    }

    /**
     * @return The full_name
     */
    public String getFull_name() {
        return full_name;
    }

    /**
     * @param full_name The full_name
     */
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

}