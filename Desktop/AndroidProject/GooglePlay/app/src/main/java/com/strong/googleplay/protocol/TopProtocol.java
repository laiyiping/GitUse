package com.strong.googleplay.protocol;

import com.strong.googleplay.domain.TopInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 */
public class TopProtocol extends BaseProtocol<ArrayList<String>> {

    @Override
    ArrayList<String> paserJson(String json) {
        ArrayList<String> stringList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                stringList.add(jsonArray.getString(i));
            }
            return stringList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    String getKey() {
        return "hot";
    }
}
