package com.jeannaclark.android.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.facebook.stetho.Stetho;
import com.jeannaclark.android.stockhawk.R;

/**
 * Created by jeannaclark on 8/10/16.
 */

public class MainActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.vector_drawable_ic_trending_up_white___px);

        if (findViewById(R.id.detail_fragment_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setElevation(0f);
            }
        }

        MainActivityFragment mainActivityFragment = (MainActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_main);
        if (null != mainActivityFragment) {
            mainActivityFragment.updateStocks();
        }

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}