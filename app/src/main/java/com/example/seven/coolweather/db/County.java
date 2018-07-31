package com.example.seven.coolweather.db;

import org.litepal.crud.LitePalSupport;

public class County extends LitePalSupport {

    private int id;

    private String countyName;   //记录县的名字

    private String weatherId;    //记录县所对应的的天气id

    private int cityId;          //记录当前县所属的市的id值

    public int getId() {
        return id;
    }

    public County setId(int id) {
        this.id = id;
        return this;
    }

    public String getCountyName() {
        return countyName;
    }

    public County setCountyName(String countyName) {
        this.countyName = countyName;
        return this;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public County setWeatherId(String weatherId) {
        this.weatherId = weatherId;
        return this;
    }

    public int getCityId() {
        return cityId;
    }

    public County setCityId(int cityId) {
        this.cityId = cityId;
        return this;
    }
}
