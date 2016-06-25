package com.team1.valueupapp.item;

/**
 * Created by songmho on 2016-05-28.
 */
public class MessageItem {
    String title;
    String title_tag;
    String body;
    String time;


    public String getTitle() {
        return title;
    }

    public String getTitle_tag() {
        return title_tag;
    }

    public String getBody() {
        return body;
    }

    public String getTime() {
        return time;
    }


    public MessageItem(String title_tag, String title,  String body, String time){
        this.title_tag =title_tag;
        this.title = title;
        this.body = body;
        this.time = time;
    }
}
