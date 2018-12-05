package com.example.sandy.momlist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.sandy.momlist.view.IBinView;

import java.util.List;


public class BinActivity extends AppCompatActivity implements onStartDragListener, IBinView {

    private TaskAdapter taskAdapter;

    private List<Task> myList;

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin);

        initList();//把列表搞定

    }

    @Override
    protected void onStart() {
        myList = Task.getSubList(true, MainActivity.taskList);
        super.onStart();
    }

    void initList() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_bin);
        recyclerView.setLayoutManager(manager);
        myList = Task.getSubList(true,MainActivity.taskList);
        taskAdapter = new TaskAdapter(myList);
        recyclerView.setAdapter(taskAdapter);
        //回调
        ItemTouchHelper.Callback callback = new TaskTouchHelperCallBack(taskAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onStartDrag(TaskAdapter.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void refreshListDelete() {
        for (Task t :
                myList) {
            t.setIndexDelete(myList.indexOf(t));
        }
    }
}
