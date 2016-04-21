package io.github.the28awg.buckler;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;

public class Workspace extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InteractionListener {

    private HashMap<Class<? extends HelperFragment>, HelperFragment> fragments = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                backPressed();
            }
        } else {
            backPressed();
        }
    }

    private void backPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            new MaterialDialog.Builder(this)
                    .title(R.string.title_quit)
                    .content(R.string.content_quit)
                    .positiveText(R.string.agree_quit)
                    .negativeText(R.string.disagree_quit)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void title(@StringRes int title) {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(title);
        }
    }

    @Override
    public void title(String title) {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(title);
        }
    }

    @Override
    public String title() {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            return String.valueOf(bar.getTitle());
        }
        return "null";
    }

    @Override
    public <T extends HelperFragment> T fragment(Class<T> tClass) {
        if (fragments.containsKey(tClass)) {
            try {
                T fragment = tClass.cast(fragments.get(tClass));
                if (fragment != null) {
                    return fragment;
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        try {
            T fragment = tClass.newInstance();
            fragments.put(tClass, fragment);
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T extends HelperFragment> void show(Class<T> tClass) {
        try {
            T fragment = tClass.cast(getSupportFragmentManager().findFragmentByTag(tClass.getName()));
            if (fragment == null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.root_container, fragment(tClass), tClass.getName());
                transaction.commit();
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
