package com.kimjunhong.jobplanner.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
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
    private String process;
    private String scheduleSub;
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

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getScheduleSub() {
        return scheduleSub;
    }

    public void setScheduleSub(String scheduleSub) {
        this.scheduleSub = scheduleSub;
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

        if(recruit.getLogo() != null) {
            newRecruit.setLogo(recruit.getLogo());
        }
        newRecruit.setCompany(recruit.getCompany());
        newRecruit.setPattern(recruit.getPattern());
        newRecruit.setPosition(recruit.getPosition());
        newRecruit.setSchedule(recruit.getSchedule());
        newRecruit.setProcess(recruit.getProcess());
        newRecruit.setScheduleSub(recruit.getScheduleSub());
        newRecruit.setProcessResult(recruit.getProcessResult());
        newRecruit.setLink(recruit.getLink());
        newRecruit.setMemo(recruit.getMemo());

        recruits.add(newRecruit);

        Log.v("log", String.valueOf(recruits));
    }
}
