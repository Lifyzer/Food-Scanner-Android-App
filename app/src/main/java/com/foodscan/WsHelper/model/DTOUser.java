package com.foodscan.WsHelper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by c157 on 25/01/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@RealmClass
public class DTOUser extends RealmObject {

    @PrimaryKey
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("password")
    private String password;
    @JsonProperty("facebook_id")
    private String facebookId;
    @JsonProperty("user_image")
    private String userImage;
    @JsonProperty("device_token")
    private String deviceToken;
    @JsonProperty("device_type")
    private String deviceType;
    @JsonProperty("created_date")
    private String createdDate;
    @JsonProperty("modified_date")
    private String modifiedDate;
    @JsonProperty("is_delete")
    private String isDelete;
    @JsonProperty("is_test")
    private String isTest;
    @JsonProperty("guid")
    private String guid;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("facebook_id")
    public String getFacebookId() {
        return facebookId;
    }

    @JsonProperty("facebook_id")
    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    @JsonProperty("user_image")
    public String getUserImage() {
        return userImage;
    }

    @JsonProperty("user_image")
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    @JsonProperty("device_token")
    public String getDeviceToken() {
        return deviceToken;
    }

    @JsonProperty("device_token")
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @JsonProperty("device_type")
    public String getDeviceType() {
        return deviceType;
    }

    @JsonProperty("device_type")
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @JsonProperty("created_date")
    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("created_date")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty("modified_date")
    public String getModifiedDate() {
        return modifiedDate;
    }

    @JsonProperty("modified_date")
    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @JsonProperty("is_delete")
    public String getIsDelete() {
        return isDelete;
    }

    @JsonProperty("is_delete")
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    @JsonProperty("is_test")
    public String getIsTest() {
        return isTest;
    }

    @JsonProperty("is_test")
    public void setIsTest(String isTest) {
        this.isTest = isTest;
    }

    @JsonProperty("guid")
    public String getGuid() {
        return guid;
    }

    @JsonProperty("guid")
    public void setGuid(String guid) {
        this.guid = guid;
    }

}
