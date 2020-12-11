package com.garbagecollectors.app.dto;

public class LoginResponse {

    private StringResponse stringResponse;
    private String jwt;

    public LoginResponse(){

    }

    public StringResponse getStringResponse() {
        return stringResponse;
    }

    public void setStringResponse(StringResponse stringResponse) {
        this.stringResponse = stringResponse;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
