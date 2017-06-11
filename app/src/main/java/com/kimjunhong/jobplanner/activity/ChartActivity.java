package com.kimjunhong.jobplanner.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kimjunhong.jobplanner.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INMA on 2017. 6. 11..
 */

public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.imageView_next_process) ImageView nextProcess;
    @BindView(R.id.imageView_previous_process) ImageView prevProcess;
    @BindView(R.id.textView_process_name) TextView processName;
    @BindView(R.id.pieChart) PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ButterKnife.bind(this);

        initToolbar();
        initChart();
        initView();
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

    private void initChart() {
        // 차트 데이터
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40, "서류"));
        entries.add(new PieEntry(15, "인/적성"));
        entries.add(new PieEntry(15, "TEST"));
        entries.add(new PieEntry(10, "1차면접"));
        entries.add(new PieEntry(10, "2차면접"));
        entries.add(new PieEntry(10, "최종면접"));

        // 차트 설정
        int[] colors = { Color.parseColor("#3F51B5"),
                Color.parseColor("#5C6BC0"),
                Color.parseColor("#7986CB"),
                Color.parseColor("#9FA8DA"),
                Color.parseColor("#C5CAE9"),
                Color.parseColor("#E8EAF6") };

        PieDataSet dataSet = new PieDataSet(entries, "전형");
        dataSet.setColors(ColorTemplate.createColors(colors));

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setOnChartValueSelectedListener(this);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED", "Value: " + e.getY() + ", index: " + h.getX() + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("JobPlanner\ndeveloped by Junhong Kim");

        s.setSpan(new RelativeSizeSpan(1.5f), 0, 10, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 10, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 10, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.65f), 10, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 15, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 15, s.length(), 0);

        return s;
    }

    private void initView() {
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
}
