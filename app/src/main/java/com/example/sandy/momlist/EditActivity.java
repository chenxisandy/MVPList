package com.example.sandy.momlist;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.stream.HttpUrlGlideUrlLoader;
import com.example.sandy.momlist.presenter.IPresenter;
import com.example.sandy.momlist.view.IEditView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity implements IEditView, View.OnClickListener {


    IPresenter presenter;


    private Task task;

    private EditText titleEdit;
    private EditText contentEdit;
    private EditText yearEdit;
    private EditText monthEdit;
    private EditText dayEdit;
    private EditText hourEdit;
    private EditText minuteEdit;
    private CheckBox timeSet;
    private ImageView imageView;

    private long time;

    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;

    private String title;
    private String content;
    private boolean isSetTime;
    private boolean isDelete;
    private int level;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        task = presenter.getTask();
        title = task.getTitle();
        isSetTime = task.isSetTime();
        content = task.getContent();
        time = task.getTime();
        isDelete = task.isDelete();
        level = task.getLevel();



        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapse_toolbar);

        loadBingPic();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(task.getTitle());

        presenter.setEdit(); //把edit text设置好



        FloatingActionButton saveAll;

        saveAll = findViewById(R.id.save_task);
        saveAll.setOnClickListener(this);


        final FloatingActionButton cancelAlarm = findViewById(R.id.cancel_alarm);
        cancelAlarm.setButtonSize(FloatingActionButton.SIZE_MINI);
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelAlarm.getColorNormal() == Color.parseColor("#ffab00")) {
                    cancelAlarm.setColorNormal(Color.parseColor("#9e9e9e"));
                    Toast.makeText(EditActivity.this, "您已取消闹钟！", Toast.LENGTH_SHORT).show();
                    // Todo 取消闹钟，暂停一下 & 上面的 if 判断条件 要加上如果已将设置了闹钟
                }
            }
        });

        final FloatingActionMenu famSetPriority = findViewById(R.id.set_priority_menu);
        final FloatingActionButton levelOne = findViewById(R.id.level_one);
        final FloatingActionButton levelTwo = findViewById(R.id.level_two);
        final FloatingActionButton levelThree = findViewById(R.id.level_three);
        final FloatingActionButton levelFour = findViewById(R.id.level_four);

        levelFour.setButtonSize(FloatingActionButton.SIZE_MINI);
        levelThree.setButtonSize(FloatingActionButton.SIZE_MINI);
        levelTwo.setButtonSize(FloatingActionButton.SIZE_MINI);
        levelOne.setButtonSize(FloatingActionButton.SIZE_MINI);
        saveAll.setButtonSize(FloatingActionButton.SIZE_MINI);


        famSetPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //如果已经设了优先级就会在点击后变回以前的颜色，防止与以前颜色弄混
                if (famSetPriority.getMenuButtonColorNormal() != R.color.colorAccent) {
                    Toast.makeText(EditActivity.this, "请设置您的优先级", Toast.LENGTH_SHORT).show();
//                    famSetPriority.setMenuButtonColorNormal(R.color.colorAccent);
                }
                famSetPriority.open(true);
            }
        });
        //之后为设置有优先级，通过颜色进行调节
        levelFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setLevel(4);
                famSetPriority.setMenuButtonColorNormal(levelFour.getColorNormal());
            }
        });
        levelThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setLevel(3);
                famSetPriority.setMenuButtonColorNormal(levelThree.getColorNormal());
            }
        });
        levelTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setLevel(2);
                famSetPriority.setMenuButtonColorNormal(levelTwo.getColorNormal());
            }
        });
        levelOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setLevel(1);
                famSetPriority.setMenuButtonColorNormal(levelOne.getColorNormal());
            }
        });


    }



    @Override
    public void onBackPressed() {//如果不小心回退，那么仍然保存数据
        presenter.saveAndBack();
        super.onBackPressed();
    }
    private void loadBingPic() {//还未完等待以后加载更新的功能功能，判断联网来确定是不是加载默认图片
//        final String requestBingPic = "http://guolin.tech/api/bing_pic";
//        final String requestBingPic = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1"后备

        imageView = findViewById(R.id.bing_picture);
        Glide.with(this).load(R.drawable.header_background).into(imageView);//必须要这样否则会溢出内存
    }


    @Override
    public Task getTask() {
        intent = getIntent();//在之后先操作再传回去
        task = (Task)intent.getSerializableExtra(getResources().getString(R.string.TASK));
        return task;
    }

    @Override
    public void setEdit() {  //视图转化出来
        //先创建对象
        yearEdit = findViewById(R.id.year_);
        monthEdit = findViewById(R.id.month_);
        dayEdit = findViewById(R.id.day_);
        hourEdit = findViewById(R.id.hour_);
        minuteEdit = findViewById(R.id.minute_);
        titleEdit = findViewById(R.id.task_title);
        contentEdit = findViewById(R.id.task_content);
        timeSet = findViewById(R.id.set_time);

        //下面把传入的时间格式化为我们的字母
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat formatDay = new SimpleDateFormat("dd");
        SimpleDateFormat formatHour = new SimpleDateFormat("HH");
        SimpleDateFormat formatMinute = new SimpleDateFormat("mm");


        year = formatYear.format(time);//再把格式化的String时间置给他们
        month = formatMonth.format(time);
        day = formatDay.format(time);
        hour = formatHour.format(time);
        minute = formatMinute.format(time);

        yearEdit.setText(year);
        monthEdit.setText(month);
        dayEdit.setText(day);
        hourEdit.setText(hour);
        minuteEdit.setText(minute);


        timeSet.setChecked(isSetTime);//到此他们都设置好了

        //把传入的值表示出来
        titleEdit.setText(title);
        contentEdit.setText(content);
    }

    @Override
    public void backAndSave() {
        year = yearEdit.getText().toString();
        month = monthEdit.getText().toString();
        day = dayEdit.getText().toString();
        hour = hourEdit.getText().toString();
        minute = minuteEdit.getText().toString();//把时间设置好了
        //小心年份要减去1900，月份要减去1月
        Date date = new Date(Integer.parseInt(year) - 1900, Integer.parseInt(month) - 1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute), 00);
        time = date.getTime();
        title = titleEdit.getText().toString();
        content = contentEdit.getText().toString();
        isSetTime = timeSet.isChecked();
        task.setContent(content);
        task.setTime(time);
        task.setTitle(title);
        task.setSetTime(isSetTime);
        task.setDelete(isDelete);
        //返回数据给主活动
        Intent intentBack = new Intent();
        intentBack.putExtra(getResources().getString(R.string.TASK_BACK), task);
        intentBack.putExtra(getResources().getString(R.string.INDEX_BACK), intent.getIntExtra("index", 0));
        setResult(RESULT_OK, intentBack);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_task:
                presenter.saveAndBack();
                finish();//这个不能写进函数以免另一个不适应
                break;
                case R.id.cancel_alarm:


        }
    }
}
