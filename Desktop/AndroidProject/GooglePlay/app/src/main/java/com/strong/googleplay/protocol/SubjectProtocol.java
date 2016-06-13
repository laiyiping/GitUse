package com.strong.googleplay.protocol;

import com.strong.googleplay.domain.SubjectInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/30.
 */
public class SubjectProtocol extends BaseProtocol<ArrayList<SubjectInfo>>{


    @Override
    ArrayList<SubjectInfo> paserJson(String json) {
        ArrayList<SubjectInfo> subjectInfos = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String des=jsonObject.getString("des");
                String url=jsonObject.getString("url");
                SubjectInfo info = new SubjectInfo(des, url);
                subjectInfos.add(info);
            }
            return subjectInfos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    String getKey() {
        return "subject";
    }
}
