package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.utils.OcrResultParser;

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
        return getText(mLeftText);
    }

    private String getText(EditText text) {
        return TextUtils.isEmpty(text.getText().toString()) ?
                String.valueOf(OcrResultParser.ERROR_VALUE) : text.getText().toString();
    }

    public void setLeftText(float value) {
        mLeftText.setText(getStringForText(value));
    }

    public String getRightText() {
        return getText(mRightText);
    }

    public void setRightText(float value) {
        mRightText.setText(getStringForText(value));
    }

    private String getStringForText(float value) {
        return (value < 0) ? null : String.valueOf(Math.round(value));
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    /**
     * Returns true if either one of EditText fields have a value.
     */
    public boolean isEdited() {
        return (!mLeftText.getText().toString().trim().isEmpty() ||
                !mRightText.getText().toString().trim().isEmpty());
    }
}
