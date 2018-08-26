package com.foodscan.Utility;

import android.content.Context;
import android.util.Log;


import com.foodscan.R;

import java.util.Hashtable;

/**
 * Created by c161 on 28/07/16.
 */
public class Typeface {

    private static final String TAG = Typeface.class.getSimpleName();

    private static Hashtable<String, android.graphics.Typeface> hashTypeface = new Hashtable<>();

    public static android.graphics.Typeface getTypeface(Context context, String fontAssetPath) {
        try {
            android.graphics.Typeface typeface = hashTypeface.get(fontAssetPath);

            if (typeface == null) {
                typeface = android.graphics.Typeface.createFromAsset(context.getAssets(), fontAssetPath);
                hashTypeface.put(fontAssetPath, typeface);
            }

            return typeface;
        } catch (Exception e) {
            Log.e(TAG, "getTypeface Exception : " + e.toString());
            return null;
        }
    }

    public static android.graphics.Typeface arialRegular(Context context) {
        return getTypeface(context, context.getString(R.string.font_arial_regular));

    }

    public static android.graphics.Typeface arialbold(Context context) {
        return getTypeface(context, context.getString(R.string.font_arial_bold));
    }

}
