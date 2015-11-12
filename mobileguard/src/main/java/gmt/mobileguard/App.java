package gmt.mobileguard;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Project: StudyDemo
 * Package: gmt.mobileguard
 * Created by Genment at 2015/11/7 16:20.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        LeakCanary.install(this);
    }
}
