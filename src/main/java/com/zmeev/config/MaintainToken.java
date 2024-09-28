package com.zmeev.config;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.twitter.clientlib.ApiClientCallback;

public class MaintainToken implements ApiClientCallback {
    @Override
    public void onAfterRefreshToken(OAuth2AccessToken oAuth2AccessToken) {
        System.out.println("access: " + oAuth2AccessToken.getAccessToken());
        System.out.println("refresh: " + oAuth2AccessToken.getRefreshToken());
    }
}
