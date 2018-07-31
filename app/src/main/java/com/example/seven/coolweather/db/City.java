package com.example.seven.coolweather.db;

import org.litepal.crud.LitePalSupport;

public class City extends LitePalSupport {

    private int id;

    private String cityName;     //记录市的名字

    private int cityCode;        //记录市的代号

    private int provinceId;     //记录当前市所属省的id值

    public int getId() {
        return id;
    }

    public City setId(int id) {
        this.id = id;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public City setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public int getCityCode() {
        return cityCode;
    }

    public City setCityCode(int cityCode) {
        this.cityCode = cityCode;
        return this;
    }

    public int getProvincedId() {
        return provinceId;
    }

    public City setProvincedId(int provincedId) {
        this.provinceId = provincedId;
        return this;
    }
}
