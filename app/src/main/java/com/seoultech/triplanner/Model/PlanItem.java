package com.seoultech.triplanner.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanItem {
    private int dateStart;
    private int dateEnd;
    private HashMap<String, ArrayList<PostItem>> placesByDay;

    public PlanItem() {
        placesByDay = new HashMap<>();
    }

    public int getDateStart() {
        return dateStart;
    }
    public void setDateStart(int dateStart) {
        this.dateStart = dateStart;
    }

    public int getDateEnd() {
        return dateEnd;
    }
    public void setDateEnd(int dateEnd) {
        this.dateEnd = dateEnd;
    }

    public HashMap<String, ArrayList<PostItem>> getPlacesByDay() {
        return placesByDay;
    }
    public void setPlacesByDay(HashMap<String, ArrayList<PostItem>> placesByDay) {
        this.placesByDay = placesByDay;
    }
}
