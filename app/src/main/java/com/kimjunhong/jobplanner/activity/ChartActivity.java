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

import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.adapter.ChartDetailAdapter;
import com.kimjunhong.jobplanner.adapter.ChartPagerAdapter;
import com.kimjunhong.jobplanner.fragment.DocumentChartFragment;
import com.kimjunhong.jobplanner.fragment.InterviewChartFragment;
import com.kimjunhong.jobplanner.fragment.SynthesizeChartFragment;
import com.kimjunhong.jobplanner.fragment.TestChartFragment;
import com.kimjunhong.jobplanner.item.ChartDetailItem;
import com.kimjunhong.jobplanner.model.Recruit;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

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
    private Realm realm;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);

        initToolbar();
        initView();
        initChartPager();
        initRecyclerView(getProcessRecruits("서류"));
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
                if(currentPage > 0)
                    viewPager.setCurrentItem(--currentPage);
            }
        });

        nextProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage < 3)
                    viewPager.setCurrentItem(++currentPage);
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
                currentPage = position;
                indicator.setViewPager(viewPager);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        processName.setText("서류");
                        initRecyclerView(getProcessRecruits("서류"));
                        break;
                    case 1:
                        processName.setText("시험");
                        initRecyclerView(getProcessRecruits("시험"));
                        break;
                    case 2:
                        processName.setText("면접");
                        initRecyclerView(getProcessRecruits("면접"));
                        break;
                    case 3:
                        processName.setText("종합");
                        initRecyclerView(getProcessRecruits("종합"));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<ChartDetailItem> getProcessRecruits(final String process) {
        final List<ChartDetailItem> items = new ArrayList<>();

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Recruit> recruits = Recruit.findAllByProcess(realm, process);
                    ChartDetailItem[] item = new ChartDetailItem[recruits.size()];

                    for (int i = 0; i < recruits.size(); i++) {
                        item[i] = new ChartDetailItem(recruits.get(i).getId(),
                                                      recruits.get(i).getLogo(),
                                                      recruits.get(i).getCompany(),
                                                      recruits.get(i).getPosition(),
                                                      recruits.get(i).getProcessResult(),
                                                      recruits.get(i).getSchedule());
                        items.add(item[i]);
                    }
                }
            });
        } finally {
            realm.close();
        }

        return items;
    }

    public List<ChartDetailItem> getProcessResults(final String process, final String result) {
        final List<ChartDetailItem> items = new ArrayList<>();

        try {
            realm = realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Recruit> recruits = Recruit.findAllByProcessWithResult(realm, process, result);
                    ChartDetailItem[] item = new ChartDetailItem[recruits.size()];

                    for (int i = 0; i < recruits.size(); i++) {
                        item[i] = new ChartDetailItem(recruits.get(i).getId(),
                                                      recruits.get(i).getLogo(),
                                                      recruits.get(i).getCompany(),
                                                      recruits.get(i).getPosition(),
                                                      recruits.get(i).getProcessResult(),
                                                      recruits.get(i).getSchedule());
                        items.add(item[i]);
                    }
                }
            });
        } finally {
            realm.close();
        }

        return items;
    }

    public void initRecyclerView(List<ChartDetailItem> items) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ChartDetailAdapter(getApplicationContext(), items));
    }
}
