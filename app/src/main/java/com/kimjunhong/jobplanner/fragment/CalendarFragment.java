package com.kimjunhong.jobplanner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.adapter.RecruitEventAdapter;
import com.kimjunhong.jobplanner.item.RecruitEventItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INMA on 2017. 5. 31..
 */

public class CalendarFragment extends Fragment {
    SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("yyyy년 MM월", Locale.getDefault());

    @BindView(R.id.calendarView_previous_month) ImageView calendarViewPreviousMonth;
    @BindView(R.id.calendarView_next_month) ImageView calendarViewNextMonth;
    @BindView(R.id.calendarView_header) TextView calendarViewHeader;
    @BindView(R.id.calendarView) CompactCalendarView calendarView;

    @BindView(R.id.recruit_event_recyclerView) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        ButterKnife.bind(this, view);

        initCalendarView();
        initRecyclerView();

        return view;
    }

    private void initCalendarView() {
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        calendarView.displayOtherMonthDays(true);
        calendarViewHeader.setText(dateFormatForMonth.format(calendarView.getFirstDayOfCurrentMonth()));
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                calendarViewHeader.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        calendarViewPreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.showPreviousMonth();
            }
        });

        calendarViewNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.showNextMonth();
            }
        });
    }

    private List<RecruitEventItem> dummyData() {
        List<RecruitEventItem> items = new ArrayList<>();
        RecruitEventItem[] item = new RecruitEventItem[5];

        item[0] = new RecruitEventItem(R.drawable.icon_company_logo, "Google", "Android Developer", "13:00", "서류", "마감");
        item[1] = new RecruitEventItem(R.drawable.icon_company_logo, "Facebook", "Android Developer", "14:00", "서류", "마감");
        item[2] = new RecruitEventItem(R.drawable.icon_company_logo, "Twitter", "Android Developer", "15:00", "서류", "마감");
        item[3] = new RecruitEventItem(R.drawable.icon_company_logo, "Instagram", "Android Developer", "16:00", "서류", "마감");
        item[4] = new RecruitEventItem(R.drawable.icon_company_logo, "Samsung", "Android Developer", "17:00", "서류", "마감");

        for(int i = 0; i < 5; i++) {
            items.add(item[i]);
        }

        return items;
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecruitEventAdapter(getActivity(), dummyData()));
    }
}
