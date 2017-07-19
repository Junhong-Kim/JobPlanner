package com.kimjunhong.jobplanner.item;

/**
 * Created by INMA on 2017. 6. 5..
 */

public class RecruitEventItem {
    private int id;
    private byte[] logo;
    private String company;
    private String position;
    private String scheduleTime;
    private String process;

    public RecruitEventItem(int id, byte[] logo, String company, String position, String scheduleTime, String process) {
        this.id = id;
        this.logo = logo;
        this.company = company;
        this.position = position;
        this.scheduleTime = scheduleTime;
        this.process = process;
    }

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
}
