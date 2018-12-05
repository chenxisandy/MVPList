package com.example.sandy.momlist.view;

import com.example.sandy.momlist.Task;

public interface IMainView {

    void gotoEdit(Task task);
    void giveHint(int hint);
    void backNav();
    void refreshSort();
    void refreshListAlive();
    void notifyAdapter();

}
