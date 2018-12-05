package com.example.sandy.momlist.presenter;

import android.app.Activity;

import com.example.sandy.momlist.Task;

import java.util.List;

public interface IPresenter {
    void loadData(); //->model
    void dialMe(Activity activity);//dial ->model
    void getPosition(int type); //type:add or edit ->model

    void goToEdit(int type, Task task);  //type->view  //1->add,2->edit
    void showHint();  //->view:toast & model:random
    void refreshSort(); //->model:refresh_collections & view:SR layout
                        //but in fact choose view
    void notifyAdapter(); //->view
    void backNav(); //->view
//    void getDataBack(int index, Task task); //->model
    void litePalSave(); //->model
//adapter
    void recoverTask(int index, List adapterList); //->model
    void deleteTaskForever(int position, List adapterList);  //delete_data from list and lite pal->model,show delete->model
    void taskToBin(Task task, int position); //view 删除

    //
    Task getTask();  //->view
    void setEdit(); //->view
    void saveAndBack();//set data into task:view
    //bin
//    void load_bin();  //->model
    //刷新使index = 等于下标
    void refreshList();       //每次增删的时候都要刷新一下->model
    void refreshListAlive(); //->view_main
    void refreshListDelete();//->model_bin
}
