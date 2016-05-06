package com.team1.valueupapp.item;

/**
 * Created by knulps on 16. 4. 20..
 * 신청 현황 화면 ViewModel
 */
public class ReqConditionItem {
    String type;
    String title;
    String detail;
    String teamId;

    public ReqConditionItem(String type, String title, String detail, String teamId) {
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.teamId = teamId;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getTeamId() {
        return teamId;
    }
}

