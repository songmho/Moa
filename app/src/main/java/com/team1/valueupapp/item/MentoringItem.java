package com.team1.valueupapp.item;

/**
 * Created by songmho on 15. 8. 4.
 */
public class MentoringItem {
    String job;
    int year;
    int month;
    int day;
    String title;
    String mentor;
    String venue;
    String detail;

    public MentoringItem(String job, int year, int month, int day, String title, String mentor, String venue, String detail) {
        this.job = job;
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.mentor = mentor;
        this.venue = venue;
        this.detail = detail;
    }

    public String getJob() {
        return this.job;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public String getTitle() {
        return this.title;
    }

    public String getMentor() {
        return this.mentor;
    }

    public String getVenue() {
        return this.venue;
    }

    public String getDetail() {
        return detail;
    }
}
