package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.CatalogueAdapter;
import com.mikalh.purchaseorderonline.Adapter.ItemAdapter;
import com.mikalh.purchaseorderonline.Model.User;

public class SearchResult extends AppCompatActivity implements CatalogueAdapter.OnClickCatalogueListener{
    TextView searchText;
    RecyclerView resultSearch_RV;
    FirebaseFirestore firestore;
    FirebaseUser user;
    FirebaseAuth auth;
    Query query;
    String ID,search;
    CatalogueAdapter adapter;
    public static final String KEY_ITEM_ID = "keyItemID";
    public static final String QUERYSEARCH = "Search";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        setTitle("Search Result");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        searchText = findViewById(R.id.searchText);
        resultSearch_RV = findViewById(R.id.resultSearch_RV);
        ID = getIntent().getExtras().getString(DetailChat.IDSeller);
        if (ID != null){
            query = firestore.collection("Items").whereEqualTo("userId",ID);
            firestore.collection("Users").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot snapshot = task.getResult();
                    User user = snapshot.toObject(User.class);
                    searchText.setText("Hasil Search Berdasarkan Perusahaan: "+user.getNama_perusahaan());
                }
            });

        }else {
            search = getIntent().getExtras().getString(Search.QUERYSEARCH);
            query = firestore.collection("Items").whereEqualTo("nama_barang",search);
            searchText.setText("Hasil Pencarian Kata Kunci: "+search);
        }
        adapter = new CatalogueAdapter(query,this){
            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }
        };

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        resultSearch_RV.setLayoutManager(llm);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(resultSearch_RV.getContext(), llm.getOrientation());
        resultSearch_RV.addItemDecoration(itemDecoration);
        resultSearch_RV.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        adapter.stopListening();
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onClickCatalogueListener(DocumentSnapshot item) {
        Intent i = new Intent(this,detailItem.class);
        i.putExtra(KEY_ITEM_ID,item.getId());
        startActivity(i);
    }
}
