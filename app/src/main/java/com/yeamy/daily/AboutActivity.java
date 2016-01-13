package com.yeamy.daily;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import com.yeamy.daily.app.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView tv = (TextView) findViewById(R.id.app_name);
        String app_name = getString(R.string.app_name);
        SpannableStringBuilder txt = new SpannableStringBuilder(app_name);

        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionName = " v" + versionName;
            txt.append(versionName);
            txt.setSpan(new RelativeSizeSpan(0.6f), app_name.length(), txt.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv.append(txt);
    }
}
