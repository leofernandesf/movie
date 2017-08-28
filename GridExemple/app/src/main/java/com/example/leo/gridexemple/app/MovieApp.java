package com.example.leo.gridexemple.app;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by leo on 30/05/17.
 */

public class MovieApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
