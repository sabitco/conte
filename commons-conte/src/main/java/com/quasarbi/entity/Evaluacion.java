package com.quasarbi.entity;

public class Evaluacion {

    private int classId;
    private int activityId;
    private int itemId;
    private String activityName;
    private String className;
    private String itemName;
    private int sumActivity;
    private int sumClass;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getSumActivity() {
        return sumActivity;
    }

    public void setSumActivity(int sumActivity) {
        this.sumActivity = sumActivity;
    }

    public int getSumClass() {
        return sumClass;
    }

    public void setSumClass(int sumClass) {
        this.sumClass = sumClass;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        String r = this.classId + " " + this.activityId + " " + this.className + " " + this.activityName + " " + this.itemName;
        return r;

    }
}
