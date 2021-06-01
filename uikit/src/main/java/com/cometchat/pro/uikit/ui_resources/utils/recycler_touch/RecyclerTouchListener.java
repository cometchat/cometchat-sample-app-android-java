package com.cometchat.pro.uikit.ui_resources.utils.recycler_touch;

import android.content.Context;
import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.uikit.R;

public class RecyclerTouchListener extends ItemTouchHelper.SimpleCallback implements RecyclerView.OnItemTouchListener {
    private ClickListener clickListener;
    private GestureDetector gestureDetector;
    private RecyclerItemSwipeListener swipeListener;

    public RecyclerTouchListener(RecyclerView recyclerView, int dragDirs, int swipeDirs, RecyclerItemSwipeListener listener) {
        super(dragDirs, swipeDirs);
        this.swipeListener = listener;
    }

    public RecyclerTouchListener(Context var1, final RecyclerView var2, final ClickListener var3) {
        super(0,0);
        this.clickListener = var3;
        this.gestureDetector = new GestureDetector(var1, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent var1) {
                return true;
            }

            public void onLongPress(MotionEvent var1) {
                View var2x = var2.findChildViewUnder(var1.getX(), var1.getY());
                if (var2x != null && var3 != null) {
                    var3.onLongClick(var2x, var2.getChildPosition(var2x));
                }

            }
        });
    }

    public boolean onInterceptTouchEvent(RecyclerView var1, MotionEvent var2) {
        View var3 = var1.findChildViewUnder(var2.getX(), var2.getY());
        if (var3 != null && this.clickListener != null && this.gestureDetector.onTouchEvent(var2)) {
            this.clickListener.onClick(var3, var1.getChildPosition(var3));
        }

        return false;
    }

    public void onTouchEvent(RecyclerView var1, MotionEvent var2) {
    }

    public void onRequestDisallowInterceptTouchEvent(boolean var1) {
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = viewHolder.itemView.findViewById(R.id.view_foreground);
            if (foregroundView!=null)
                getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = viewHolder.itemView.findViewById(R.id.view_foreground);
        if (foregroundView!=null)
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = viewHolder.itemView.findViewById(R.id.view_foreground);
        if (foregroundView!=null)
            getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = viewHolder.itemView.findViewById(R.id.view_foreground);
        if (foregroundView!=null)
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        swipeListener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemSwipeListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }

}
