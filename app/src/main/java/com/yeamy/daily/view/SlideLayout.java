package com.yeamy.daily.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SlideLayout extends FrameLayout {
    private GestureDetector detector;
    private Scroller scroller;
    private OnSlideListener listener;


    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        detector = new GestureDetector(context, new GestureListener());
        scroller = new Scroller(context, new LinearInterpolator());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                // 添加结束事件,实现上下回弹
                int width = getWidth();
                int x = getScrollX();
                if (x < 0 && x > -width) {
                    int dx = x < -width / 3 ? -width : 0;
                    scroller.startScroll(x, 0, dx - x, 0);
                    invalidate();
                }
        }
        return detector.onTouchEvent(event);
    }

    public void reset() {
        if (getScrollX() != 0) {
            scrollTo(0, 0);
            scrolled = false;
        }
    }

    private boolean scrolled = false;

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {//判断mScroller滚动是否完成
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        } else if (listener != null) {
            int x = getScrollX();
            if (x == -getWidth() && !scrolled) {
                listener.onSlide(this);
                scrolled = true;
            }
        }
    }

    public void setListener(OnSlideListener listener) {
        this.listener = listener;
    }

    public interface OnSlideListener extends OnClickListener {
        void onSlide(SlideLayout layout);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            scroller.abortAnimation();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (listener != null) {
                listener.onClick(SlideLayout.this);
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            int scroll = (int) (getScrollX() + distanceX);
//            int maxScroll = getWidth();
//            scroll = scroll > maxScroll ? maxScroll : scroll;
//            scroll = scroll < 0 ? 0 : scroll;
//            scrollBy(scroll - getScrollX(), 0);

            getParent().requestDisallowInterceptTouchEvent(true);

//            System.out.println(distanceX);
            int scroll = (int) (getScrollX() + distanceX);
            int maxScroll = -getWidth();
            scroll = scroll < maxScroll ? maxScroll : scroll;
            scroll = scroll > 0 ? 0 : scroll;
            scrollBy(scroll - getScrollX(), 0);
            return true;
        }
    }

}
