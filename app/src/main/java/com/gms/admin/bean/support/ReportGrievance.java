package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportGrievance implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("grievance_name")
    @Expose
    private String grievance_name;

    @SerializedName("petition_enquiry_no")
    @Expose
    private String petition_enquiry_no;

    @SerializedName("grievance_type")
    @Expose
    private String grievance_type;

    @SerializedName("grievance_date")
    @Expose
    private String grievance_date;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("created_by")
    @Expose
    private String created_by;

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    @SerializedName("role_name")
    @Expose
    private String role_name;

    @SerializedName("paguthi_name")
    @Expose
    private String paguthi_name;

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
     * @return The grievance_name
     */
    public String getgrievance_name() {
        return grievance_name;
    }

    /**
     * @param grievance_name The grievance_name
     */
    public void setgrievance_name(String grievance_name) {
        this.grievance_name = grievance_name;
    }

    /**
     * @return The mobile_no
     */
    public String getmobile_no() {
        return mobile_no;
    }

    /**
     * @param mobile_no The mobile_no
     */
    public void setmobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    /**
     * @return The petition_enquiry_no
     */
    public String getpetition_enquiry_no() {
        return petition_enquiry_no;
    }

    /**
     * @param petition_enquiry_no The petition_enquiry_no
     */
    public void setpetition_enquiry_no(String petition_enquiry_no) {
        this.petition_enquiry_no = petition_enquiry_no;
    }

    /**
     * @return The grievance_type
     */
    public String getgrievance_type() {
        return grievance_type;
    }

    /**
     * @param grievance_type The grievance_type
     */
    public void setgrievance_type(String grievance_type) {
        this.grievance_type = grievance_type;
    }

    /**
     * @return The grievance_date
     */
    public String getgrievance_date() {
        return grievance_date;
    }

    /**
     * @param grievance_date The grievance_date
     */
    public void setgrievance_date(String grievance_date) {
        this.grievance_date = grievance_date;
    }

    /**
     * @return The role_name
     */
    public String getrole_name() {
        return role_name;
    }

    /**
     * @param role_name The role_name
     */
    public void setrole_name(String role_name) {
        this.role_name = role_name;
    }

    /**
     * @return The description
     */
    public String getdescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setdescription(String description) {
        this.description = description;
    }

    /**
     * @return The status
     */
    public String getstatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setstatus(String status) {
        this.status = status;
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