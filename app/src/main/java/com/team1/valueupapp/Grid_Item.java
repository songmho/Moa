package com.team1.valueupapp;

/**
 * Created by songmho on 2015-07-04.
 */
public class Grid_Item {
    String idea;
    String state;
    int plan;
    int max_plan;
    int develop;
    int max_dev;
    int design;
    int max_dis;
    String idea_info;


    String getIdea(){
        return this.idea;
    }
    String getState() {
        return this.state;
    }
    int getPlan(){
        return this.plan;
    }
    int getDevelop(){
        return this.develop;
    }
    int getDesign(){
        return  this.design;
    }

    int getMax_dev() {
        return max_dev;
    }
    int getMax_dis() {
        return max_dis;
    }
    int getMax_plan() {
        return max_plan;
    }

    public String getIdea_info() {
        return idea_info;
    }

    public Grid_Item(String idea, String state, int plan, int max_plan, int develop, int max_dev, int design, int max_dis, String idea_info){
        this.idea=idea;
        this.state=state;
        this.plan=plan;
        this.max_plan=max_plan;
        this.develop=develop;
        this.max_dev=max_dev;
        this.design=design;
        this.max_dis=max_dis;
        this.idea_info=idea_info;
    }
}
