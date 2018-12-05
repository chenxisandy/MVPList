package com.example.sandy.momlist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sandy.momlist.presenter.IPresenter;
import com.example.sandy.momlist.presenter.Presenter;
import com.example.sandy.momlist.view.IMainView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
* 因为butter knife要api26 安卓8，可我的试验机不支持
* 所以放弃使用
* */

/*
* B1U6 Text A
Read the text and finish the following task.
The article ends without an ending. What would Mr. Blanchard do when he knew Miss Maynell put him to a test? Write an ending to the story. (at least 200 words)
批改网查找作文号 1270445提交续写。
DDL: 2018.12.08 23:59
* */
public class MainActivity extends AppCompatActivity implements onStartDragListener, IMainView {
    //用于代替返回值的数字
    private final int ADD_FOR_RESULT = 1;
    private final int EDIT_FOR_RESULT = 2;
    private final int BIN_FOR_RESULT = 3;

    
    IPresenter presenter;
    

    private ItemTouchHelper itemTouchHelper;

    private DrawerLayout mDrawerLayout;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TaskAdapter adapter;

    public static List<Task> taskList = new ArrayList<>();

    private List<Task> myList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolBar布局

        // TODO: 2018/12/1 待删除数据库
        presenter = new Presenter(MainActivity.this, BinActivity.this, EditActivity.this, adapter);
        presenter.loadData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //抽屉布局，加导航栏
        mDrawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
        }
        //初始化导航栏：让光标停留在call的位置
        navigationView.setCheckedItem(R.id.dial);
        //导航栏各个逻辑的实现**********************************啦啦啦
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {//如果点击就拨号给我
                    case R.id.dial:
                        presenter.dialMe(MainActivity.this);
                        break;
                    case R.id.see_task:// TODO: 2018/12/1 to delete 
                        Toast.makeText(MainActivity.this, "您将进入事项界面", Toast.LENGTH_SHORT).show();
                        presenter.backNav();
                        break;
                    case R.id.position:
                        Toast.makeText(MainActivity.this, "您即将可以定位并计算与朱孝曦的距离", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //悬浮按钮的实现*********************************啦啦啦
        FloatingActionMenu floatingActionMenu = findViewById(R.id.fab_menu);
        FloatingActionButton fabAddTask = findViewById(R.id.add_task);
        FloatingActionButton fabFind = findViewById(R.id.find);
        fabAddTask.setButtonSize(FloatingActionButton.SIZE_MINI);
        fabFind.setButtonSize(FloatingActionButton.SIZE_MINI);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task(false, false, System.currentTimeMillis(), 2, "", "", taskList.size(), myList.size(), -1);

                presenter.goToEdit(1, task); // 1->add
            }
        });
        fabFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//随机生成提示告诉用户
                presenter.showHint();

            }
        });

        //刷新功能
        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this, "刷新后，您暂时的排序会改变", Toast.LENGTH_SHORT).show();
                presenter.refreshSort();
            }
        });


    }

    @Override
    public void gotoEdit(Task task) {
        Toast.makeText(MainActivity.this, "您即将进入编辑界面", Toast.LENGTH_SHORT).show();
        Intent intentAdd = new Intent(MainActivity.this, EditActivity.class);
        taskList.add(task);
        myList = Task.getSubList(false, taskList);
        intentAdd.putExtra(getResources().getString(R.string.TASK), task);
        startActivityForResult(intentAdd, ADD_FOR_RESULT);
    }

    @Override
    public void giveHint(int hint) { //hint应该为0到7
        String[] hints = {
                "点击添加事项按钮即可添加新的事项",
                "滑动事项卡即可将其删除",
                "点击事项卡即可查看或编辑",
                "长按事项卡即可移动卡片到你想要的位置",
                "如果一不小心误删，可以点击回收站去查看回收",
                "右滑可进入导航栏",
                "下拉可以刷新排序，更利于您查看卡片",
                "标题栏可能会因为上划而不见，下滑即可"
        };
        Toast.makeText(this, hints[hint], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backNav() {
        Toast.makeText(MainActivity.this, "您将进入事项界面", Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void refreshSort() {  //刷新排序
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(taskList);
                        Collections.sort(myList);
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public void refreshListAlive() { //刷新一下保证
        for (Task t :
                myList) {
            t.setIndexAlive(myList.indexOf(t));
        }
    }

    @Override
    public void notifyAdapter() {
// TODO: 2018/12/1
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {//点击回收箱进入回收的task列表
            case R.id.delete_bin:
                Intent intent = new Intent(MainActivity.this, BinActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {//如果点击返回不是直接就返回而是先退出导航栏，比较符合我个人的习惯
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//返回时接收数据
        switch (requestCode) {
            case EDIT_FOR_RESULT:
            case ADD_FOR_RESULT:
                if (resultCode == RESULT_OK) {//数据返回
                    Task taskBack = (Task) data.getSerializableExtra(getResources().getString(R.string.TASK_BACK));
                    myList.set(taskBack.getIndexAlive(), taskBack);
                    taskList.set(taskBack.getIndex(),  taskBack);
//                    myList.set(index, taskBack);
                    adapter.notifyDataSetChanged();//刷新数据
                }
            case BIN_FOR_RESULT:
                // ToDo
                break;
        }
    }

    @Override
    protected void onStart() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        myList = Task.getSubList(false, taskList);
        adapter = new TaskAdapter(myList);//得到未被删除的哪些东西
        recyclerView.setAdapter(adapter);

        //回调CallBack
        ItemTouchHelper.Callback callback = new TaskTouchHelperCallBack(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        super.onStart();
    }

    @Override
    protected void onDestroy() {//在撤销时保存好数据
        super.onDestroy();
// ToDo 先不加以免已经加了数据库        Task.saveAll(taskList);->
      //  presenter.litePalSave();
    }


    @Override
    public void onStartDrag(TaskAdapter.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
