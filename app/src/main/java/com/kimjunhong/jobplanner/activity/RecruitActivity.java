package com.kimjunhong.jobplanner.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.model.Recruit;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.kimjunhong.jobplanner.R.menu.edit;

/**
 * Created by INMA on 2017. 6. 2..
 */

public class RecruitActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recruit_edit_logo) ImageView logo;
    @BindView(R.id.recruit_edit_company) EditText company;
    @BindView(R.id.recruit_edit_pattern) RadioGroup pattern;
    @BindView(R.id.recruit_edit_pattern_newcomer) RadioButton newcomer;
    @BindView(R.id.recruit_edit_pattern_intern) RadioButton intern;
    @BindView(R.id.recruit_edit_pattern_contract) RadioButton contract;
    @BindView(R.id.recruit_edit_pattern_career) RadioButton career;
    @BindView(R.id.recruit_edit_position) EditText position;
    @BindView(R.id.recruit_edit_schedule) TextView schedule;
    @BindView(R.id.recruit_edit_schedule_time) TextView scheduleTime;
    @BindView(R.id.recruit_edit_process) Spinner process;
    @BindView(R.id.recruit_edit_process_result) RadioGroup result;
    @BindView(R.id.recruit_edit_process_ing) RadioButton ing;
    @BindView(R.id.recruit_edit_process_pass) RadioButton pass;
    @BindView(R.id.recruit_edit_process_fail) RadioButton fail;
    @BindView(R.id.recruit_edit_link) EditText link;
    @BindView(R.id.recruit_edit_memo) EditText memo;

    static int REQUEST_PHOTO_ALBUM = 0;
    int processPosition;
    private Realm realm;

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
        getMenuInflater().inflate(edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;

            case R.id.menu_edit:
                int id = getIntent().getIntExtra("id", 0);

                if(id != 0) {
                    updateRecruit(id);
                } else {
                    createRecruit();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        final int id = getIntent().getIntExtra("id", 0);
        if(id != 0) {
            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Recruit recruit = realm.where(Recruit.class).equalTo("id", id).findFirst();
                        Log.v("log", "Selected: " + recruit);

                        // Logo
                        Glide.with(getApplicationContext())
                                .load(recruit.getLogo())
                                .asBitmap()
                                .into(logo);
                        // Company
                        company.setText(recruit.getCompany());
                        // Pattern
                        String checkedPattern = recruit.getPattern();
                        if(checkedPattern.equals("신입")) {
                            pattern.check(newcomer.getId());
                        } else if(checkedPattern.equals("인턴")) {
                            pattern.check(intern.getId());
                        } else if(checkedPattern.equals("계약")) {
                            pattern.check(contract.getId());
                        } else if(checkedPattern.equals("경력")) {
                            pattern.check(career.getId());
                        }
                        // Position
                        position.setText(recruit.getPosition());
                        // Schedule
                        schedule.setText(recruit.getSchedule());
                        // ScheduleTime
                        scheduleTime.setText(recruit.getScheduleTime());
                        // Process
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.recruit_process, android.R.layout.simple_spinner_dropdown_item);
                        process.setAdapter(adapter);
                        processPosition = adapter.getPosition(recruit.getProcess());
                        // Process result
                        String checkedResult = recruit.getProcessResult();
                        if(checkedResult.equals("진행중")) {
                            result.check(ing.getId());
                        } else if(checkedResult.equals("합격")) {
                            result.check(pass.getId());
                        } else if(checkedResult.equals("불합격")) {
                            result.check(fail.getId());
                        }
                        // Link
                        link.setText(recruit.getLink());
                        // Memo
                        memo.setText(recruit.getMemo());
                    }
                });
            } finally {
                realm.close();
            }
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.recruit_process, android.R.layout.simple_spinner_dropdown_item);
        process.setAdapter(adapter);
        if(processPosition == 0) {
            // Clicked fab or document process
            process.setSelection(0);
        } else {
            // Etc process
            process.setSelection(processPosition);
        }

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final Date time = calendar.getTime();

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeAlbum();
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        schedule.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                        Toast.makeText(getApplicationContext(), year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
                    }
                };

                new DatePickerDialog(RecruitActivity.this, datePicker, year, month, day).show();
            }
        });

        scheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timePicker = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        scheduleTime.setText(hourOfDay + ":" + minute);
                        Toast.makeText(getApplicationContext(), hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
                    }
                };

                new TimePickerDialog(RecruitActivity.this, timePicker, time.getHours(), time.getMinutes(), true).show();
            }
        });
    }

    private void takeAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_PHOTO_ALBUM){
                logo.setImageURI(data.getData());
            }
        }
    }

    private void createRecruit() {
        int patternId = pattern.getCheckedRadioButtonId();
        final RadioButton pattern = (RadioButton) findViewById(patternId);

        int resultId = result.getCheckedRadioButtonId();
        final RadioButton result = (RadioButton) findViewById(resultId);

        if(String.valueOf(company.getText()).equals("") || String.valueOf(pattern.getText()).equals("") || String.valueOf(position.getText()).equals("") ||
           String.valueOf(schedule.getText()).equals("") || String.valueOf(scheduleTime.getText()).equals("") ||
           String.valueOf(process.getSelectedItem()).equals("") || String.valueOf(result.getText()).equals("")) {
            Toast.makeText(getApplicationContext(), "입력 사항이 부족합니다", Toast.LENGTH_SHORT).show();
        } else {
            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Recruit recruit = new Recruit();

                        // 이미지 처리
                        Drawable drawable = logo.getDrawable();
                        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] logoByteData = stream.toByteArray();
                        recruit.setLogo(logoByteData);

                        recruit.setCompany(String.valueOf(company.getText()));
                        recruit.setPattern(String.valueOf(pattern.getText()));
                        recruit.setPosition(String.valueOf(position.getText()));
                        recruit.setSchedule(String.valueOf(schedule.getText()));
                        recruit.setScheduleTime(String.valueOf(scheduleTime.getText()));
                        recruit.setProcess(String.valueOf(process.getSelectedItem()));
                        recruit.setProcessResult(String.valueOf(result.getText()));
                        recruit.setLink(String.valueOf(link.getText()));
                        recruit.setMemo(String.valueOf(memo.getText()));

                        Recruit.create(realm, recruit);
                        Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            } finally {
                realm.close();
            }
        }
    }

    private void updateRecruit(final int id) {
        int patternId = pattern.getCheckedRadioButtonId();
        final RadioButton pattern = (RadioButton) findViewById(patternId);

        int resultId = result.getCheckedRadioButtonId();
        final RadioButton result = (RadioButton) findViewById(resultId);

        if(String.valueOf(company.getText()).equals("") || String.valueOf(pattern.getText()).equals("") || String.valueOf(position.getText()).equals("") ||
           String.valueOf(schedule.getText()).equals("") || String.valueOf(scheduleTime.getText()).equals("") ||
           String.valueOf(process.getSelectedItem()).equals("") || String.valueOf(result.getText()).equals("")) {
            Toast.makeText(getApplicationContext(), "입력 사항이 부족합니다", Toast.LENGTH_SHORT).show();
        } else {
            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Recruit recruit = new Recruit();

                        // 이미지 처리
                        Drawable drawable = logo.getDrawable();
                        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] logoByteData = stream.toByteArray();
                        recruit.setLogo(logoByteData);

                        recruit.setId(id);
                        recruit.setCompany(String.valueOf(company.getText()));
                        recruit.setPattern(String.valueOf(pattern.getText()));
                        recruit.setPosition(String.valueOf(position.getText()));
                        recruit.setSchedule(String.valueOf(schedule.getText()));
                        recruit.setScheduleTime(String.valueOf(scheduleTime.getText()));
                        recruit.setProcess(String.valueOf(process.getSelectedItem()));
                        recruit.setProcessResult(String.valueOf(result.getText()));
                        recruit.setLink(String.valueOf(link.getText()));
                        recruit.setMemo(String.valueOf(memo.getText()));

                        Recruit.update(realm, recruit);
                        Toast.makeText(getApplicationContext(), "수정", Toast.LENGTH_SHORT).show();
                    }
                });
            } finally {
                realm.close();
            }
        }
    }
}
