package com.zmeev.service;

import com.twitter.clientlib.ApiClient;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.TweetCreateRequest;
import com.twitter.clientlib.model.TweetCreateResponse;
import com.zmeev.config.MaintainToken;
import com.zmeev.config.TwitterConfiguration;

public class TwitterService {

    TwitterConfiguration twitterConfiguration;
    TwitterApi twitterApi;

    public TwitterService() {
        twitterConfiguration = new TwitterConfiguration();
        TwitterCredentialsOAuth2 credentials = twitterConfiguration.getCredentials();

        ApiClient apiClient = new ApiClient();
        apiClient.setTwitterCredentials(credentials);
        twitterApi = new TwitterApi(apiClient);
        twitterApi.addCallback(new MaintainToken());
    }

    public void createNewPost(String postMessage) {
        try {
            TweetCreateRequest request = new TweetCreateRequest();
            request.setText(postMessage);

            TweetCreateResponse response = twitterApi.tweets().createTweet(request).execute();

            if (response.getData() != null) {
                System.out.println("Successfully posted tweet: " + response.getData().getText());
                System.out.println("Tweet ID: " + response.getData().getId());
            } else {
                System.err.println("Failed to post tweet. Error: " + response.getErrors());
            }

        } catch (ApiException e) {
            System.err.println("Error while posting the tweet: " + e.getResponseBody());
        }
    }
}
