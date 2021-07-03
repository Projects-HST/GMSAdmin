package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportMeetings implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("paguthi_name")
    @Expose
    private String paguthi_name;

    @SerializedName("meeting_date")
    @Expose
    private String meeting_date;

    @SerializedName("created_by")
    @Expose
    private String created_by;

    @SerializedName("meeting_title")
    @Expose
    private String meeting_title;

    @SerializedName("meeting_status")
    @Expose
    private String meeting_status;

    @SerializedName("father_husband_name")
    @Expose
    private String fatherHubandName;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("pin_code")
    @Expose
    private String pincode;

    @SerializedName("door_no")
    @Expose
    private String doorNo;

    @SerializedName("address")
    @Expose
    private String address;

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
    public String getfull_name() {
        return full_name;
    }

    /**
     * @param full_name The full_name
     */
    public void setfull_name(String full_name) {
        this.full_name = full_name;
    }

    /**
     * @return The paguthi_name
     */
    public String getpaguthi_name() {
        return paguthi_name;
    }

    /**
     * @param paguthi_name The paguthi_name
     */
    public void setpaguthi_name(String paguthi_name) {
        this.paguthi_name = paguthi_name;
    }

    /**
     * @return The meeting_date
     */
    public String getmeeting_date() {
        return meeting_date;
    }

    /**
     * @param meeting_date The meeting_date
     */
    public void setmeeting_date(String meeting_date) {
        this.meeting_date = meeting_date;
    }

    /**
     * @return The created_by
     */
    public String getcreated_by() {
        return created_by;
    }

    /**
     * @param created_by The created_by
     */
    public void setcreated_by(String created_by) {
        this.created_by = created_by;
    }

    /**
     * @return The meeting_title
     */
    public String getmeeting_title() {
        return meeting_title;
    }

    /**
     * @param meeting_title The meeting_title
     */
    public void setmeeting_title(String meeting_title) {
        this.meeting_title = meeting_title;
    }

    /**
     * @return The meeting_status
     */
    public String getmeeting_status() {
        return meeting_status;
    }

    /**
     * @param meeting_status The meeting_status
     */
    public void setmeeting_status(String meeting_status) {
        this.meeting_status = meeting_status;
    }

    public String getFatherHubandName() {
        return fatherHubandName;
    }

    public void setFatherHubandName(String fatherHubandName) {
        this.fatherHubandName = fatherHubandName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
