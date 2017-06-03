package com.kimjunhong.jobplanner.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kimjunhong.jobplanner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by INMA on 2017. 6. 2..
 */

public class RecruitActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.spinner_recruit_process) Spinner spinner;
    @BindView(R.id.textView_recruit_schedule) TextView schedule;
    @BindView(R.id.textView_recruit_schedule_sub) TextView scheduleSub;
    @BindView(R.id.radioGroup_pattern) RadioGroup rgPattern;
    @BindView(R.id.radioGroup_process_result) RadioGroup rgResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recruit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_edit:
                Toast.makeText(this, "EDIT", Toast.LENGTH_SHORT).show();
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.recruit_process, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Custom DateTime Picker
            }
        });

        scheduleSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scheduleSub.getText().equals("마감")) {
                    scheduleSub.setText("예정");
                } else {
                    scheduleSub.setText("마감");
                }
            }
        });
    }
}
