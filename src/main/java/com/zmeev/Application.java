package com.zmeev;

import com.zmeev.service.TwitterService;

/**
 * This is the startup application class
 */
public class Application {
    public static void main(String[] args) {
        TwitterService service = new TwitterService();
        service.createNewPost("Hello World. This is my first post.");
    }
}
