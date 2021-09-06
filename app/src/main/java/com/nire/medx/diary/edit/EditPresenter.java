package com.nire.medx.diary.edit;

import com.nire.medx.diary.DiaryRealmObject;

import java.util.Date;

import io.realm.Realm;

public class EditPresenter {

    Realm realm;

    public EditPresenter() {
        realm = Realm.getDefaultInstance();
    }

    public void close() {
        realm.close();
    }

    public DiaryRealmObject retrieveEntry(Date datetime) {
        return realm.where(DiaryRealmObject.class).equalTo("datetime", datetime).findFirst();
    }

    public void saveEntry(Date datetime, Integer upperPressure, Integer lowerPressure, String note, Integer pulse) {
        realm.beginTransaction();
        DiaryRealmObject editingEntry;
        if (datetime == null) {
            editingEntry = realm.createObject(DiaryRealmObject.class);
            editingEntry.setDatetime(new Date());
        } else {
            editingEntry = realm.where(DiaryRealmObject.class).equalTo("datetime", datetime).findFirst();
        }
        assert editingEntry != null;
        editingEntry.setUpperPressure(upperPressure);
        editingEntry.setLowerPressure(lowerPressure);
        editingEntry.setNote(note);
        editingEntry.setPulse(pulse);
        realm.commitTransaction();
    }

}
