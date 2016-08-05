package com.htqindonesia.htq;


import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        SurahFragment.OnFragmentInteractionListener,
        ReadSurahFragment.OnFragmentInteractionListener,
        PagesFragment.OnFragmentInteractionListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        SurahFragment surahFragment = new SurahFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.fragment, surahFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass= null;
            fragmentClass = SurahFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            }catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit();

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String url = null;

        Fragment fragment = null;

        Bundle bundle = new Bundle();

        if (id == R.id.nav_Beranda) {
            fragment = new SurahFragment();
        }  else if (id == R.id.nav_Tentang) {
            url = "file:///android_asset/pages/tentang.html";
            fragment = new PagesFragment();
            bundle.putString(PagesFragment.ARG_URL, url);
        } else if (id == R.id.nav_Metode) {
            url = "file:///android_asset/pages/metode.html";
            fragment = new PagesFragment();
            bundle.putString(PagesFragment.ARG_URL, url);
        } else if (id == R.id.nav_Kisah) {
            url = "file:///android_asset/pages/kisah.html";
            fragment = new PagesFragment();
            bundle.putString(PagesFragment.ARG_URL, url);
        } else if (id == R.id.nav_Keistimewaan) {
            url = "file:///android_asset/pages/keistimewaan.html";
            fragment = new PagesFragment();
            bundle.putString(PagesFragment.ARG_URL, url);
        } else if (id == R.id.nav_Donasi) {
            url = "file:///android_asset/pages/donasi.html";
            fragment = new PagesFragment();
            bundle.putString(PagesFragment.ARG_URL, url);
        }

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
