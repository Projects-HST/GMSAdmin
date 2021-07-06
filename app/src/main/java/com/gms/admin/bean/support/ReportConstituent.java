package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportConstituent implements Serializable {


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("constituency_id")
    @Expose
    private String constituency_id;

    @SerializedName("paguthi_id")
    @Expose
    private String paguthi_id;

    @SerializedName("office_id")
    @Expose
    private String office_id;

    @SerializedName("ward_id")
    @Expose
    private String ward_id;

    @SerializedName("booth_address")
    @Expose
    private String booth_address;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("father_husband_name")
    @Expose
    private String father_husband_name;

    @SerializedName("guardian_name")
    @Expose
    private String guardian_name;

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    @SerializedName("mobile_otp")
    @Expose
    private String mobile_otp;

    @SerializedName("whatsapp_no")
    @Expose
    private String whatsapp_no;

    @SerializedName("whatsapp_broadcast")
    @Expose
    private String whatsapp_broadcast;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("door_no")
    @Expose
    private String door_no;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("pin_code")
    @Expose
    private String pin_code;

    @SerializedName("religion_id")
    @Expose
    private String religion_id;

    @SerializedName("email_id")
    @Expose
    private String email_id;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("voter_id_status")
    @Expose
    private String voter_id_status;

    @SerializedName("voter_status")
    @Expose
    private String voter_status;

    @SerializedName("voter_id_no")
    @Expose
    private String voter_id_no;

    @SerializedName("aadhaar_status")
    @Expose
    private String aadhaar_status;

    @SerializedName("aadhaar_no")
    @Expose
    private String aadhaar_no;

    @SerializedName("party_member_status")
    @Expose
    private String party_member_status;

    @SerializedName("volunteer_status")
    @Expose
    private String volunteer_status;

    @SerializedName("serial_no")
    @Expose
    private String serial_no;

    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("created_by")
    @Expose
    private String created_by;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("created_office_id")
    @Expose
    private String created_office_id;

    @SerializedName("updated_by")
    @Expose
    private String updated_by;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConstituency_id() {
        return constituency_id;
    }

    public void setConstituency_id(String constituency_id) {
        this.constituency_id = constituency_id;
    }

    public String getPaguthi_id() {
        return paguthi_id;
    }

    public void setPaguthi_id(String paguthi_id) {
        this.paguthi_id = paguthi_id;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getWard_id() {
        return ward_id;
    }

    public void setWard_id(String ward_id) {
        this.ward_id = ward_id;
    }

    public String getBooth_address() {
        return booth_address;
    }

    public void setBooth_address(String booth_address) {
        this.booth_address = booth_address;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getFather_husband_name() {
        return father_husband_name;
    }

    public void setFather_husband_name(String father_husband_name) {
        this.father_husband_name = father_husband_name;
    }

    public String getGuardian_name() {
        return guardian_name;
    }

    public void setGuardian_name(String guardian_name) {
        this.guardian_name = guardian_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getMobile_otp() {
        return mobile_otp;
    }

    public void setMobile_otp(String mobile_otp) {
        this.mobile_otp = mobile_otp;
    }

    public String getWhatsapp_no() {
        return whatsapp_no;
    }

    public void setWhatsapp_no(String whatsapp_no) {
        this.whatsapp_no = whatsapp_no;
    }

    public String getWhatsapp_broadcast() {
        return whatsapp_broadcast;
    }

    public void setWhatsapp_broadcast(String whatsapp_broadcast) {
        this.whatsapp_broadcast = whatsapp_broadcast;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDoor_no() {
        return door_no;
    }

    public void setDoor_no(String door_no) {
        this.door_no = door_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getReligion_id() {
        return religion_id;
    }

    public void setReligion_id(String religion_id) {
        this.religion_id = religion_id;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getVoter_id_status() {
        return voter_id_status;
    }

    public void setVoter_id_status(String voter_id_status) {
        this.voter_id_status = voter_id_status;
    }

    public String getVoter_status() {
        return voter_status;
    }

    public void setVoter_status(String voter_status) {
        this.voter_status = voter_status;
    }

    public String getVoter_id_no() {
        return voter_id_no;
    }

    public void setVoter_id_no(String voter_id_no) {
        this.voter_id_no = voter_id_no;
    }

    public String getAadhaar_status() {
        return aadhaar_status;
    }

    public void setAadhaar_status(String aadhaar_status) {
        this.aadhaar_status = aadhaar_status;
    }

    public String getAadhaar_no() {
        return aadhaar_no;
    }

    public void setAadhaar_no(String aadhaar_no) {
        this.aadhaar_no = aadhaar_no;
    }

    public String getParty_member_status() {
        return party_member_status;
    }

    public void setParty_member_status(String party_member_status) {
        this.party_member_status = party_member_status;
    }

    public String getVolunteer_status() {
        return volunteer_status;
    }

    public void setVolunteer_status(String volunteer_status) {
        this.volunteer_status = volunteer_status;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_office_id() {
        return created_office_id;
    }

    public void setCreated_office_id(String created_office_id) {
        this.created_office_id = created_office_id;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
