package com.team1.valueupapp.item;

/**
 * Created by songmho on 2016-05-28.
 */
public class MessageItem {
    String title;
    String body;
    String time;
    String status;

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getTime() {
        return time;
    }


    public MessageItem(String status, String title, String body, String time){
        this.status=status;
        this.title = title;
        this.body = body;
        this.time = time;
    }
}
