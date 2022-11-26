package com.seoultech.triplanner.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanItem {
    private String fbPlanID;
    private String fbDateStart; // LocalDate -> String : 20XX-MM-dd
    private String fbDateEnd;
    private HashMap<String, ArrayList<PostItem>> fbPlacesByDay;

    public PlanItem() {
        fbPlacesByDay = new HashMap<>();
    }

    public String getFbPlanID() {
        return fbPlanID;
    }
    public void setFbPlanID(String planID) {
        this.fbPlanID = planID;
    }

    public String getFbDateStart() {
        return fbDateStart;
    }
    public void setFbDateStart(String dateStart) {
        this.fbDateStart = dateStart;
    }

    public String getDateEnd() {
        return fbDateEnd;
    }
    public void setDateEnd(String dateEnd) {
        this.fbDateEnd = dateEnd;
    }

    public HashMap<String, ArrayList<PostItem>> getFbPlacesByDay() {
        return fbPlacesByDay;
    }
    public void setFbPlacesByDay(HashMap<String, ArrayList<PostItem>> placesByDay) {
        this.fbPlacesByDay = placesByDay;
    }
}
