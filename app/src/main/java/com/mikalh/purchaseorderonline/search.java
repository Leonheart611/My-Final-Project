package com.mikalh.purchaseorderonline;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toolbar;

import com.mikalh.purchaseorderonline.Pager.SearchPagger;

public class search extends AppCompatActivity implements SearchView.OnQueryTextListener, TabLayout.OnTabSelectedListener {
    private Toolbar searchToolBar;
    ViewPager mySearch_viewPagger;
    SearchPagger adapter;
    TabLayout searchTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Search");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mySearch_viewPagger = findViewById(R.id.mySearch_viewPagger);
        searchTab = findViewById(R.id.search_tab);
        adapter = new SearchPagger(getSupportFragmentManager(),2);
        mySearch_viewPagger.setAdapter(adapter);
        mySearch_viewPagger.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(searchTab));
        searchTab.setOnTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchbar,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();


        return true;
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putString("Hello","World");
        startSearch(null,false,appData,false);
        return super.onSearchRequested();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }
    public String handleIntent(Intent intent){
        String query ="";
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            query = intent.getStringExtra(SearchManager.QUERY);

            return query;
        }else {
            return query;
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mySearch_viewPagger.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
