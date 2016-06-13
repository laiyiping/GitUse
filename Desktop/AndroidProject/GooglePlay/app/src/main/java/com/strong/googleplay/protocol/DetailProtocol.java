package com.strong.googleplay.protocol;

import com.strong.googleplay.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/1.
 */
public class DetailProtocol extends BaseProtocol<AppInfo> {
    private String packageName;

    public DetailProtocol(String packageName) {
        this.packageName = packageName;
    }

    @Override
    AppInfo paserJson(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            long id = jsonObject.getLong("id");
            String name = jsonObject.getString("name");
            String packageName = jsonObject.getString("packageName");
            String iconUrl = jsonObject.getString("iconUrl");
            float stars = Float.parseFloat(jsonObject.getString("stars"));
            long size = jsonObject.getLong("size");
            String downloadUrl = jsonObject.getString("downloadUrl");
            String des = jsonObject.getString("des");
            String downloadNum = jsonObject.getString("downloadNum");
            String version = jsonObject.getString("version");
            String date = jsonObject.getString("date");
            String author = jsonObject.getString("author");

            ArrayList<String> screen = new ArrayList<>();
            JSONArray screenArray = jsonObject.getJSONArray("screen");
            for (int i = 0; i < screenArray.length(); i++) {
                screen.add(screenArray.getString(i));
            }

            ArrayList<String> safeUrl=new ArrayList<>();
            ArrayList<String> safeDesUrl=new ArrayList<>();
            ArrayList<String> safeDes=new ArrayList<>();
            ArrayList<Integer> safeDesColor=new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("safe");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                safeUrl.add(object.getString("safeUrl"));
                safeDesUrl.add(object.getString("safeDesUrl"));
                safeDes.add(object.getString("safeDes"));
                safeDesColor.add(object.getInt("safeDesColor"));
            }

            AppInfo appInfo=new AppInfo(id,name,packageName,iconUrl,stars,size,downloadUrl,des,
                    downloadNum,version, date,author,screen,safeUrl,safeDesUrl,safeDes,safeDesColor);
            return appInfo;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    String getKey() {
        return "detail";
    }

    @Override
    protected String getParams() {
        return "&packageName="+packageName;
    }
}
