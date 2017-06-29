package com.kimjunhong.jobplanner.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.adapter.TabPagerAdapter;
import com.kimjunhong.jobplanner.fragment.CalendarFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navigationView) NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initToolbar();
        initViewPager();
        initDrawerLayout();
        permissionCheck();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.menu_chart:
                startActivity(new Intent(MainActivity.this, ChartActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new com.kimjunhong.jobplanner.fragment.ListFragment());
        fragments.add(new CalendarFragment());

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(tabPagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("리스트"));
        tabLayout.addTab(tabLayout.newTab().setText("캘린더"));
        tabLayout.setTabTextColors(Color.LTGRAY, ContextCompat.getColor(this, R.color.colorPrimary));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                    case 1:
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                switch (id) {
                    case R.id.navigation_item_export:
                        Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_item_settings:
                        Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_item_feedback:
                        Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_item_review:
                        Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });
    }

    private void permissionCheck() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "거부된 권한\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("원활한 서비스 이용을 위해\n앱의 권한이 필요합니다!\n\n- 개발자 올림 -")
                .setDeniedMessage("앗.. 권한을 거부하셨어요!\n[설정] ► [권한]에서 권한을 허용하시면\n원활한 서비스 이용이 가능합니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
}
