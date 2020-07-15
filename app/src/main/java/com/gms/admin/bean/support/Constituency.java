package com.gms.admin.bean.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Constituency implements Serializable {
    
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("party_id")
    @Expose
    private String party_id;
    
    @SerializedName("constituency_id")
    @Expose
    private String constituency_id;

    @SerializedName("constituency_name")
    @Expose
    private String constituency_name;

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
     * @return The party_id
     */
    public String getparty_id() {
        return party_id;
    }

    /**
     * @param party_id The party_id
     */
    public void setparty_id(String party_id) {
        this.party_id = party_id;
    }

    /**
     * @return The constituency_id
     */
    public String getconstituency_id() {
        return constituency_id;
    }

    /**
     * @param constituency_id The constituency_id
     */
    public void setconstituency_id(String constituency_id) {
        this.constituency_id = constituency_id;
    }


    /**
     * @return The constituency_name
     */
    public String getconstituency_name() {
        return constituency_name;
    }

    /**
     * @param constituency_name The constituency_name
     */
    public void setconstituency_name(String constituency_name) {
        this.constituency_name = constituency_name;
    }

}