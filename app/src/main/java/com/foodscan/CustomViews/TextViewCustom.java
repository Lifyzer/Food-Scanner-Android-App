package com.foodscan.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.foodscan.R;


@SuppressLint("AppCompatCustomView")
public class TextViewCustom extends TextView {

    private static final String TAG = TextViewCustom.class.getSimpleName();


    public TextViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewCustom(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/arial.ttf");
        setTypeface(tf ,0);

    }

//    public TextViewCustom(Context context) {
//        super(context);
//    }
//
//    public TextViewCustom(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        //setCustomFont(context, attrs);
//    }
//
//    public TextViewCustom(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        //setCustomFont(context, attrs);
//    }

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