package com.yeamy.daily.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ScrollBottomListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            if (visibleItemCount > 0) {
                int lastPosition = layoutManager.getItemCount() - 1;
                int count = layoutManager.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = layoutManager.getChildAt(i);
                    RecyclerView.LayoutParams pm = (RecyclerView.LayoutParams) child.getLayoutParams();
                    if (pm.getViewAdapterPosition() >= lastPosition) {
                        onScrollBottom(recyclerView);
                    }
                }
            }
        }
    }

    public void onScrollBottom(RecyclerView recyclerView) {
    }
}
