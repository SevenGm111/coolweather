package com.example.seven.coolweather.gson;

//包含几天的天气信息
import com.google.gson.annotations.SerializedName;

public class Forecast {

    public String date;

    @SerializedName("tmp")              //使用@SerializedName注解把JSon里的tmp与我们自定义的名称temperature对应起来
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class  Temperature{

        public String max;

        public  String min;

    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }

}
