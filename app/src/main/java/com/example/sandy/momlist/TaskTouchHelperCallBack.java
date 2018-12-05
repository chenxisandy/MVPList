package com.example.sandy.momlist;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class TaskTouchHelperCallBack extends ItemTouchHelper.Callback { //监听者模式

    private onMoveAndSwipeListener listenerAdapter; //靠这个接口在 活动里面实现具体的删除

    public TaskTouchHelperCallBack(onMoveAndSwipeListener listenerAdapter) {//构造函数
        this.listenerAdapter = listenerAdapter;
    }
    /*
    * 支持任意位置对之进行操作滑动
    * */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
    /*支持长安拖动
    * */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {//ToDo to be understand
            final int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlag, swipeFlag);

        //为什么不可以直接写底下？这么坑的吗？
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        if (viewHolder.getItemViewType() != viewHolder1.getItemViewType()) {
            return false;
        }
        // Notify the adapter of the move(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
        listenerAdapter.onItemMove(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        listenerAdapter.onItemDelete(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof onStateChangedListener) {
                ((onStateChangedListener)viewHolder).onItemSelect();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof onStateChangedListener) {
            ((onStateChangedListener)viewHolder).onItemClear();
        }
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            final float alpha;
            if (dX > 0.0) {
                alpha = (float) 1.0000 - dX/viewHolder.itemView.getWidth();
            } else {
                alpha = (float) (1.0000 + dX/viewHolder.itemView.getWidth());
            }
            viewHolder.itemView.setAlpha(alpha);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
