package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toolbar;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.ItemAdapter;

public class Search extends AppCompatActivity implements ItemAdapter.OnItemSelectedListener, SearchView.OnQueryTextListener {
    private Toolbar searchToolBar;
    TabLayout searchTab;
    Query query,queryKategoriBB,queryKategoriBM;
    FirebaseFirestore firestore;
    FirebaseUser user;
    FirebaseAuth auth;
    ItemAdapter itemAdapter,itemAdapterKategoriBarangBaku,itemAdapterKategoriBarangMentah;
    RecyclerView itemRV_search,barangMentah_RV,barangBaku_rv;
    public static final String KEY_ITEM_ID = "keyItemID";
    public static final String QUERYSEARCH = "Search";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Search");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        query = firestore.collection("Items").orderBy("nama_barang", Query.Direction.DESCENDING);
        queryKategoriBB = firestore.collection("Items").whereEqualTo("kategori","Barang Baku").orderBy("nama_barang", Query.Direction.DESCENDING);
        queryKategoriBM = firestore.collection("Items").whereEqualTo("kategori","Barang Mentah").orderBy("nama_barang", Query.Direction.DESCENDING);
        itemRV_search = findViewById(R.id.itemRV_search);
        barangBaku_rv = findViewById(R.id.barangBaku_rv);
        barangMentah_RV = findViewById(R.id.barangMentah_RV);
        itemAdapterKategoriBarangBaku = new ItemAdapter(queryKategoriBB,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
            }
        };
        itemAdapterKategoriBarangMentah = new ItemAdapter(queryKategoriBM,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
            }
        };
        itemAdapter = new ItemAdapter(query,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Crashlytics.logException(e);
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

        };
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManagaer2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManagaer3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        itemRV_search.setLayoutManager(horizontalLayoutManagaer);
        barangMentah_RV.setLayoutManager(horizontalLayoutManagaer2);
        barangBaku_rv.setLayoutManager(horizontalLayoutManagaer3);
        itemRV_search.setItemAnimator(new DefaultItemAnimator());
        barangMentah_RV.setItemAnimator(new DefaultItemAnimator());
        barangBaku_rv.setItemAnimator(new DefaultItemAnimator());
        itemRV_search.setAdapter(itemAdapter);
        barangMentah_RV.setAdapter(itemAdapterKategoriBarangMentah);
        barangBaku_rv.setAdapter(itemAdapterKategoriBarangBaku);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchbar,menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (itemAdapter != null){
            itemAdapter.startListening();
        }if (itemAdapterKategoriBarangMentah!=null){
            itemAdapterKategoriBarangMentah.startListening();
        }if (itemAdapterKategoriBarangBaku!=null){
            itemAdapterKategoriBarangBaku.startListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (itemAdapter!=null){
            itemAdapter.stopListening();
        }if (itemAdapterKategoriBarangBaku!=null){
            itemAdapterKategoriBarangBaku.stopListening();
        }if (itemAdapterKategoriBarangMentah!=null){
            itemAdapterKategoriBarangMentah.stopListening();
        }
    }

    @Override
    public void onItemSelected(DocumentSnapshot item) {
        Intent i = new Intent(this,detailItem.class);
        i.putExtra(KEY_ITEM_ID,item.getId());
        startActivity(i);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent i = new Intent(Search.this,SearchResult.class);
        i.putExtra(QUERYSEARCH,query);
        startActivity(i);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
