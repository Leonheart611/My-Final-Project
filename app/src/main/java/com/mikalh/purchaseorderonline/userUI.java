package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.ItemAdapter;
import com.mikalh.purchaseorderonline.Model.Item;

public class userUI extends AppCompatActivity implements ItemAdapter.OnItemSelectedListener {
    FloatingActionButton addItem;
    RecyclerView myRecyler;
    Query query;
    FirebaseFirestore firestore;
    //FirestoreRecyclerAdapter adapter;

    ItemAdapter adapter;
    public static final String KEY_ITEM_ID = "keyItemID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ui);
        myRecyler = findViewById(R.id.myRecyler);
        firestore = FirebaseFirestore.getInstance();
        query = FirebaseFirestore.getInstance().collection("Items")
                .limit(50);

       /* FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query,Item.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Item, ItemHolder>(options) {

            @Override
            public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_list_items,parent,false);
                return new ItemHolder(view);
            }

            @Override
            protected void onBindViewHolder(ItemHolder holder, int position, final Item model) {
               holder.namaBarang_list.setText(model.getNama_barang());
               holder.jenisBarang_list.setText(model.getJenis_barang());
               holder.hargaBarang_list.setText(model.getHarga_barang()+"");
               holder.myConstrain.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                    Intent i = new Intent(userUI.this, detailItem.class);
                    //i.putExtra(KEY_ITEM_ID,)
                   }
               });
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("Error kenapa :",e.getMessage());
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
            }
        };*/
       adapter = new ItemAdapter(query,this){
           @Override
           protected void onDataChanged() {
               super.onDataChanged();
           }

           @Override
           public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
               super.onBindViewHolder(holder, position);
           }

           @Override
           public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               return super.onCreateViewHolder(parent, viewType);
           }

           @Override
           protected void onError(FirebaseFirestoreException e) {
               super.onError(e);
               Log.e("Error Adapter",e.getMessage());
           }
       };



        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myRecyler.setLayoutManager(llm);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(myRecyler.getContext(),llm.getOrientation());
        myRecyler.addItemDecoration(itemDecoration);
        myRecyler.setAdapter(adapter);


        addItem = findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(userUI.this, com.mikalh.purchaseorderonline.addItem.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        if (adapter != null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null){
            adapter.stopListening();
        }
    }

    @Override
    public void onItemSelected(DocumentSnapshot item) {
        Intent i = new Intent(this,detailItem.class);
        i.putExtra(KEY_ITEM_ID,item.getId());
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_ui,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.signOut:
                signOut();
                return true;
            case R.id.profile:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(userUI.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        TextView namaBarang_list, jenisBarang_list, hargaBarang_list;
        ConstraintLayout myConstrain;
        public ItemHolder(View itemView) {
            super(itemView);
            namaBarang_list = itemView.findViewById(R.id.namaBarang_list);
            jenisBarang_list = itemView.findViewById(R.id.jenisBarang_list);
            hargaBarang_list = itemView.findViewById(R.id.hargaBarang_list);
            myConstrain = itemView.findViewById(R.id.myConstrain);
        }
    }

}
