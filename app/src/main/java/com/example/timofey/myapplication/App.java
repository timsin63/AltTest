package com.example.timofey.myapplication;

import android.app.Application;

import com.example.timofey.myapplication.database.DaoMaster;
import com.example.timofey.myapplication.database.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by timofey on 19.02.2017.
 */

public class App extends Application {

    public static final boolean ENCRYPTED = true;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");

        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
