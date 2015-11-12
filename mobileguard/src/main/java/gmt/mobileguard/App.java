package gmt.mobileguard;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import gmt.mobileguard.util.SharedPrefsCtrl;

/**
 * Project: StudyDemo
 * Package: gmt.mobileguard
 * Created by Genment at 2015/11/7 16:20.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        SharedPrefsCtrl.init(this);
        LeakCanary.install(this);
    }
}
