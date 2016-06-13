package com.strong.googleplay.fragment;

import android.support.v4.app.Fragment;

import com.strong.googleplay.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/28.
 */
public class FragmentFactory {

    public static Map<Integer,Fragment> mFragments=new HashMap<>();

    public static Fragment createFragment(int pos) {
        Fragment fragment=null;
        fragment = mFragments.get(pos);
        if (fragment == null) {
            if (pos == 0) {
                fragment=new HomeFragment();
            }
            if (pos == 1) {
                fragment=new AppFragment();
            }
            if (pos == 2) {
                fragment=new GameFragment();
            }
            if (pos == 3) {
                fragment=new SubjectFragment();
            }
            if (pos == 4) {
                fragment=new CategoryFragment();
            }
            if (pos == 5) {
                fragment=new TopFragment();
            }
            if (fragment != null) {
                mFragments.put(pos, fragment);
            }
        }
        return fragment;
    }
}
