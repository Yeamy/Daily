package com.yeamy.daily.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yeamy.daily.R;
import com.yeamy.daily.data.DataList;

public class DividerDecoration extends RecyclerView.ItemDecoration {
    private int dividerMargin, dividerHeight;
    private Paint paint;

    public DividerDecoration(Context context) {
        Resources res = context.getResources();
        this.dividerMargin = res.getDimensionPixelSize(R.dimen.divider_margin_left);
        this.dividerHeight = res.getDimensionPixelSize(R.dimen.divider_height);
        this.paint = new Paint();
        paint.setColor(0xffdddddd);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, dividerHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            int position = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
            draw(c, child, position);
        }
    }

    private void draw(Canvas c, View child, int position) {
//        int left = (data != null && data.isDivide(position)) ? 0 : dividerMargin;
        int left = dividerMargin;
        int top = child.getBottom();
        int right = child.getWidth();
        int bottom = top + dividerHeight;
        c.drawRect(left, top, right, bottom, paint);
    }

}
