package com.kimjunhong.jobplanner.item;

/**
 * Created by INMA on 2017. 6. 19..
 */

public class ChartDetailItem {
    private int id;
    private byte[] logo;
    private String company;
    private String position;
    private String processResult;
    private String schedule;

    public ChartDetailItem(int id, byte[] logo, String company, String position, String processResult, String schedule) {
        this.id = id;
        this.logo = logo;
        this.company = company;
        this.position = position;
        this.processResult = processResult;
        this.schedule = schedule;
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

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
