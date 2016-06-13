package com.strong.googleplay.protocol;

import android.os.SystemClock;
import android.provider.ContactsContract;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.IOUtils;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.utils.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/30.
 */
public abstract class BaseProtocol<T> {

    public T load(int index) {

        SystemClock.sleep(1000);
        //请求服务器
        String json = loadLocal(index);
        if (json == null) {
            json=loadServer(index);
        }
        if (json != null) {
            return paserJson(json);
        } else {
            return null;
        }
    }

    private String loadServer(int index) {

        HttpUtils utils = new HttpUtils();
        try {
            ResponseStream responseStream=utils.sendSync(HttpRequest.HttpMethod.GET,
                    "http://127.0.0.1:8090/"+getKey()+"?index=" + index+getParams());//不同协议地址不一样
            String result = responseStream.readString();
            saveLocal(result, index);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String getParams() {
        return "";
    }

    /*
    保存数据到缓存
     */
    private void saveLocal(String json, int index)  {

        BufferedWriter bufferedWriter=null;
        try {
            File dir = FileUtils.getCacheDir();
//            在第一行保存缓存过期时间
            File file = new File(dir, getKey()+"_" + index+getParams());  //不同协议保存的名字不一样
            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(System.currentTimeMillis() + 1000 * 100 + "");
            bufferedWriter.newLine();
            bufferedWriter.write(json);//保存json
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(bufferedWriter);
        }

    }

    /*
    从缓存读取数据
     */
    private String loadLocal(int index) {

        File dir = FileUtils.getCacheDir();
        File file = new File(dir, getKey()+"_" + index+getParams());
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            long outOfDate = Long.parseLong(br.readLine());
            if (System.currentTimeMillis() > outOfDate) {//缓存过期
                return null;
            } else
            {
                String str;
                StringWriter sw = new StringWriter();
                while ((str = br.readLine()) != null) {
                    sw.write(str);
                }
                return sw.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    abstract T paserJson(String json);

    /**
     * 说明访问网络的关键字
     * @return
     */
    abstract String getKey();
}
