package com.kimjunhong.jobplanner.item;

/**
 * Created by INMA on 2017. 6. 19..
 */

public class ChartDetailItem {
    int logo;
    String name;
    String job;
    String result;
    String date;

    public ChartDetailItem(int logo, String name, String job, String result, String date) {
        this.logo = logo;
        this.name = name;
        this.job = job;
        this.result = result;
        this.date = date;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
