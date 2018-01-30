package com.foodscan.WsHelper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by c157 on 25/01/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@RealmClass
public class DTOLoginData extends RealmObject {

    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("User")
    private RealmList<DTOUser> user = null;
    @JsonProperty("UserToken")
    private String userToken;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("User")
    public RealmList<DTOUser> getUser() {
        return user;
    }

    @JsonProperty("User")
    public void setUser(RealmList<DTOUser> user) {
        this.user = user;
    }

    @JsonProperty("UserToken")
    public String getUserToken() {
        return userToken;
    }

    @JsonProperty("UserToken")
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

}
