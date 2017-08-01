package com.kimjunhong.jobplanner.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.adapter.RecruitChartAdapter;
import com.kimjunhong.jobplanner.item.RecruitChartItem;
import com.kimjunhong.jobplanner.model.Recruit;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by INMA on 2017. 6. 11..
 */

public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.chart_pieChart) PieChart pieChart;
    @BindView(R.id.chart_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.chart_defaultLayout) FrameLayout defaultLayout;
    @BindView(R.id.chart_adView) AdView adView;

    private Realm realm;
    private int documentPercent;
    private int testPercent;
    private int interviewPercent;
    private int finalPassPercent;
    private int resultSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);

        initToolbar();
        initChart();
        initRecyclerView(getProcessRecruits());

        // AdMob 전면 광고
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initChart() {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                resultSize = realm.where(Recruit.class).equalTo("processResult", "불합격")
                                                       .or()
                                                       .equalTo("process", "최종면접")
                                                       .equalTo("processResult", "합격")
                                                       .findAll().size();
            }
        });

        // 채용 결과 데이터가 있을 경우 분석
        if(resultSize > 0) {
            defaultLayout.setVisibility(View.INVISIBLE);

            // 차트 비율
            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        float documentSize = Recruit.findAllByProcessWithResult(realm, "서류", "불합격").size();
                        float testSize = Recruit.findAllByProcessWithResult(realm, "필기", "불합격").size();
                        float interviewSize = Recruit.findAllByProcessWithResult(realm, "면접", "불합격").size();

                        float finalPassSize = realm.where(Recruit.class).equalTo("process", "최종면접")
                                                                        .equalTo("processResult", "합격")
                                                                        .findAll().size();

                        float totalSize = documentSize + testSize + interviewSize + finalPassSize;

                        documentPercent = (int) ((documentSize / totalSize) * 100);
                        testPercent = (int) ((testSize / totalSize) * 100);
                        interviewPercent = (int) ((interviewSize / totalSize) * 100);
                        finalPassPercent = (int) ((finalPassSize / totalSize) * 100);
                    }
                });
            } finally {
                realm.close();
            }

            // 차트 데이터
            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(finalPassPercent, "최종합격"));
            entries.add(new PieEntry(documentPercent, "서류"));
            entries.add(new PieEntry(testPercent, "필기"));
            entries.add(new PieEntry(interviewPercent, "면접"));

            // 차트 설정
            int[] colors = {ContextCompat.getColor(getApplicationContext(), R.color.positive),
                            ContextCompat.getColor(getApplicationContext(), R.color.negative1),
                            ContextCompat.getColor(getApplicationContext(), R.color.negative2),
                            ContextCompat.getColor(getApplicationContext(), R.color.negative3)};

            PieDataSet dataSet = new PieDataSet(entries, "종합");
            // 차트 색
            dataSet.setColors(ColorTemplate.createColors(colors));
            // 차트 라인 설정
            dataSet.setValueLinePart1OffsetPercentage(80f);
            dataSet.setValueLinePart1Length(1.5f);
            dataSet.setValueLinePart2Length(0.5f);

            PieData data = new PieData(dataSet);
            dataSet.setValueFormatter(new PercentFormatter(new DecimalFormat("###")));
            data.setValueTextSize(15);
            data.setValueTextColor(Color.BLACK);
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setValueLineColor(R.color.colorPrimary);

            pieChart.setData(data);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setUsePercentValues(true);
            // pieChart.setCenterText("종합");
            pieChart.setRotationEnabled(true);
            pieChart.setHighlightPerTapEnabled(true);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);
            pieChart.setOnChartValueSelectedListener(this);
            pieChart.setTransparentCircleRadius(20);
            pieChart.setHoleRadius(10);
            pieChart.setExtraOffsets(0,20,0,20);
            pieChart.setEntryLabelColor(Color.parseColor("#757575"));

            Legend l = pieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setEnabled(false);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null) {
            return;
        } else if (h.getX() == 0) {
            Toast.makeText(getApplicationContext(), "최종합격", Toast.LENGTH_LONG).show();
            final List<RecruitChartItem> items = new ArrayList<>();

            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Recruit> recruits = realm.where(Recruit.class).equalTo("process", "최종면접")
                                                                                   .equalTo("processResult", "합격")
                                                                                   .findAll();

                        RecruitChartItem[] item = new RecruitChartItem[recruits.size()];

                        for (int i = 0; i < recruits.size(); i++) {
                            item[i] = new RecruitChartItem(recruits.get(i).getId(),
                                                           recruits.get(i).getLogo(),
                                                           recruits.get(i).getCompany(),
                                                           recruits.get(i).getPattern(),
                                                           recruits.get(i).getPosition(),
                                                           recruits.get(i).getProcess(),
                                                           recruits.get(i).getProcessResult(),
                                                           recruits.get(i).getSchedule());

                            items.add(item[i]);
                        }
                    }
                });
            } finally {
                initRecyclerView(items);
                realm.close();
            }
        } else if (h.getX() == 1) {
            Toast.makeText(getApplicationContext(), "서류전형 결과", Toast.LENGTH_LONG).show();
            initRecyclerView(getProcessResults("서류", "불합격"));
        } else if (h.getX() == 2) {
            Toast.makeText(getApplicationContext(), "필기전형 결과", Toast.LENGTH_LONG).show();
            initRecyclerView(getProcessResults("필기", "불합격"));
        } else {
            Toast.makeText(getApplicationContext(), "면접전형 결과", Toast.LENGTH_LONG).show();
            initRecyclerView(getProcessResults("면접", "불합격"));
        }

        initChart();
    }

    @Override
    public void onNothingSelected() {
        Toast.makeText(getApplicationContext(), "전체보기", Toast.LENGTH_LONG).show();
        initRecyclerView(getProcessRecruits());
    }

    private List<RecruitChartItem> getProcessRecruits() {
        final List<RecruitChartItem> items = new ArrayList<>();

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Recruit> recruits = Recruit.findAll(realm);
                    RecruitChartItem[] item = new RecruitChartItem[recruits.size()];

                    for (int i = 0; i < recruits.size(); i++) {
                        item[i] = new RecruitChartItem(recruits.get(i).getId(),
                                                       recruits.get(i).getLogo(),
                                                       recruits.get(i).getCompany(),
                                                       recruits.get(i).getPattern(),
                                                       recruits.get(i).getPosition(),
                                                       recruits.get(i).getProcess(),
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

    public List<RecruitChartItem> getProcessResults(final String process, final String result) {
        final List<RecruitChartItem> items = new ArrayList<>();

        try {
            realm = realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Recruit> recruits = Recruit.findAllByProcessWithResult(realm, process, result);
                    RecruitChartItem[] item = new RecruitChartItem[recruits.size()];

                    for (int i = 0; i < recruits.size(); i++) {
                        item[i] = new RecruitChartItem(recruits.get(i).getId(),
                                                       recruits.get(i).getLogo(),
                                                       recruits.get(i).getCompany(),
                                                       recruits.get(i).getPattern(),
                                                       recruits.get(i).getPosition(),
                                                       recruits.get(i).getProcess(),
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

    public void initRecyclerView(List<RecruitChartItem> items) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecruitChartAdapter(getApplicationContext(), items));
    }
}
