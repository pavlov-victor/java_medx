package com.nire.medx.diary;

import java.util.Date;

import io.realm.RealmObject;

public class DiaryRealmObject extends RealmObject {
    private int lowerPressure;

    private int upperPressure;

    private int pulse;

    private String note;

    private Date datetime;

    public DiaryRealmObject() {
    }


    public int getLowerPressure() {
        return lowerPressure;
    }

    public int getUpperPressure() {
        return upperPressure;
    }

    public int getPulse() {
        return pulse;
    }

    public String getNote() {
        return note;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setLowerPressure(int lowerPressure) {
        this.lowerPressure = lowerPressure;
    }

    public void setUpperPressure(int upperPressure) {
        this.upperPressure = upperPressure;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
