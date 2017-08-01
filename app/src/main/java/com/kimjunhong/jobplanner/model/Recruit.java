package com.kimjunhong.jobplanner.model;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by INMA on 2017. 6. 28..
 */

public class Recruit extends RealmObject {
    @PrimaryKey
    private int id;
    private byte[] logo;
    private String company;
    private String pattern;
    private String position;
    private String schedule;
    private String scheduleTime;
    private String process;
    private String processResult;
    private String link;
    private String memo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public static void create(Realm realm, Recruit recruit) {
        RecruitList recruitList = realm.where(RecruitList.class).findFirst();
        RealmList<Recruit> recruits = recruitList.getRecruitList();

        // PK Auto Increment
        Number currentId = realm.where(Recruit.class).max("id");
        int nextId;

        if(currentId == null) {
            nextId = 1;
        } else {
            nextId = currentId.intValue() + 1;
        }

        // 두번째 인자로 PK 값
        Recruit newRecruit = realm.createObject(Recruit.class, nextId);

        newRecruit.setLogo(recruit.getLogo());
        newRecruit.setCompany(recruit.getCompany());
        newRecruit.setPattern(recruit.getPattern());
        newRecruit.setPosition(recruit.getPosition());
        newRecruit.setSchedule(recruit.getSchedule());
        newRecruit.setScheduleTime(recruit.getScheduleTime());
        newRecruit.setProcess(recruit.getProcess());
        newRecruit.setProcessResult(recruit.getProcessResult());
        newRecruit.setLink(recruit.getLink());
        newRecruit.setMemo(recruit.getMemo());

        recruits.add(newRecruit);
    }

    public static void update(Realm realm, Recruit newRecruit) {
        Recruit recruit = findOne(realm, newRecruit.getId());

        recruit.setLogo(newRecruit.getLogo());
        recruit.setCompany(newRecruit.getCompany());
        recruit.setPattern(newRecruit.getPattern());
        recruit.setPosition(newRecruit.getPosition());
        recruit.setSchedule(newRecruit.getSchedule());
        recruit.setScheduleTime(newRecruit.getScheduleTime());
        recruit.setProcess(newRecruit.getProcess());
        recruit.setProcessResult(newRecruit.getProcessResult());
        recruit.setLink(newRecruit.getLink());
        recruit.setMemo(newRecruit.getMemo());
    }

    public static void delete(Realm realm, int id) {
        Recruit recruit = findOne(realm, id);
        recruit.deleteFromRealm();
    }

    public static Recruit findOne(Realm realm, int id) {
        return realm.where(Recruit.class).equalTo("id", id).findFirst();
    }

    public static RealmResults<Recruit> findAll(Realm realm) {
        return realm.where(Recruit.class).findAll();
    }

    public static RealmResults<Recruit> findAllByDate(Realm realm, String date) {
        return realm.where(Recruit.class).equalTo("schedule", date).findAll();
    }

    public static RealmResults<Recruit> findAllByProcessWithResult(Realm realm, String process, String result) {
        switch (process) {
            case "서류":
                return realm.where(Recruit.class).equalTo("process", "서류")
                                                 .equalTo("processResult", result)
                                                 .findAll();
            case "필기":
                return realm.where(Recruit.class).equalTo("process", "인/적성")
                                                 .equalTo("processResult", result)
                                                 .or()
                                                 .equalTo("process", "테스트")
                                                 .equalTo("processResult", result)
                                                 .findAll();
            case "면접":
                return realm.where(Recruit.class).equalTo("process", "1차면접")
                                                 .equalTo("processResult", result)
                                                 .or()
                                                 .equalTo("process", "2차면접")
                                                 .equalTo("processResult", result)
                                                 .or()
                                                 .equalTo("process", "최종면접")
                                                 .equalTo("processResult", result)
                                                 .findAll();
            default:
                return realm.where(Recruit.class).equalTo("processResult", result).findAll();
        }
    }
}
