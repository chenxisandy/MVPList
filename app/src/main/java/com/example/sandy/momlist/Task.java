package com.example.sandy.momlist;

import android.graphics.Color;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task extends DataSupport implements Serializable, Comparable {
    private boolean isDelete;

    private boolean isSetTime;

    private long time;

    private int level;

    private String title;

    private String content;

    private int index;

    private int indexAlive;

    private int indexDelete;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndexAlive() {
        return indexAlive;
    }

    public void setIndexAlive(int indexAlive) {
        this.indexAlive = indexAlive;
    }

    public int getIndexDelete() {
        return indexDelete;
    }

    public void setIndexDelete(int indexDelete) {
        this.indexDelete = indexDelete;
    }

    public Task(boolean isDelete, boolean isSetTime, long time, int level, String title, String content, int index, int indexAlive, int indexDelete) {
        this.isDelete = isDelete;
        this.isSetTime = isSetTime;
        this.time = time;
        this.level = level;
        this.title = title;
        this.content = content;
        this.index = index;
        this.indexAlive = indexAlive;
        this.isDelete = isDelete;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public boolean isSetTime() {
        return isSetTime;
    }

    public long getTime() {
        return time;
    }

    public int getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public void setSetTime(boolean setTime) {
        isSetTime = setTime;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setLevel(int level) {// level have 1, 2, 3. and 2 = default
        this.level = level;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(Object o) {//实现list的排序问题
        Task task = (Task) o;
        if (this.getLevel() > task.getLevel()) {
            return 1;
        } else if (this.getLevel() < task.getLevel()) {
            return -1;
        } else {
            if (this.getTime() > System.currentTimeMillis() && task.getTime() < System.currentTimeMillis()) {
                return 1;
            } else if (this.getTime() < System.currentTimeMillis() && task.getTime() > System.currentTimeMillis()) {
                return -1;
            } else if (this.getTime() > System.currentTimeMillis() && task.getTime() > System.currentTimeMillis()) {
                return (int) ((this.getTime() - task.getTime()) % 1);
            } else {
                return 0;
            }
        }
    }

    // TODO: 2018/11/27 ques to ask
    /*
    * 目前颜色有很大问题，
    * 要问学长
    * */
    public int getColorFromLevel() {
        if ((this.getTime() < System.currentTimeMillis() && this.isSetTime())|| this.getLevel() < 2) {
            return R.color.colorGrey;
        } else {
            switch (this.getLevel()) {//因为直接return 所以不用break;
                case 2:
                    return R.color.colorGreen;
                case 3:
                    return R.color.colorBlue;
                case 4:
                    return R.color.colorPink;
                default:
                    return R.color.colorGrey;
            }
        }
    }

    public static List<Task> getSubList(boolean isDelete, List<Task> mList) {//用来分别得到list里未删除的以及删除的
        List<Task> tasks = new ArrayList<>();
        if (isDelete) {
            for (Task t :
                    mList) {
                if (t.isDelete) {
                    tasks.add(t);
                }
            }
        } else {
            for (Task t :
                    mList) {
                if (!t.isDelete) {
                    tasks.add(t);
                }
            }
        }
        return tasks;
    }
}
