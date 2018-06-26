package com.mikalh.purchaseorderonline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.CompanyAdapter;

public class CompanyList_admin extends AppCompatActivity implements CompanyAdapter.OnCompanySelectedListener{
    CompanyAdapter adapter;
    RecyclerView companyListRV_admin;
    FirebaseFirestore firestore;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("Users").orderBy("nama_perusahaan", Query.Direction.ASCENDING);
        setContentView(R.layout.activity_company_list_admin);
        setTitle("Company List");
        companyListRV_admin = findViewById(R.id.companyListRV_admin);
        adapter = new CompanyAdapter(query,this){
            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Crashlytics.logException(e);
            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }
        };
        companyListRV_admin.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(CompanyList_admin.this);
        llm.setSmoothScrollbarEnabled(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(companyListRV_admin.getContext(), llm.getOrientation());
        companyListRV_admin.setLayoutManager(llm);
        companyListRV_admin.addItemDecoration(itemDecoration);
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
    public void onCompanySelected(DocumentSnapshot user) {

    }
}
