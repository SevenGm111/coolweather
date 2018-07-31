package com.example.seven.coolweather.db;

import org.litepal.crud.LitePalSupport;

public class Province extends LitePalSupport {

    private int id;

    private String provinceName;  //记录省的名字

    private int provinceCode;     //记录省的代号

    public int getId() {
        return id;
    }

    public Province setId(int id) {
        this.id = id;
        return this;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public Province setProvinceName(String provinceName) {
        this.provinceName = provinceName;
        return this;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public Province setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
        return this;
    }
}
