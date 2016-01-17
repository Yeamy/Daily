package com.yeamy.daily;

import static com.yeamy.daily.TimelineFragment.RESULT_ADD;
import static com.yeamy.daily.TimelineFragment.EXTRA_MISSION;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.yeamy.daily.app.BaseActivity;
import com.yeamy.daily.data.HandleTask;
import com.yeamy.daily.data.Mission;

public class AddActivity extends BaseActivity implements View.OnClickListener {
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        edit = (EditText) findViewById(R.id.edit);
        findViewById(R.id.fab).setOnClickListener(this);

        setTitle(R.string.title_add);
    }

    @Override
    public void onClick(View v) {
        String txt = edit.getText().toString();
        if (txt.length() == 0) {
            Snackbar.make(edit, R.string.empty_text, Snackbar.LENGTH_LONG).show();
            return;
        }
        final Mission mission = new Mission();
        mission.content = txt;
        HandleTask.add(this, mission, new Runnable() {
            @Override
            public void run() {
                Intent data = new Intent();
                data.putExtra(EXTRA_MISSION, mission);
                setResult(RESULT_ADD, data);
                finish();
            }
        });
    }

}
