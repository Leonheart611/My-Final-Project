package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.R;

public class POActiveAdapter extends FirestoreAdapter<POActiveAdapter.POActive>{
    public interface OnPOSelectedListener {

        void onPOSelectedListener(DocumentSnapshot transaction);
    }
    private OnPOSelectedListener mlistener;
    public POActiveAdapter(Query query, OnPOSelectedListener listener) {
        super(query);
        mlistener = listener;
    }

    @Override
    public POActive onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.po_active_list,parent,false);
        return new POActive(v);
    }

    @Override
    public void onBindViewHolder(POActive holder, int position) {
        holder.bind(getSnapshot(position),mlistener);
    }

    public static class POActive extends RecyclerView.ViewHolder{
        TextView companyName_Active,itemCount_Active;
        public POActive(View itemView) {
            super(itemView);
            companyName_Active = itemView.findViewById(R.id.companyName_Active);
            itemCount_Active = itemView.findViewById(R.id.itemCount_Active);
        }
        public void bind(final DocumentSnapshot snapshot
                , final OnPOSelectedListener listener){
            String namaPerusahaan = snapshot.get("namaPerusahaan").toString();
            String banyakBarang = snapshot.get("BanyakData").toString();
            companyName_Active.setText(namaPerusahaan);
            itemCount_Active.setText(banyakBarang);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onPOSelectedListener(snapshot);
                    }
                }
            });
        }
    }
}
