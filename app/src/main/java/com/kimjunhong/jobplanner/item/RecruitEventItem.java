package com.kimjunhong.jobplanner.item;

/**
 * Created by INMA on 2017. 6. 5..
 */

public class RecruitEventItem {
    byte[] logo;
    String name;
    String job;
    String time;
    String process;

    public RecruitEventItem(byte[] logo, String name, String job, String time, String process) {
        this.logo = logo;
        this.name = name;
        this.job = job;
        this.time = time;
        this.process = process;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
