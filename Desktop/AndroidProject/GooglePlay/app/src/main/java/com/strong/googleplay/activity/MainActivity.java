package com.strong.googleplay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.strong.googleplay.R;
import com.strong.googleplay.fragment.FragmentFactory;
import com.strong.googleplay.fragment.HomeFragment;
import com.strong.googleplay.holder.MenuHolder;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mViewPager;
    private PagerTabStrip mPagerTabStrip;
    private String[] mTabsName;
    private FrameLayout flLeftMenu;


    @Override
    protected void initView() {
        LogUtils.e("Main initView");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPagerTabStrip= (PagerTabStrip) findViewById(R.id.pager_tab_strip);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.
                drag_open,R.string.drag_close){
        };
        mPagerTabStrip.setTabIndicatorColorResource(R.color.colorTabIndicator);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

        flLeftMenu = (FrameLayout) findViewById(R.id.fl_left_menu);
        flLeftMenu.addView(new MenuHolder().getContentView());
    }

    @Override
    protected void initActitonBar() {
    }

    @Override
    protected void init() {
        mTabsName = UiUtils.getStringArray(R.array.tabs_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public class MainPagerAdapter extends FragmentStatePagerAdapter{

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return FragmentFactory.createFragment(position);
        }

        @Override
        public int getCount() {
            return mTabsName.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mTabsName[position];
        }
    }
}
