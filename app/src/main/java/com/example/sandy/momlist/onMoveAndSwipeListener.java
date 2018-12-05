package com.example.sandy.momlist;

public interface onMoveAndSwipeListener {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDelete(int position);
}
