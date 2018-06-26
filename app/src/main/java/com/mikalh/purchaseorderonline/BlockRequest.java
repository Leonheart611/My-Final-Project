package com.mikalh.purchaseorderonline;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
    CustomDialog customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("BlockRequest").whereEqualTo("Done",false);
        setContentView(R.layout.activity_block_request);
        customDialog = new CustomDialog(this);
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
        DividerItemDecoration itemDecoration = new DividerItemDecoration(blockRequestRV_admin.getContext(), llm.getOrientation());
        blockRequestRV_admin.setLayoutManager(llm);
        blockRequestRV_admin.addItemDecoration(itemDecoration);
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
        String namaPerusahaan = data.get("NamaPerusahaan").toString();
        String alasan = data.get("Alasan").toString();
        String id = data.getId();
        String idPerusahaan = data.get("BlockedID").toString();
        popUpAccept(namaPerusahaan,idPerusahaan,alasan,id);
    }
    void popUpAccept(String namaPerusahaan, final String IDBlockPerusahaan, String alasan, final String id){
        final Dialog dialog = new Dialog(this);
        /* dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        dialog.setContentView(R.layout.accept_block_request);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView namaPerusahaan_aceptAdmin = dialog.findViewById(R.id.namaPerusahaan_aceptAdmin);
        namaPerusahaan_aceptAdmin.setText(namaPerusahaan);
        TextView reason_aceptAdmin = dialog.findViewById(R.id.reason_aceptAdmin);
        reason_aceptAdmin.setText(alasan);
        Button submit_aceptAdmin = dialog.findViewById(R.id.submit_aceptAdmin);
        submit_aceptAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                customDialog.show();
                firestore.collection("BlockRequest").document(id).update("Done",true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            firestore.collection("Users").document(IDBlockPerusahaan).update("Block",true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        customDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"User Blocked",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Crashlytics.logException(e);
                                    customDialog.dismiss();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Crashlytics.logException(e);
                        customDialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }
}
