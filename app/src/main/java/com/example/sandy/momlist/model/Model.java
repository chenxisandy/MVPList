package com.example.sandy.momlist.model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.example.sandy.momlist.MainActivity;
import com.example.sandy.momlist.Task;
import com.example.sandy.momlist.presenter.IPresenter;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Random;

public class Model implements IModel {

    private IPresenter presenter;


    @Override
    public void loadData() {  //数据库待最后解除封印
//        LitePal.getDatabase();
//        MainActivity.taskList = DataSupport.findAll(Task.class);
    }

    @Override
    public int randomHints() { //得到随机提示
        Random random = new Random();
        int index = random.nextInt(8);
        return index;
    }

    @Override
    public void dialZXX(Activity activity) {  //dial
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:18273423738"));
        activity.startActivity(intent);
    }

    @Override
    public void getPosition() {
// TODO: 2018/12/1 we need to add the fun of get position of mine
    }

//    @Override
//    public void getDataBack(int index, Task task) {
//
//    }

    @Override
    public void litePalSave() { //数据库待解封
        // TODO: 2018/12/1  to add
//        Task.saveAll(MainActivity.taskList);
    }

    @Override
    public void deleteData(int position, List adapterList) { // TODO: 2018/12/1 add data delete by lite pal
        MainActivity.taskList.remove(adapterList.get(position));
        adapterList.remove(position);
    }

    @Override
    public void recoverTask(int index, List<Task> adapterList) {
        adapterList.get(index).setDelete(false);
        // TODO: 2018/11/27 待检验会不会出事 下一行
        MainActivity.taskList.get(MainActivity.taskList.indexOf(adapterList.get(index)));
        adapterList.remove(index);
    }

    @Override
    public void refreshList() {
        for (Task t :
                MainActivity.taskList) {
            t.setIndex(MainActivity.taskList.indexOf(t));
        }
    }
}
