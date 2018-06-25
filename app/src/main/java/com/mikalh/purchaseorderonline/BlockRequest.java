package com.mikalh.purchaseorderonline;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.BlockRequestAdapter;

public class BlockRequest extends AppCompatActivity implements BlockRequestAdapter.OnBlockRequestListener{
    RecyclerView blockRequestRV_admin;
    BlockRequestAdapter adapter;
    FirebaseFirestore firestore;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("BlockRequest").whereEqualTo("Done",false);
        setContentView(R.layout.activity_block_request);
        adapter = new BlockRequestAdapter(query,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Crashlytics.logException(e);
            }
        };
        blockRequestRV_admin = findViewById(R.id.blockRequestRV_admin);
        blockRequestRV_admin.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(BlockRequest.this);
        llm.setSmoothScrollbarEnabled(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        blockRequestRV_admin.setLayoutManager(llm);
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
    public void onBlockRequestListener(DocumentSnapshot data) {

    }
    void popUpAccept(){
        final Dialog dialog = new Dialog(this);
        /* dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        dialog.setContentView(R.layout.block_user_submit);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }
}
