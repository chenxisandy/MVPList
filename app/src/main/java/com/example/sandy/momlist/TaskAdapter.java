package com.example.sandy.momlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sandy.momlist.presenter.IPresenter;
import com.example.sandy.momlist.view.IAdapterView;

import java.util.Collections;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements onMoveAndSwipeListener, IAdapterView {

    List<Task> mTaskList;

    private Context mContext;

    IPresenter presenter;

    ViewHolder mViewHolder;

    public TaskAdapter(List<Task> mTaskList) {
        this.mTaskList = mTaskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext = viewGroup.getContext();//这一点特别重要，因为没有context根本活不下去
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.task_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        mViewHolder = viewHolder;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final Task task = mTaskList.get(viewHolder.getAdapterPosition());
        //Task task = mTaskList.get(i);
        viewHolder.name.setText(task.getTitle());//得到标题
        if (task.isDelete()) {
            viewHolder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorGrey));
        } else {
//            viewHolder.cardView.setBackgroundColor(task.getColorFromLevel());    //不可以用它因为它是背景
            viewHolder.cardView.setCardBackgroundColor(task.getColorFromLevel());// 得到对应的颜色
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击即进入编辑界面
                if (!mTaskList.get(i).isDelete()) {
                    presenter.goToEdit(2);
                } else {
                    Toast.makeText(mContext, "将会恢复这条事项：" + mTaskList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                    presenter.recoverTask(i, mTaskList);
                    notifyItemRemoved(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaskList == null ? 0 : mTaskList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mTaskList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDelete(final int position) {
        // Todo 我们在这里只是控制一部分，而且数据是复制按值传递过去的
        // ToDo 而且我们还没有加上litepal的使用
        Task task = mTaskList.get(position);
        if (mTaskList.get(position).isDelete()) { //具体实现
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("删除确认")
                    .setMessage("您确定要彻底删除《" + mTaskList.get(position).getTitle() + "》这条事项吗？")
                    .setNegativeButton("手抖-_-", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "如果您想恢复，点击一下即可", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton("嗯嗯~", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            // TODO: 2018/11/25 还差数据库里面没有删除
                            presenter.deleteTaskForever(position, mTaskList);
                            notifyItemRemoved(position);
                        }
                    })
                    .show();
        } else {
            presenter.taskToBin();
        }
    }

    @Override
    public void GoEdit(Task task) {
        Intent intent = new Intent(mContext, EditActivity.class);

        intent.putExtra(mContext.getResources().getString(R.string.TASK), task);
        intent.putExtra(mContext.getResources().getString(R.string.INDEX), mViewHolder.getAdapterPosition());
//                    intent.putExtra(mContext.getResources().getString(R.string.INDEX), i);

        ((Activity) mContext).startActivityForResult(intent, 2);
        // ToDo add a intent which intents to deliver task to EditActivity
    }

    @Override
    public void TaskToBin(Task task, int  position) {
        task.setDelete(true);
        MainActivity.taskList.get(MainActivity.taskList.indexOf(task));// TODO: 2018/11/27
        mTaskList.remove(position);
        notifyItemRemoved(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements onStateChangedListener {
        TextView name;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.task_name);
            cardView = view.findViewById(R.id.task_card);
        }

        @Override
        public void onItemSelect() {
            itemView.setAlpha((float) 0.5);
        }

        @Override
        public void onItemClear() {
            itemView.setAlpha((float) 1.0);
        }
    }
}
