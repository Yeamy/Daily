package com.yeamy.daily;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.yeamy.daily.app.ResourcesCompat;
import com.yeamy.widget.ImageCheckButton;

public class ColorDialog extends AlertDialog implements ImageCheckButton.OnCheckedChangeListener {
    private OnColorSelectedListener l;
    private int[][] kv;

    public ColorDialog(Context context, int color, OnColorSelectedListener l) {
        super(context, R.style.AppTheme_Dialog);
        setTitle(R.string.title_color_dialog);
        View view = View.inflate(context, R.layout.dialog_color, null);
        setView(view);

        int[][] kv = {
                {R.id.color_lens_null, 0},
                {R.id.color_lens_red, ResourcesCompat.getColor(context, R.color.color_lens_red)},
                {R.id.color_lens_yellow, ResourcesCompat.getColor(context, R.color.color_lens_yellow)},
                {R.id.color_lens_green, ResourcesCompat.getColor(context, R.color.color_lens_green)},
                {R.id.color_lens_blue, ResourcesCompat.getColor(context, R.color.color_lens_blue)},
                {R.id.color_lens_purple, ResourcesCompat.getColor(context, R.color.color_lens_purple)},
                {R.id.color_lens_brown, ResourcesCompat.getColor(context, R.color.color_lens_brown)},
                {R.id.color_lens_grey, ResourcesCompat.getColor(context, R.color.color_lens_grey)}
        };
        this.kv = kv;
        this.l = l;
        for (int[] it : kv) {
            ImageCheckButton btn = (ImageCheckButton) view.findViewById(it[0]);
            btn.setOnCheckedChangeListener(this);
            if (color == it[1]) {
                btn.setChecked(true);
            }
        }
    }

    @Override
    public void onCheckedChanged(ImageCheckButton imageView, boolean isChecked) {
        if (isChecked && l != null) {
            int[][] kv = this.kv;
            int id = imageView.getId();
            for (int[] it : kv) {
                if (it[0] == id) {
                    l.onColorSelected(it[1]);
                }
            }
        }
        dismiss();
    }

    public void setOnColorSelectedListener(OnColorSelectedListener l) {
        this.l = l;
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }
}
