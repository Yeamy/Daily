package com.yeamy.daily.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yeamy.daily.R;

import java.util.Calendar;

public class NavigationTopView extends ImageView {

    public NavigationTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        setScaleType(ScaleType.CENTER_CROP);
        setImageResource(hour < 18 ? R.mipmap.nav_top_day : R.mipmap.nav_top_night);
    }
}
