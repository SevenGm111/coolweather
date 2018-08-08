package com.example.seven.coolweather.gson;

//aqi包含当前空气质量的情况
public class AQI {

    public AQICity city;

    public class AQICity{

        public String aqi;

        public String pm25;

    }
}
