package com.exemplo.spotifydownloader.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpotifyTokenResponse {

    @JsonProperty("access_token") // Mapeia o campo "access_token" do JSON
    private String accessToken;

    @JsonProperty("token_type") // Mapeia o campo "token_type" do JSON
    private String tokenType;

    @JsonProperty("expires_in") // Mapeia o campo "expires_in" do JSON
    private int expiresIn;

    // Getters e Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}