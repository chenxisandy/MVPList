package com.example.sandy.momlist.model;

import android.app.Activity;

import com.example.sandy.momlist.Task;

import java.util.List;

public interface IModel {
    void loadData();

    int randomHints();

    void dialZXX(Activity activity);

    void getPosition();

//    void getDataBack(int index, Task task);

    void litePalSave();

    void deleteData(int position, List<Task> adapterList);

    void recoverTask(int index, List<Task> adapterList);

    void refreshList();
}
