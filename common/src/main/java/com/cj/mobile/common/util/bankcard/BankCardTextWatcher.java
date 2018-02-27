package com.cj.mobile.common.util.bankcard;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 包名： com.cj.mobile.common.ui
 * 对象名： BankCardTextWatcher
 * 描述：类银行卡4位插入一空格监听
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/6/28 9:22
 */

public class BankCardTextWatcher implements TextWatcher {
    //default max length = 21 + 5 space
    private static final int DEFAULT_MAX_LENGTH = 21 + 5;
    //max input length
    private int maxLength = DEFAULT_MAX_LENGTH;
    private int beforeTextLength = 0;
    private boolean isChanged = false;

    //space count
    private int space = 0;

    private StringBuffer buffer = new StringBuffer();
    private EditText editText;

    public static void bind(EditText editText) {
        new BankCardTextWatcher(editText, DEFAULT_MAX_LENGTH);
    }

    public static void bind(EditText editText, int maxLength) {
        new BankCardTextWatcher(editText, maxLength);
    }

    public BankCardTextWatcher(EditText editText, int maxLength) {
        this.editText = editText;
        this.maxLength = maxLength;
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeTextLength = s.length();
        if (buffer.length() > 0) {
            buffer.delete(0, buffer.length());
        }
        space = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                space++;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int length = s.length();
        buffer.append(s.toString());
        if (length == beforeTextLength || length <= 3
                || isChanged) {
            isChanged = false;
            return;
        }
        isChanged = true;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isChanged) {
            int selectionIndex = editText.getSelectionEnd();
            //total char length
            int index = 0;
            while (index < buffer.length()) {
                if (buffer.charAt(index) == ' ') {
                    buffer.deleteCharAt(index);
                } else {
                    index++;
                }
            }
            //total space count
            index = 0;
            int totalSpace = 0;
            while (index < buffer.length()) {
                if ((index == 4 || index == 9 || index == 14 || index == 19 || index == 24)) {
                    buffer.insert(index, ' ');
                    totalSpace++;
                }
                index++;
            }
            //selection index
            if (totalSpace > space) {
                selectionIndex += (totalSpace - space);
            }
            char[] tempChar = new char[buffer.length()];
            buffer.getChars(0, buffer.length(), tempChar, 0);
            String str = buffer.toString();
            if (selectionIndex > str.length()) {
                selectionIndex = str.length();
            } else if (selectionIndex < 0) {
                selectionIndex = 0;
            }
            editText.setText(str);
            Editable text = editText.getText();
            //set selection
            Selection.setSelection(text, selectionIndex < maxLength ? selectionIndex : maxLength);
            isChanged = false;
        }

        if(this.overListener != null) {
            this.overListener.onOver();
        }
    }

    public NumSpaceInputOverListener overListener;

    public void setNumSpaceInputOverListener(NumSpaceInputOverListener overListener) {
        this.overListener = overListener;
    }

    public interface NumSpaceInputOverListener {
        void onOver();
    }
}
