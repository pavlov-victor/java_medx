package com.nire.medx.diary.list;

import com.nire.medx.diary.DiaryRealmObject;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.Sort;

public class DiaryListPresenter {


    private final Realm realm;

    public DiaryListPresenter() {
        realm = Realm.getDefaultInstance();
    }

    public void close() {
        realm.close();
    }

    public OrderedRealmCollection<DiaryRealmObject> getDiaries() {
        return realm.where(DiaryRealmObject.class).sort("datetime", Sort.DESCENDING).findAll();
    }

    public void deleteDiary(DiaryRealmObject entry) {
        realm.beginTransaction();
        entry.deleteFromRealm();
        realm.commitTransaction();
    }

}
