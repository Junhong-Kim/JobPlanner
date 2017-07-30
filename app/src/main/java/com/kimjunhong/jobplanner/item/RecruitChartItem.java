package com.kimjunhong.jobplanner.item;

/**
 * Created by INMA on 2017. 6. 19..
 */

public class RecruitChartItem {
    private int id;
    private byte[] logo;
    private String company;
    private String pattern;
    private String position;
    private String process;
    private String processResult;
    private String schedule;

    public RecruitChartItem(int id, byte[] logo, String company, String pattern, String position, String process, String processResult, String schedule) {
        this.id = id;
        this.logo = logo;
        this.company = company;
        this.pattern = pattern;
        this.position = position;
        this.process = process;
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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
