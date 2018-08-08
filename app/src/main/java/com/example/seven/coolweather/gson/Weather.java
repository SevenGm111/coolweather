package com.example.seven.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")  //因为daily_forecast包含的是一个数组，所以用List集合引用
    public List<Forecast> forecastList;


}
