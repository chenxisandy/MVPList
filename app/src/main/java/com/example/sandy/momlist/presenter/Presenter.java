package com.example.sandy.momlist.presenter;

import android.app.Activity;

import com.example.sandy.momlist.Task;
import com.example.sandy.momlist.model.IModel;
import com.example.sandy.momlist.model.Model;
import com.example.sandy.momlist.view.IAdapterView;
import com.example.sandy.momlist.view.IBinView;
import com.example.sandy.momlist.view.IEditView;
import com.example.sandy.momlist.view.IMainView;

import java.util.List;

public class Presenter implements IPresenter {

    private IModel model;

    private IMainView mainView;

    private IBinView binView;

    private IEditView editView;

    private IAdapterView adapterView;

    @Override
    public void loadData() {
        model.loadData();
    }

    @Override
    public void dialMe(Activity activity) {
        model.dialZXX(activity);
    }


    @Override
    public void getPosition(int type) {

    }

    @Override
    public void goToEdit(int type, Task task) {
        switch (type) {
            case 1:
                mainView.gotoEdit(task);
                break;
            case 2:
                adapterView.GoEdit(task);
                break;
                default:
                    break;
        }
    }

    @Override
    public void showHint() {
        mainView.giveHint(model.randomHints());
    }

    @Override
    public void refreshSort() {
        mainView.refreshSort();
    }

    @Override
    public void notifyAdapter() {
        mainView.notifyAdapter();
    }

    @Override
    public void backNav() {
        mainView.backNav();
    }

    @Override
    public void litePalSave() {
        model.litePalSave();
    }

    @Override
    public void recoverTask(int index, List adapterList) {
        model.recoverTask(index, adapterList);
    }

    @Override
    public void deleteTaskForever(int position, List adapterList) {
        model.deleteData(position, adapterList);
    }

    @Override
    public void taskToBin(Task task, int position) {
        adapterView.TaskToBin(task, position);
    }

    @Override
    public Task getTask() {
        return editView.getTask();
    }

    @Override
    public void setEdit() {
        editView.setEdit();
    }

    @Override
    public void saveAndBack() {
        editView.backAndSave();
    }

    @Override
    public void refreshList() {
        model.refreshList();
    }

    @Override
    public void refreshListAlive() {
        mainView.refreshListAlive();
    }

    public Presenter(IMainView mainView, IBinView binView, IEditView editView, IAdapterView adapterView) {
        this.mainView = mainView;
        this.binView = binView;
        this.editView = editView;
        this.adapterView = adapterView;
        this.model = new Model();
    }

    @Override
    public void refreshListDelete() {
        binView.refreshListDelete();

    }
}
