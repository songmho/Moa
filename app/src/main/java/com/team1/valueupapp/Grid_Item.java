package com.team1.valueupapp;

/**
 * Created by songmho on 2015-07-04.
 */
public class Grid_Item {
    String idea;
    String state;
    int plan;
    int develop;
    int design;
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

    public String getIdea_info() {
        return idea_info;
    }

    public Grid_Item(String idea, String state, int plan, int develop, int design, String idea_info){
        this.idea=idea;
        this.state=state;
        this.plan=plan;
        this.develop=develop;
        this.design=design;
        this.idea_info=idea_info;
    }
}
