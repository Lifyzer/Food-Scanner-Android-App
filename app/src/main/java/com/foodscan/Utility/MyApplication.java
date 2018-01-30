package com.foodscan.Utility;

import android.app.Application;
import android.graphics.Typeface;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by c49 on 29/07/16.
 */
public class MyApplication extends Application {
    String DBNAME = "FoodScan.realm";
    public static Typeface fontNG;
    //private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        //context=getApplicationContext();
        // The Realm file will be located in Context.getFilesDir() with name "default.realm"

        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder().name(DBNAME)
                .build();


        Realm realm = Realm.getInstance(config);


    }

//    public static Context getContext() {
//        return context;
//    }
}
