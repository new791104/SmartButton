package com.button.smart.smartbutton.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.button.smart.smartbutton.Adapter.MyRecyclerViewAdapter;
import com.button.smart.smartbutton.Global.GV;
import com.button.smart.smartbutton.Http.Network_core;
import com.button.smart.smartbutton.Objects.ButtonItem;
import com.button.smart.smartbutton.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView bRecyclerView;
    private MyRecyclerViewAdapter bAdapter;
    private DrawerLayout layDrawer;
    private ListView lstDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Network_core nCore;
    private String old_response = new String();

    //介面上的 button action
    private ButAct butact = new ButAct();
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final Gson gson = new Gson();
            // 更新Buttons
            //Get all information
            Log.e("debug", "start findAll");
            nCore.button_findAll("charlie");
            nCore.setCallback(new Network_core.netCallback() {
                @Override
                public String response(String response) {
                    Log.e("debug", "response");
                    ArrayList<ButtonItem> rItems = gson.fromJson(response, new TypeToken<List<ButtonItem>>(){}.getType());

                    // bItems_size: APP的bItems大小, rItems_size: response的bItem大小
                    int i, bItems_size = GV.bItems.size(), rItems_size = rItems.size();

                    Log.e("bItems size", "" + GV.bItems.size());
                    Log.e("rItem size", "" + rItems.size());

                    if (GV.bItems.size() == 0) {    // 第一次載入
                        old_response = response;
                        for (i = 0; i < rItems_size; i++) {
                            if (rItems_size != rItems.size()) {
                                i = 0;
                                rItems_size = rItems.size();
                                continue;
                            }
                            //ButtonItem rItem = rItems.get(i);
                            Log.e("debug", "GV.bItems.add(rItems.get(i));");
                            GV.bItems.add(rItems.get(i));
                        }
                    }
                    else {
                        if(response.equals(old_response)){      // button數量不變, 更新資訊
                            for (i = 0; i < rItems_size; i++) {
                                if (rItems_size != rItems.size()) {
                                    i = 0;
                                    rItems_size = rItems.size();
                                    continue;
                                }
                                ButtonItem rItem = rItems.get(i);
                                GV.bItems.get(i).setName(rItem.getName());
                                GV.bItems.get(i).setBid(rItem.getBid());
                                GV.bItems.get(i).setGroup(rItem.getGroup());
                                GV.bItems.get(i).setDescription(rItem.getDescription());
                                GV.bItems.get(i).setStatus(rItem.getStatus());
                            }
                        }
                        else if (old_response.length() < response.length()) {     // button數量增加
                                for (i = 0; i < rItems_size; i++) {
                                    if (rItems_size != rItems.size()) {
                                        i = 0;
                                        rItems_size = rItems.size();
                                        continue;
                                    }
                                    if (old_response.indexOf(rItems.get(i).get_id().toString()) < 0) {
                                        //GV.bItems.add(new ButtonItem(rItems.get(i).get_id(), rItems.get(i).getUser(), rItems.get(i).getBid(), rItems.get(i).getGroup(), rItems.get(i).getName(), rItems.get(i).getDescription(), rItems.get(i).getStatus()));
                                        GV.bItems.add(rItems.get(i));
                                        bRecyclerView.getAdapter().notifyDataSetChanged();
                                        Log.e("debug", "GV.bitems.size" + GV.bItems.size());
                                    }
                                }
                        }

                        else if (old_response.length() > response.length()) {     // button數量減少
                            for (i = 0; i < GV.bItems.size(); i++) {
                                if (rItems_size != rItems.size()) {
                                    i = 0;
                                    rItems_size = rItems.size();
                                    continue;
                                }
                                if (response.indexOf(GV.bItems.get(i).get_id().toString()) < 0) {
                                    GV.bItems.remove(i);
                                    bRecyclerView.getAdapter().notifyDataSetChanged();
                                }
                            }
                        }
                        old_response = response;
                    }

                    bRecyclerView.getAdapter().notifyDataSetChanged();
                    return null;
                }
            });

            handler.postDelayed(this, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("debug", "###onCreate###");
        nCore = new Network_core(this);

        //關閉系統狀態列
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //make some fake data
        //initData();

        // 開關list
        bAdapter = new MyRecyclerViewAdapter(MainActivity.this, GV.bItems);
        bRecyclerView = (RecyclerView) findViewById(R.id.main_RecyclerView);
        bRecyclerView.setAdapter(bAdapter);     //設定適配器
        bRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        // RecyclerView Header
        setHeaderView(bRecyclerView);

        // 右下角選單
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                butact.button_addition(view);
            }
        });

        //左邊隱藏drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLvLeftMenu = (ListView) findViewById(R.id.id_lv_left_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        setUpDrawer();

        //監聽時間變化
        handler.postDelayed(runnable, 1000);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setHeaderView(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.rheader, view, false);
        bAdapter.setHeaderView(header);
    }

    private ListView mLvLeftMenu;
    private DrawerLayout mDrawerLayout;
    private void setUpDrawer()
    {
        LayoutInflater inflater = LayoutInflater.from(this);

        if (mLvLeftMenu == null) {
            mLvLeftMenu.addHeaderView(inflater.inflate(R.layout.header_just_username, mLvLeftMenu, false));
        }
        mLvLeftMenu.setAdapter(new MenuItemAdapter(this));
    }

    /*
    初始化假資料
     */
    private  void initData() {
        for (int i = 0; i < 20; i++) {
            GV.bItems.add(i, new ButtonItem("ABC", "user0", "000" + i, "myGroup", "test" + i, "description", false));
            //Log.e("debug", bItems.toString());
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("debug", "###onBackPressed###");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("debug", "###onCreateOptionsMenu###");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionbItemsSelected(MenuItem item) {
        Log.d("debug", "###onOptionbItemsSelected###");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionbItemsSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d("debug", "###onNavigationItemSelected###");
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
