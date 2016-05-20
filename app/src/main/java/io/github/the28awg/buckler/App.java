package io.github.the28awg.buckler;

import android.app.Application;
import android.content.Context;

import io.github.the28awg.buckler.system.sql.Database;
import io.github.the28awg.buckler.system.sql.Sql;

/**
 * Created by the28awg on 20.05.16.
 */
public class App extends Application {

    private static Context context;

    public static Context context() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Sql.sql().initialize(this, Database.database("buckler.db"));

    }
}
