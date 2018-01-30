package com.foodscan.WsHelper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by c157 on 25/01/18.
 */

public class DTORefreshTokenData {


    @JsonProperty("data")
    private DTOData data;
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;

    public DTORefreshTokenData() {
    }

    @JsonProperty("data")
    public DTOData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(DTOData data) {
        this.data = data;
    }

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

    //*************************************//
    //**********      DTO DATA     ********//
    //*************************************//

    public static class DTOData {

        @JsonProperty("tempToken")
        private String tempToken;
        @JsonProperty("adminConfig")
        private AdminConfig adminConfig;

        public DTOData() {
        }

        @JsonProperty("tempToken")
        public String getTempToken() {
            return tempToken;
        }

        @JsonProperty("tempToken")
        public void setTempToken(String tempToken) {
            this.tempToken = tempToken;
        }

        @JsonProperty("adminConfig")
        public AdminConfig getAdminConfig() {
            return adminConfig;
        }

        @JsonProperty("adminConfig")
        public void setAdminConfig(AdminConfig adminConfig) {
            this.adminConfig = adminConfig;
        }
    }

    //**********************************************//
    //***********  AdminConfig class ***************//
    //**********************************************//


    public static class AdminConfig {

        @JsonProperty("globalPassword")
        private String globalPassword;
        @JsonProperty("userAgent")
        private String userAgent;
        @JsonProperty("tempToken")
        private String tempToken;

        public AdminConfig() {
        }

        @JsonProperty("globalPassword")
        public String getGlobalPassword() {
            return globalPassword;
        }

        @JsonProperty("globalPassword")
        public void setGlobalPassword(String globalPassword) {
            this.globalPassword = globalPassword;
        }

        @JsonProperty("userAgent")
        public String getUserAgent() {
            return userAgent;
        }

        @JsonProperty("userAgent")
        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        @JsonProperty("tempToken")
        public String getTempToken() {
            return tempToken;
        }

        @JsonProperty("tempToken")
        public void setTempToken(String tempToken) {
            this.tempToken = tempToken;
        }

    }




    }
