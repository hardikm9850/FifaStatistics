package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;

public class AddMatchListItem extends LinearLayout {

    private EditText mLeftText;
    private EditText mRightText;
    private TextView mTitle;

    public AddMatchListItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.match_input_list_item, this);

        mLeftText = (EditText) findViewById(R.id.left_text);
        mRightText = (EditText) findViewById(R.id.right_text);
        mTitle = (TextView) findViewById(R.id.title_text);
    }

    public String getLeftText() {
        return mLeftText.getText().toString();
    }

    public void setLeftText(int value) {
        mLeftText.setText(value);
    }

    public String getRightText() {
        return mRightText.getText().toString();
    }

    public void setRightText(int value) {
        mRightText.setText(value);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }
}
