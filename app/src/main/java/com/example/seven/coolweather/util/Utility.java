package com.example.seven.coolweather.util;

import android.text.TextUtils;
import android.view.View;

import com.example.seven.coolweather.db.City;
import com.example.seven.coolweather.db.County;
import com.example.seven.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    /**
      解析和处理服务器返回的省级数据
     */
    public  static boolean handlerProvinceResponse(String response){
        if (! TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);   //解析为一个JSON数组
                for (int i = 0; i < allProvinces.length(); i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);  //获取每一个JSONObject实例
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  false;
    }

    /*
       解析和处理服务器返回的市级数据
     */
    public static boolean handlerCityResponse(String response, int provinceId){
        if (! TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvincedId(provinceId);
                    city.save();
                }
                return true;

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  false;
    }

    /*
       解析和处理服务器返回的县级的数据
     */
    public static boolean handlerCountyRsponse(String response, int cityId){
        if ( !TextUtils.isEmpty(response)){
            try{
                JSONArray allcounties = new JSONArray(response);
                for (int i = 0; i < allcounties.length(); i++){
                    JSONObject countObject = allcounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countObject.getString("name"));
                    county.setWeatherId(countObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return  true;
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  false;
    }

    /*
       将返回的JSON数据解析成Weather实体类
     */

}
