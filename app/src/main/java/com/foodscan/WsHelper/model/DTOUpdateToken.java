package com.foodscan.WsHelper.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DTOUpdateToken {


    @JsonProperty("GUID")
    private String gUID;
    @JsonProperty("masterKey")
    private String masterKey;
    @JsonProperty("acessKey")
    private String acessKey;
    @JsonProperty("UserToken")
    private String userToken;
    @JsonProperty("status")
    private String status;

    @JsonProperty("GUID")
    public String getGUID() {
        return gUID;
    }

    @JsonProperty("GUID")
    public void setGUID(String gUID) {
        this.gUID = gUID;
    }

    @JsonProperty("masterKey")
    public String getMasterKey() {
        return masterKey;
    }

    @JsonProperty("masterKey")
    public void setMasterKey(String masterKey) {
        this.masterKey = masterKey;
    }

    @JsonProperty("acessKey")
    public String getAcessKey() {
        return acessKey;
    }

    @JsonProperty("acessKey")
    public void setAcessKey(String acessKey) {
        this.acessKey = acessKey;
    }

    @JsonProperty("UserToken")
    public String getUserToken() {
        return userToken;
    }

    @JsonProperty("UserToken")
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

}
