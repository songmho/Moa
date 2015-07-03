package com.team1.valueupapp;

/**
 * Created by songmho on 2015-07-04.
 */
public class item {
    String idea;
    int plan;
    int develop;
    int design;

    String getIdea(){
        return this.idea;
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

    public item(String idea, int plan, int develop, int design){
        this.idea=idea;
        this.plan=plan;
        this.develop=develop;
        this.design=design;
    }
}
