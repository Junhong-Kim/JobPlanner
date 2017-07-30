package com.kimjunhong.jobplanner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.adapter.RecruitEventAdapter;
import com.kimjunhong.jobplanner.item.RecruitEventItem;
import com.kimjunhong.jobplanner.model.Recruit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by INMA on 2017. 5. 31..
 */

public class CalendarFragment extends Fragment {
    @BindView(R.id.calendarView_previous_month) ImageView calendarViewPreviousMonth;
    @BindView(R.id.calendarView_next_month) ImageView calendarViewNextMonth;
    @BindView(R.id.calendarView_header) TextView calendarViewHeader;
    @BindView(R.id.calendarView) CompactCalendarView calendarView;
    @BindView(R.id.calendar_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.calendar_defaultLayout) FrameLayout defaultLayout;

    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("yyyy년 MM월", Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일");
    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);

        Date today = new Date();
        String date = dateFormat.format(today);

        initCalendarView();
        initRecyclerView(date);

        return view;
    }

    private void initCalendarView() {
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        calendarView.displayOtherMonthDays(true);
        calendarViewHeader.setText(dateFormatForMonth.format(calendarView.getFirstDayOfCurrentMonth()));
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                String date = dateFormat.format(dateClicked);
                int dateSize = scheduleData(date).size();

                if(dateSize == 0) {
                    // 채용 일정이 없을 경우
                    defaultLayout.setVisibility(View.VISIBLE);
                } else {
                    // 채용 일정이 있을 경우
                    defaultLayout.setVisibility(View.INVISIBLE);
                    initRecyclerView(date);
                }

                Log.v("log", "date : " + date + ", " + "size : " + dateSize);
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

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Recruit> recruits = Recruit.findAll(realm);
                    for(Recruit recruit : recruits) {
                        try {
                            Date date = dateFormat.parse(recruit.getSchedule());
                            Event event = new Event(ContextCompat.getColor(getActivity(), R.color.colorAccent), date.getTime());
                            calendarView.addEvent(event, true);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } finally {
            realm.close();
        }
    }

    private List<RecruitEventItem> scheduleData(final String date) {
        final List<RecruitEventItem> items = new ArrayList<>();

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Recruit> recruits = Recruit.findAllByDate(realm, date);

                    RecruitEventItem[] item = new RecruitEventItem[recruits.size()];
                    for (int i = 0; i < recruits.size(); i++) {
                        item[i] = new RecruitEventItem(recruits.get(i).getId(),
                                                       recruits.get(i).getLogo(),
                                                       recruits.get(i).getCompany(),
                                                       recruits.get(i).getPattern(),
                                                       recruits.get(i).getPosition(),
                                                       recruits.get(i).getScheduleTime(),
                                                       recruits.get(i).getProcess());
                        items.add(item[i]);
                    }
                }
            });
        } finally {
            realm.close();
        }

        return items;
    }

    private void initRecyclerView(String date) {
        int dateSize = scheduleData(date).size();

        if(dateSize == 0) {
            // 채용 일정이 없을 경우
            defaultLayout.setVisibility(View.VISIBLE);
        } else {
            // 채용 일정이 있을 경우
            defaultLayout.setVisibility(View.INVISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new RecruitEventAdapter(getActivity(), scheduleData(date)));
        }
    }
}
