package com.yeamy.daily;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yeamy.daily.app.BaseActivity;
import com.yeamy.daily.data.DataBase;
import com.yeamy.daily.data.HandleTask;
import com.yeamy.daily.data.Mission;

import static com.yeamy.daily.TimelineFragment.EXTRA_MISSION;
import static com.yeamy.daily.TimelineFragment.RESULT_DEL;
import static com.yeamy.daily.TimelineFragment.RESULT_EDIT;

/**
 * 修改FinishTime,无需提示,直接保存
 */
public class DetailsActivity extends BaseActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, ColorDialog.OnColorSelectedListener {
    private Mission mission;
    private TextView content, startTime, finishTime;
    private View color;
    private Intent result = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        findViewById(R.id.fab).setOnClickListener(this);

        //startTime
        TextView startTime = (TextView) findViewById(R.id.startTime);
        startTime.setOnClickListener(this);
        this.startTime = startTime;
        //finishTime
        TextView finishTime = (TextView) findViewById(R.id.finishTime);
        finishTime.setOnClickListener(this);
        this.finishTime = finishTime;
        //content
        content = (TextView) findViewById(R.id.content);
        color = findViewById(R.id.color);

        mission = (Mission) getIntent().getSerializableExtra(EXTRA_MISSION);
        setData(mission);
        result.putExtra(EXTRA_MISSION, mission);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            String txt = data.getStringExtra(EditActivity.EXTRA_TXT);
            mission.content = txt;
            content.setText(txt);
            ContentValues values = new ContentValues();
            values.put(DataBase.CONTENT, txt);
            HandleTask.update(this, mission, values);
            setResult(RESULT_EDIT, result);
        } else if (mission == null) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void setData(Mission mission) {
        String startDate = Mission.getDateText(this, mission.startTime);
        startTime.setText(startDate);
        if (mission.isFinish()) {
            finishTime.setText(Mission.getDateText(this, mission.finishTime));
        } else {
            finishTime.setText(getStrikeText(Mission.getDateText(this, System.currentTimeMillis())));
        }
        content.setText(mission.content);
        color.setBackgroundColor(mission.color);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.color:
                new ColorDialog(this, mission.color, this).show();
                return true;
            case R.id.delete:
                HandleTask.del(this, mission);
                setResult(RESULT_DEL, result);
                finish();
                return true;
            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mission.toString(this));
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onColorSelected(int color) {
        mission.color = color;
        this.color.setBackgroundColor(color);
        setResult(RESULT_EDIT, result);
        ContentValues values = new ContentValues();
        values.put(DataBase.COLOR, color);
        HandleTask.update(this, mission, values);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab: {
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(EditActivity.EXTRA_TXT, mission.content);
                intent.putExtra(EditActivity.EXTRA_COLOR, mission.color);
                startActivityForResult(intent, 0);
                overridePendingTransition(0, 0);
                break;
            }
            case R.id.startTime: {
                DatePickerDialog dialog = new DatePickerDialog(this, R.id.startTime,
                        mission.startTime, false);
                dialog.setListener(this);
                dialog.setTitle(R.string.title_start_time_dialog);
                dialog.show();
                break;
            }
            case R.id.finishTime: {
                DatePickerDialog dialog = new DatePickerDialog(this, R.id.finishTime,
                        mission.finishTime, true);
                dialog.setListener(this);
                dialog.setTitle(R.string.title_finish_time_dialog);
                dialog.show();
                break;
            }
            default:
        }
    }

    @Override
    public void onDateSet(int id, boolean enable, long timeMillis, String date) {
        ContentValues values = new ContentValues();
        switch (id) {
            case R.id.finishTime:
                if (enable) {
                    mission.finishTime = timeMillis;
                    finishTime.setText(date);
                } else {
                    mission.finishTime = Long.MAX_VALUE;
                    finishTime.setText(getStrikeText(finishTime.getText()));
                }
                values.put(DataBase.FINISH, mission.finishTime);
                HandleTask.update(this, mission, values);
                break;
            case R.id.startTime:
                mission.startTime = timeMillis;
                startTime.setText(date);
                values.put(DataBase.START, timeMillis);
                HandleTask.update(this, mission, values);
                break;
        }
        setResult(RESULT_EDIT, result);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.show();
        }
    }

    private static CharSequence getStrikeText(CharSequence charSequence) {
        SpannableString txt = new SpannableString(charSequence);
        txt.setSpan(new StrikethroughSpan(), 0, txt.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return txt;
    }

}
