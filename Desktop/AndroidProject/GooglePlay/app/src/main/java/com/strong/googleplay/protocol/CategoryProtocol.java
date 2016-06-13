package com.strong.googleplay.protocol;

import com.strong.googleplay.domain.CategoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/3.
 */
public class CategoryProtocol extends BaseProtocol<ArrayList<CategoryInfo>> {
    @Override
    ArrayList<CategoryInfo> paserJson(String json) {
        ArrayList<CategoryInfo> infos = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String title = object.getString("title");
                CategoryInfo categoryInfo = new CategoryInfo(title,true);//标题类型
                infos.add(categoryInfo);
                JSONArray jsonArray = object.getJSONArray("infos");
                for(int j=0;j<jsonArray.length();j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    String name1 = jsonObject.getString("name1");
                    String name2 = jsonObject.getString("name2");
                    String name3 = jsonObject.getString("name3");
                    String url1 = jsonObject.getString("url1");
                    String url2 = jsonObject.getString("url2");
                    String url3 = jsonObject.getString("url3");
                    CategoryInfo info = new CategoryInfo(title, name1, name2, name3, url1, url2, url3,false);//不是标题
                    infos.add(info);
                }
            }

            return infos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    String getKey() {
        return "category";
    }
}
