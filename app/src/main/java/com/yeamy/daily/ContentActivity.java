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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yeamy.daily.app.BaseActivity;
import com.yeamy.daily.data.DataBase;
import com.yeamy.daily.data.HandleTask;
import com.yeamy.daily.data.Mission;

/**
 * 修改FinishTime,无需提示,直接保存
 */
public class ContentActivity extends BaseActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {
    public int RESULT_OK;
    public static final int RESULT_EDIT = -1;
    public static final int RESULT_DEL = -2;
    public static final int RESULT_ADD = -3;
    public static final String EXTRA_MISSION = "mission";
    private Mission mission;
    private TextView content, startTime, finishTime;
    private Intent result = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
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

        mission = (Mission) getIntent().getSerializableExtra(EXTRA_MISSION);
        if (mission == null) {
            RESULT_OK = RESULT_ADD;
            startActivityForResult(new Intent(this, EditActivity.class), 0);
            overridePendingTransition(0, 0);
        } else {
            RESULT_OK = RESULT_EDIT;
            setData(mission);
            result.putExtra(EXTRA_MISSION, mission);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            String txt = data.getStringExtra(EditActivity.EXTRA_TXT);
            if (mission == null) {
                mission = new Mission();
                mission.content = txt;
                setData(mission);
                HandleTask.add(this, mission);
                result.putExtra(EXTRA_MISSION, mission);
            } else {
                mission.content = txt;
                content.setText(txt);
                ContentValues values = new ContentValues();
                values.put(DataBase.CONTENT, txt);
                HandleTask.update(this, mission, values);
            }
            setResult(RESULT_OK, result);
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
            finishTime.setText(getStrikeText(startDate));
        }
        content.setText(mission.content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.id.delete, 0, R.string.delete);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                HandleTask.del(this, mission);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_MISSION, mission);
                setResult(RESULT_DEL, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab: {
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(EditActivity.EXTRA_TXT, mission.content);
                startActivityForResult(intent, 0);
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
        setResult(RESULT_OK, result);
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
