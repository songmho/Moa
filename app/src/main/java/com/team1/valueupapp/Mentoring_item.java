package com.team1.valueupapp;

/**
 * Created by songmho on 15. 8. 4.
 */
public class Mentoring_item {
    String job;
    int year;
    int month;
    int day;
    String title;
    String mentor;
    String venue;
    String detail;

    Mentoring_item(String job, int year, int month, int day, String title, String mentor, String venue,String detail){
        this.job=job;
        this.year=year;
        this.month=month;
        this.day=day;
        this.title=title;
        this.mentor=mentor;
        this.venue=venue;
        this.detail=detail;
    }

    String getJob(){return this.job;}
    int getYear(){return this.year;}
    int getMonth(){return this.month;}
    int getDay(){return this.day;}
    String getTitle(){return this.title;}
    String getMentor(){return this.mentor;}
    String getVenue(){return this.venue;}

    public String getDetail() {
        return detail;
    }
}
