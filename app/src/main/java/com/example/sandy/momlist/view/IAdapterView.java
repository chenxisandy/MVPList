package com.example.sandy.momlist.view;



import android.support.v7.widget.RecyclerView;

import com.example.sandy.momlist.Task;

import java.util.List;

public interface IAdapterView {
   // void showDelete();
    void GoEdit(Task task);
    void TaskToBin(Task task, int position); //把task丢到垃圾箱
}
