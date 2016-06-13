package com.strong.googleplay.protocol;

import com.strong.googleplay.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/31.
 */
public class AppProtocol extends BaseProtocol<ArrayList<AppInfo>> {
    @Override
    ArrayList<AppInfo> paserJson(String json) {
        ArrayList<AppInfo> appInfoList=new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                long id = jsonObject.getLong("id");
                String name = jsonObject.getString("name");
                String packageName = jsonObject.getString("packageName");
                String iconUrl = jsonObject.getString("iconUrl");
                float stars = Float.parseFloat(jsonObject.getString("stars"));
                long size = jsonObject.getLong("size");
                String downloadUrl = jsonObject.getString("downloadUrl");
                String des = jsonObject.getString("des");

                AppInfo appInfo = new AppInfo(id, name, iconUrl, packageName, stars, size, downloadUrl, des);
                appInfoList.add(appInfo);
            }
            return appInfoList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    String getKey() {
        return "app";
    }
}
