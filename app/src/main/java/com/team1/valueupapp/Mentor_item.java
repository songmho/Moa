package com.team1.valueupapp;

/**
 * Created by eugene on 2015-08-07.
 */
public class Mentor_item {
    String mentor_name;
    String mentor_filed;
    String company;
    String email;

    public String getMentor_name() {
        return mentor_name;
    }

    public String getMentor_filed() {
        return mentor_filed;
    }

    public String getCompany() {  return company;    }

    public String getEmail() {
        return email;
    }

    public Mentor_item(String mentor_name, String mentor_filed, String company, String email) {
        this.mentor_name = mentor_name;
        this.mentor_filed = mentor_filed;
        this.company = company;
        this.email = email;
    }
}
