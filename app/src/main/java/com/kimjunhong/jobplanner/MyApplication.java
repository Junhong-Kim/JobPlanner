package com.kimjunhong.jobplanner;

import android.app.Application;

import com.kimjunhong.jobplanner.model.RecruitList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by INMA on 2017. 6. 28..
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(RecruitList.class);
                    }
                })
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
