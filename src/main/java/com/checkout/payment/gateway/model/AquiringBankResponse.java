package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AquiringBankResponse {

    private Boolean authorized;

    @JsonProperty("authorization_code")
    private String authorizationCode;

    public AquiringBankResponse() {}

    public AquiringBankResponse(String authorizationCode, Boolean authorized) {
        this.authorizationCode = authorizationCode;
        this.authorized = authorized;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }


}
