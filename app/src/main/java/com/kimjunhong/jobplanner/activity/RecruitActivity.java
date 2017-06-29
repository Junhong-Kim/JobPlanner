package com.kimjunhong.jobplanner.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;

import com.kimjunhong.jobplanner.R;
import com.kimjunhong.jobplanner.model.Recruit;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by INMA on 2017. 6. 2..
 */

public class RecruitActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recruit_edit_logo) ImageView logo;
    @BindView(R.id.recruit_edit_company) EditText company;
    @BindView(R.id.recruit_edit_pattern) RadioGroup pattern;
    @BindView(R.id.recruit_edit_position) EditText position;
    @BindView(R.id.recruit_edit_schedule) TextView schedule;
    @BindView(R.id.recruit_edit_process) Spinner process;
    @BindView(R.id.recruit_edit_schedule_sub) TextView scheduleSub;
    @BindView(R.id.recruit_edit_process_result) RadioGroup result;
    @BindView(R.id.recruit_edit_link) EditText link;
    @BindView(R.id.recruit_edit_memo) EditText memo;

    static int REQUEST_PHOTO_ALBUM = 0;
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
        getMenuInflater().inflate(R.menu.recruit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;

            case R.id.menu_edit:
                createRecruit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.recruit_process, android.R.layout.simple_spinner_item);
        process.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

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

        if(String.valueOf(company.getText()).equals("") || String.valueOf(pattern.getText()).equals("") || String.valueOf(position.getText()).equals("") || String.valueOf(schedule.getText()).equals("") ||
           String.valueOf(process.getSelectedItem()).equals("") || String.valueOf(scheduleSub.getText()).equals("") || String.valueOf(result.getText()).equals("")) {
            Toast.makeText(getApplicationContext(), "입력 사항이 부족합니다", Toast.LENGTH_SHORT).show();
        } else {
            try {
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Recruit recruit = new Recruit();

                        // 이미지 처리
                        if(logo.getDrawable() != null) {
                            Drawable drawable = logo.getDrawable();
                            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] logoByteData = stream.toByteArray();
                            recruit.setLogo(logoByteData);
                        }

                        recruit.setCompany(String.valueOf(company.getText()));
                        recruit.setPattern(String.valueOf(pattern.getText()));
                        recruit.setPosition(String.valueOf(position.getText()));
                        recruit.setSchedule(String.valueOf(schedule.getText()));
                        recruit.setProcess(String.valueOf(process.getSelectedItem()));
                        recruit.setScheduleSub(String.valueOf(scheduleSub.getText()));
                        recruit.setProcessResult(String.valueOf(result.getText()));
                        recruit.setLink(String.valueOf(link.getText()));
                        recruit.setMemo(String.valueOf(memo.getText()));

                        Recruit.create(realm, recruit);
                        Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                    }
                });
            } finally {
                realm.close();
            }
        }
    }
}
