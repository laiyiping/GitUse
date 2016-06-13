package com.strong.googleplay.protocol;

import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.IOUtils;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.utils.FileUtils;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/29.
 */
public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>>{


    private ArrayList<String> mPictures;

    public ArrayList<AppInfo> paserJson(String json) {
        ArrayList<AppInfo> appInfos=new ArrayList<>();
        mPictures = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray1 = jsonObject.getJSONArray("picture");
            for (int i = 0; i < jsonArray1.length(); i++) {
                String s = jsonArray1.getString(i);
                mPictures.add(s);
            }

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                 long id=jsonObj.getLong("id");
                 String name=jsonObj.getString("name");
                 String packageName=jsonObj.getString("packageName");
                 String iconUrl=jsonObj.getString("iconUrl");
                 float stars=Float.parseFloat(jsonObj.getString("stars"));
                 long size=jsonObj.getLong("size");
                 String downloadUrl=jsonObj.getString("downloadUrl");
                 String des=jsonObj.getString("des");
                AppInfo appInfo = new AppInfo(id,name,iconUrl,packageName,stars,size,downloadUrl,des);
                appInfos.add(appInfo);
            }
            return appInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<String> getmPictures() {
        return mPictures;
    }

    @Override
    String getKey() {
        return "home";
    }

}
