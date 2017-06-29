package com.kimjunhong.jobplanner.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by INMA on 2017. 6. 28..
 */

public class RecruitList extends RealmObject {
    @SuppressWarnings("unused")
    private RealmList<Recruit> recruitList;

    public RealmList<Recruit> getRecruitList() {
        return recruitList;
    }
}
