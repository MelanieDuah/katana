package com.katana.ui.support;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.katana.infrastructure.support.ActionFuncWithOneParam;

import java.util.AbstractMap;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;

/**
 * Created by Akwasi Owusu on 1/24/18
 */

public class RecycleItemTouchCallback extends SimpleCallback {

    private ActionFuncWithOneParam swipedAction;

    RecycleItemTouchCallback() {
        super(0, LEFT | RIGHT);
    }

    void setSwipedAction(ActionFuncWithOneParam swipedAction) {
        this.swipedAction = swipedAction;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (swipedAction != null)
            swipedAction.invoke(new AbstractMap.SimpleEntry<>(direction, viewHolder));
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((BindableRecyclerViewAdapter.KatanaRecyclerViewHolder) viewHolder).getForegroundView();
        if (foregroundView != null)
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX / 2, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((BindableRecyclerViewAdapter.KatanaRecyclerViewHolder) viewHolder).getForegroundView();
        if (foregroundView != null)
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX / 2, dY, actionState, isCurrentlyActive);
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        BindableRecyclerViewAdapter adapter = (BindableRecyclerViewAdapter) recyclerView.getAdapter();
        return adapter.getSwipeDirs();
    }
}
