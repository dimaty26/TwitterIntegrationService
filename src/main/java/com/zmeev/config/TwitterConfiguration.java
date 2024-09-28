package com.zmeev.config;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.pkce.PKCE;
import com.github.scribejava.core.pkce.PKCECodeChallengeMethod;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.auth.TwitterOAuth20Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;

public class TwitterConfiguration {

    private final TwitterCredentialsOAuth2 credentials;

    public TwitterConfiguration() {
        String clientId = getProperty("client.id");
        String clientSecret = getProperty("client.secret");
        credentials = new TwitterCredentialsOAuth2(clientId, clientSecret, "", "");
        OAuth2AccessToken accessToken = getAccessToken(credentials);
        if (accessToken != null) {
            credentials.setTwitterOauth2AccessToken(accessToken.getAccessToken());
            credentials.setTwitterOauth2RefreshToken(accessToken.getRefreshToken());
        }
    }

    public TwitterCredentialsOAuth2 getCredentials() {
        return credentials;
    }

    private String getProperty(String propertyName) {
        Properties prop = new Properties();
        try {
            prop.load(TwitterConfiguration.class.getClassLoader().getResourceAsStream("application.properties"));
            return prop.getProperty(propertyName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load client id", e);
        }
    }

    private OAuth2AccessToken getAccessToken(TwitterCredentialsOAuth2 credentials) {

        OAuth2AccessToken accessToken = null;
        try (TwitterOAuth20Service service = new TwitterOAuth20Service(credentials.getTwitterOauth2ClientId(),
                credentials.getTwitterOAuth2ClientSecret(),
                "https://localhost:8080",
                "offline.access tweet.read users.read tweet.write")) {
            final Scanner in = new Scanner(System.in, StandardCharsets.UTF_8);
            System.out.println("Fetching the Authorization URL...");

            final String secretState = "state";
            PKCE pkce = new PKCE();
            pkce.setCodeChallenge("challenge");
            pkce.setCodeChallengeMethod(PKCECodeChallengeMethod.PLAIN);
            pkce.setCodeVerifier("challenge");
            String authorizationUrl = service.getAuthorizationUrl(pkce, secretState);

            System.out.println("Go to the Authorization URL and authorize your App:\n" +
                    authorizationUrl + "\nAfter that paste the authorization code here\n>>");
            final String code = in.nextLine();
            System.out.println("\nTrading the Authorization Code for an Access Token...");
            accessToken = service.getAccessToken(pkce, code);

            System.out.println("Access token: " + accessToken.getAccessToken());
            System.out.println("Refresh token: " + accessToken.getRefreshToken());
        } catch (Exception e) {
            System.err.println("Error while getting the access token:\n " + e);
        }
        return accessToken;
    }
}
