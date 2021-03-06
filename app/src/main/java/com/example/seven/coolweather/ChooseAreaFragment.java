package com.example.seven.coolweather;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seven.coolweather.db.City;
import com.example.seven.coolweather.db.County;
import com.example.seven.coolweather.db.Province;
import com.example.seven.coolweather.util.HttpUtil;
import com.example.seven.coolweather.util.Utility;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {

    private static final String TAG = "ChooseAreaFragment";

    public static final int LEVEN_PROVINCE = 0;

    public static final int LEVEN_CITY = 1;

    public static final int LEVEN_COUNTY = 2;

    private ProgressDialog progressDialog;   //进度条

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();


    private List<Province> provinceList;  // 省列表

    private List<City> cityList;         //市列表

    private List<County> countyList;    //县列表

    private Province selectedProvince;  //选中的省份

    private City selectedCity;           //选中的市

    private int currentLevel;      //当前选中的级别

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);  //第二个参数表示显示一行text
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //对点击lisetView的点击事件进行处理，当点击的是省时，会跳到市的数据，当点的是市时，会跳到县级的数据,当点击的是县时，跳转到WeatherActivity（显示天气信息)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (currentLevel == LEVEN_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEN_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEN_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity){
                        Intent intent = new Intent(getActivity(),WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();  //关闭滑动菜单
                        activity.swipeRefreshLayout.setRefreshing(true);  //更新下拉刷新进度条
                        activity.requestWeather(weatherId);
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {      //对返回按钮进行判断
                if (currentLevel == LEVEN_COUNTY) {   //如果当前是县级，那就返回市级
                    queryCities();
                } else if (currentLevel == LEVEN_CITY) { //如果当前是市级，那就返回省级
                    queryProvinces();
                }
            }
        });
        queryProvinces();   //从这里开始加载省级的数据
    }

    /**
     * 查询全国所有的省，优先从数据库中查询，如果没有查询到再去服务器上查询  （已检查）
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);  //隐藏返回按钮
        provinceList = LitePal.findAll(Province.class);  //在数据库中查询数据     DataSupport.findAll失效
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEN_PROVINCE;
        } else {   //如果没有查询到，则到服务器上查询
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * 查询省中所有的市，优先从数据库查询，如果没查询到就到服务器上查询 （已检查）
     */
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = LitePal.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEN_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }


    /**
     * 查询选中市中的所有的县，优先从数据库中查询，如果查不到再到服务器上查询 （已检查）
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = LitePal.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEN_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }

    }


    /**
     * 根据传入的地址和类型从服务器上查询省市县数据  （已检查）
     */
    private void queryFromServer(String address, final String type) {
        showProgressBar();
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressBar();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handlerProvinceResponse(responseText);  //解析和处理返回的数据
                } else if ("city".equals(type)) {
                    result = Utility.handlerCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handlerCountyRsponse(responseText, selectedCity.getId());

                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressBar();   //关闭进度条
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }

            }
        });

    }

    /**
     * 显示进度对话框
     */
// TODO: 2018/8/1 把ProgressDialog变成ProgressBar
    private void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */

    private void closeProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }
}



