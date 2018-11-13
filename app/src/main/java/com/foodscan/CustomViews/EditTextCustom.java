package com.foodscan.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.foodscan.R;
import com.foodscan.Utility.Typeface;


@SuppressLint("AppCompatCustomView")
public class EditTextCustom extends EditText {

    private static final String TAG = EditTextCustom.class.getSimpleName();

    public EditTextCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextCustom(Context context) {
        super(context);
        init();
    }

    private void init() {
        android.graphics.Typeface tf = android.graphics.Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
        setTypeface(tf, 0);

    }

//
//    private void setCustomFont(Context context, AttributeSet attrs) {
//        try {
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFont);
//            String customFont = typedArray.getString(R.styleable.CustomFont_font);
//            if (customFont != null && customFont.length() > 0) {
//                setTypeface(Typeface.getTypeface(context, customFont));
//            }
//            typedArray.recycle();
//        } catch (Exception e) {
//            Log.e(TAG, "setCustomFont Exception : " + e.toString());
//        }
//    }
}