package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IndividualMeeting implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("constituent_id")
    @Expose
    private String constituent_id;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("meeting_date")
    @Expose
    private String meeting_date;

    @SerializedName("meeting_detail")
    @Expose
    private String meeting_detail;

    @SerializedName("meeting_title")
    @Expose
    private String meeting_title;

    @SerializedName("meeting_status")
    @Expose
    private String meeting_status;

    @SerializedName("created_user")
    @Expose
    private String created_user;

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
     * @return The constituent_id
     */
    public String getconstituent_id() {
        return constituent_id;
    }

    /**
     * @param constituent_id The constituent_id
     */
    public void setconstituent_id(String constituent_id) {
        this.constituent_id = constituent_id;
    }

    /**
     * @return The created_at
     */
    public String getcreated_at() {
        return created_at;
    }

    /**
     * @param created_at The created_at
     */
    public void setcreated_at(String created_at) {
        this.created_at = created_at;
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
     * @return The meeting_detail
     */
    public String getmeeting_detail() {
        return meeting_detail;
    }

    /**
     * @param meeting_detail The meeting_detail
     */
    public void setmeeting_detail(String meeting_detail) {
        this.meeting_detail = meeting_detail;
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


    public String getCreated_user() {
        return created_user;
    }

    public void setCreated_user(String created_user) {
        this.created_user = created_user;
    }
}