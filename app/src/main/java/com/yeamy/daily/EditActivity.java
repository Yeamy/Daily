package com.yeamy.daily;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.yeamy.daily.app.BaseActivity;

/**
 * 编辑完进入显示模式, 编辑模式显示重置按钮/保存按钮
 */
public class EditActivity extends BaseActivity implements View.OnClickListener {
    public static final String EXTRA_TXT = "txt";
    private String text;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        edit = (EditText) findViewById(R.id.edit);
        findViewById(R.id.fab).setOnClickListener(this);

        text = getIntent().getStringExtra(EXTRA_TXT);
        edit.setText(text);
    }

    @Override
    public void onClick(View v) {
        String txt = edit.getText().toString();
        if (txt.length() == 0) {
            Snackbar.make(edit, R.string.empty_text, Snackbar.LENGTH_LONG).show();
            return;
        }
        if (txt.length() != this.text.length() || !txt.equals(this.text)) {
            Intent data = new Intent();
            data.putExtra(EXTRA_TXT, txt);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.id.restore, 0, R.string.restore).setIcon(R.mipmap.ic_restore_white_24dp)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restore:
                edit.setText(text);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
