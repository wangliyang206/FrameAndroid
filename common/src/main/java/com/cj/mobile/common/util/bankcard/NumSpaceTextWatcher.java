package com.cj.mobile.common.util.bankcard;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 包名： com.cj.mobile.common.ui
 * 对象名： NumSpaceTextWatcher
 * 描述：类银行卡4位插入一空格监听
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/6/28 9:22
 */

public class NumSpaceTextWatcher implements TextWatcher {

    public interface NumSpaceInputOverListener {
        void onOver();
    }

    private static final int DEFAULT_OFFSET = 4;
    // 目标输入框
    private final EditText mDesTxt;
    // 偏移量(几位插入一空格)
    private int mOffset;

    // 记录目标字符串
    private StringBuffer mBuffer = new StringBuffer();
    // 改变之前的文本长度
    private int mBeforeTextLength;
    // 改变之后的文本长度
    private int mOnTextLength;
    // 改变之前去除空格的文本长度
    private int mBeforeNumTxtLength;
    // 改变之后去除空格的文本长度
    private int mNumTxtLength;
    // 目标 光标的位置
    private int mLocation = 0;
    // 之前 光标的位置(可判断用户是否做删除操作)
    private int mBeforeLocation = 0;
    // 改变前有多少空格
    private int mBeforeSpaceNumber = 0;

    // 是否选中空格覆盖
    private boolean isOverrideSpace;
    // 被覆盖的空格数
    private int mOverrideSpaceNum;
    // 是否是粘贴(此粘贴非彼粘贴)
    private boolean isPaste;
    // 复制的字符数(不包括空格)
    private int mPasteNum;
    // 是否需要进行格式化字符串操作
    private boolean isChanged = false;

    //输入完毕
    public NumSpaceInputOverListener overListener;

    //设置输入完毕后的过程
    public void setNumSpaceInputOverListener(NumSpaceInputOverListener overListener) {
        this.overListener = overListener;
    }

    public NumSpaceTextWatcher(@NonNull EditText target) {
        this(target, DEFAULT_OFFSET);
    }

    public NumSpaceTextWatcher(@NonNull EditText target, int offset) {
        mDesTxt = target;
        mOffset = offset;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mBeforeTextLength = s.length();
        mBeforeNumTxtLength = s.toString().replaceAll(" ", "").length();
        mBeforeLocation = mDesTxt.getSelectionEnd();

        // 重置mBuffer
        if (mBuffer.length() > 0) {
            mBuffer.delete(0, mBuffer.length());
        }
        // 计算改变前空格的个数
        mBeforeSpaceNumber = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                mBeforeSpaceNumber++;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mOnTextLength = s.length();
        mNumTxtLength = s.toString().replaceAll(" ", "").length();

        // 判断是否是粘贴,其中粘贴小于offset位的不做判断,并且offset>2判断才有意义
        if (mOffset >= 2 && count >= mOffset) {
            isPaste = true;
            mPasteNum = count;
        } else {
            isPaste = false;
            mPasteNum = 0;
        }

        // 若是经过afterTextChanged方法，则直接return
        if (isChanged) {
            isChanged = false;
            return;
        }

        // 若改变后长度小于等于mOffset - 1，则直接return
        if (mOnTextLength <= mOffset - 1) {
            isChanged = false;
            return;
        }

        // 若改变前后长度一致，并且数字位数相同，则isChanged为false
        // (数字位数相同是防止用户单选空格后输入数字)
        if (mBeforeTextLength == mOnTextLength && mBeforeNumTxtLength == mNumTxtLength) {
            isChanged = false;
            return;
        } else {
            isChanged = true;
        }

        // 若要进行格式化，则判断该情况
        // 判断是否选中空格覆盖(排除删除空格的情况)
        if (before == 1 && count == 0) {
            isOverrideSpace = false;
        } else {
            isOverrideSpace = mBeforeTextLength - mBeforeSpaceNumber - before + count != mNumTxtLength;
        }
        // 若是该情况，计算覆盖空格的个数
        if (isOverrideSpace) {
            mOverrideSpaceNum = mNumTxtLength - (mBeforeTextLength - mBeforeSpaceNumber - before + count);
        } else {
            mOverrideSpaceNum = 0;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isChanged) {
            mLocation = mDesTxt.getSelectionEnd();
            // 去除空格
            mBuffer.append(s.toString().replace(" ", ""));

            // 格式化字符串，mOffset位加一个空格
            int index = 0;
            int mAfterSpaceNumber = 0;
            while (index < mBuffer.length()) {
                if (index == mOffset * (1 + mAfterSpaceNumber) + mAfterSpaceNumber) {
                    mBuffer.insert(index, ' ');
                    mAfterSpaceNumber++;
                }
                index++;
            }

            // 判断是否是粘贴键入
            if (isPaste) {
                mLocation += mPasteNum / mOffset;
                isPaste = false;
                // 判断是否是选中空格输入
            } else if (isOverrideSpace) {
                mLocation += mOverrideSpaceNum;
                // 判断此时光标是否在特殊位置上
            } else if (mLocation % (mOffset + 1) == 0) {
                // 是键入OR删除
                if (mBeforeLocation <= mLocation) {
                    mLocation++;
                } else {
                    mLocation--;
                }
            }

            // 若是删除数据刚好删除一位，前一位是空格，mLocation会超出格式化后字符串的长度(因为格
            // 式化后的长度没有不包括最后的空格)，将光标移到正确的位置
            String str = mBuffer.toString();
            if (mLocation > str.length()) {
                mLocation = str.length();
            } else if (mLocation < 0) {
                mLocation = 0;
            }
            s.replace(0, s.length(), str);
            Editable editable = mDesTxt.getText();
            Selection.setSelection(editable, mLocation);
            isChanged = false;
        }

        if (overListener != null) {
            overListener.onOver();
        }
    }
}
