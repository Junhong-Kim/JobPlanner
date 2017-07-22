package com.kimjunhong.jobplanner.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kimjunhong.jobplanner.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INMA on 2017. 7. 21..
 */

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.settings_start_date_layout) LinearLayout startDateLayout;
    @BindView(R.id.settings_start_date) TextView startDate;
    @BindView(R.id.settings_message_layout) LinearLayout messageLayout;
    @BindView(R.id.settings_message) TextView message;
    @BindView(R.id.settings_notification) Switch notification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        initToolbar();
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        // 키보드 숨기기
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        // 취준 시작일 설정
        final SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        final String date = pref.getString("date", "");
        startDate.setText(date);

        // 목표/각오 설정
        String message = pref.getString("message", "");
        this.message.setText(message);

        // 취준 시작일
        startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        startDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                        Toast.makeText(getApplicationContext(), year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일", Toast.LENGTH_SHORT).show();

                        // 목표/각오 저장
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("date", String.valueOf(startDate.getText()));
                        editor.commit();
                    }
                };

                new DatePickerDialog(SettingsActivity.this, datePicker, year, month, day).show();
            }
        });

        // 목표/각오
        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InputActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 알림
        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notification.setChecked(false);
                Toast.makeText(getApplicationContext(), "서비스 준비중입니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
