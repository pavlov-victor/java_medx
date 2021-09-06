package com.nire.medx;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build());
    }
}
