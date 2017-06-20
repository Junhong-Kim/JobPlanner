package com.kimjunhong.jobplanner.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.adapter.ChartDetailAdapter;
import com.kimjunhong.jobplanner.adapter.ChartPagerAdapter;
import com.kimjunhong.jobplanner.fragment.DocumentChartFragment;
import com.kimjunhong.jobplanner.fragment.InterviewChartFragment;
import com.kimjunhong.jobplanner.fragment.SynthesizeChartFragment;
import com.kimjunhong.jobplanner.fragment.TestChartFragment;
import com.kimjunhong.jobplanner.item.ChartDetailItem;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INMA on 2017. 6. 11..
 */

public class ChartActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.chart_next_process) ImageView nextProcess;
    @BindView(R.id.chart_previous_process) ImageView prevProcess;
    @BindView(R.id.chart_process_name) TextView processName;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.viewPager_indicator) CirclePageIndicator indicator;
    @BindView(R.id.chart_detail_recyclerView) RecyclerView recyclerView;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ButterKnife.bind(this);

        initToolbar();
        initView();
        initChartPager();
        initRecyclerView(dummyData());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        context = this;
        processName.setText("서류");

        prevProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "PREV", Toast.LENGTH_SHORT).show();
            }
        });

        nextProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "NEXT", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initChartPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new DocumentChartFragment());   // 서류
        fragments.add(new TestChartFragment());       // 시험
        fragments.add(new InterviewChartFragment());  // 면접
        fragments.add(new SynthesizeChartFragment()); // 종합

        PagerAdapter adapter = new ChartPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.setViewPager(viewPager);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        processName.setText("서류");
                        initRecyclerView(dummyData());
                        break;
                    case 1:
                        processName.setText("시험");
                        initRecyclerView(dummyData());
                        break;
                    case 2:
                        processName.setText("면접");
                        initRecyclerView(dummyData());
                        break;
                    case 3:
                        processName.setText("종합");
                        initRecyclerView(dummyData());
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<ChartDetailItem> dummyData() {
        List<ChartDetailItem> items = new ArrayList<>();
        ChartDetailItem[] item = new ChartDetailItem[2];

        item[0] = new ChartDetailItem(R.drawable.icon_company_logo, "Company", "Android Developer", "합격", "17.06.19");
        item[1] = new ChartDetailItem(R.drawable.icon_company_logo, "Company", "Android Developer", "합격", "17.06.19");

        for(int i = 0; i < item.length; i++) {
            items.add(item[i]);
        }

        return items;
    }

    public void initRecyclerView(List<ChartDetailItem> items) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ChartDetailAdapter(getApplicationContext(), items));
    }
}
