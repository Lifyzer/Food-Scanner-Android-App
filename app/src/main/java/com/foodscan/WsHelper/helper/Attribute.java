package com.foodscan.WsHelper.helper;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by c166 on 04/11/15.
 * Class to pass attribute value of API.
 * add parameters according to request.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class Attribute {

    public Attribute() {
    }

    @JsonProperty("email_id")
    private String email_id;

    @JsonProperty("password")
    private String password;

    @JsonProperty("device_type")
    private String device_type;

    @JsonProperty("secret_key")
    private String secret_key;

    @JsonProperty("access_key")
    private String access_key;

    @JsonProperty("first_name")
    private String first_name;

    @JsonProperty("last_name")
    private String last_name;

    @JsonProperty("user_id")
    private String user_id;

    @JsonProperty("product_name")
    private String product_name;

    @JsonProperty("to_index")
    private String to_index;

    @JsonProperty("from_index")
    private String from_index;



    // *************************************//
    // *********  getter setter  *********** //
    // ************************************ //

    @JsonProperty("email_id")
    public String getEmail_id() {
        return email_id;
    }

    @JsonProperty("email_id")
    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("device_type")
    public String getDevice_type() {
        return device_type;
    }

    @JsonProperty("device_type")
    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    @JsonProperty("secret_key")
    public String getSecret_key() {
        return secret_key;
    }

    @JsonProperty("secret_key")
    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    @JsonProperty("access_key")
    public String getAccess_key() {
        return access_key;
    }

    @JsonProperty("access_key")
    public void setAccess_key(String access_key) {
        this.access_key = access_key;
    }

    @JsonProperty("first_name")
    public String getFirst_name() {
        return first_name;
    }

    @JsonProperty("first_name")
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    @JsonProperty("last_name")
    public String getLast_name() {
        return last_name;
    }

    @JsonProperty("last_name")
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @JsonProperty("user_id")
    public String getUser_id() {
        return user_id;
    }

    @JsonProperty("user_id")
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @JsonProperty("product_name")
    public String getProduct_name() {
        return product_name;
    }

    @JsonProperty("product_name")
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    @JsonProperty("to_index")
    public String getTo_index() {
        return to_index;
    }

    @JsonProperty("to_index")
    public void setTo_index(String to_index) {
        this.to_index = to_index;
    }

    @JsonProperty("from_index")
    public String getFrom_index() {
        return from_index;
    }

    @JsonProperty("from_index")
    public void setFrom_index(String from_index) {
        this.from_index = from_index;
    }



}

