package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportStaff implements Serializable {
    
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("total_cons")
    @Expose
    private String total_cons;

    @SerializedName("total_v")
    @Expose
    private String total_v;

    @SerializedName("total_g")
    @Expose
    private String total_g;

    @SerializedName("total_broadcast")
    @Expose
    private String total_broadcast;

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

    public String getTotal_cons() {
        return total_cons;
    }

    public void setTotal_cons(String total_cons) {
        this.total_cons = total_cons;
    }

    public String getTotal_v() {
        return total_v;
    }

    public void setTotal_v(String total_v) {
        this.total_v = total_v;
    }

    public String getTotal_g() {
        return total_g;
    }

    public void setTotal_g(String total_g) {
        this.total_g = total_g;
    }

    public String getTotal_broadcast() {
        return total_broadcast;
    }

    public void setTotal_broadcast(String total_broadcast) {
        this.total_broadcast = total_broadcast;
    }
}