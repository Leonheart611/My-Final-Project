package com.mikalh.purchaseorderonline.Adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Model.Item;
import com.mikalh.purchaseorderonline.R;

/**
 * Created by mika.frentzen on 06/02/2018.
 */

public class ItemAdapter extends FirestoreAdapter<ItemAdapter.ViewHolder> {

    public interface OnItemSelectedListener {

        void onItemSelected(DocumentSnapshot item);

    }

    private OnItemSelectedListener mListener;

    public ItemAdapter(Query query, OnItemSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaBarang_list, jenisBarang_list, hargaBarang_list;
        ImageView barangImage_list;

        public ViewHolder(View itemView) {
            super(itemView);
            namaBarang_list = itemView.findViewById(R.id.namaBarang_list);
            hargaBarang_list = itemView.findViewById(R.id.hargaBarang_list);
            barangImage_list = itemView.findViewById(R.id.barangImage_list);

        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnItemSelectedListener listener) {
            Item item = snapshot.toObject(Item.class);
            //Resources resources = itemView.getResources();
            if (!item.getImageItemUrl().isEmpty()) {
                Glide.with(barangImage_list.getContext())
                        .load(item.getImageItemUrl())
                        .into(barangImage_list);
            }
            namaBarang_list.setText(item.getNama_barang());
            hargaBarang_list.setText(item.getHarga_barang()+"");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onItemSelected(snapshot);
                    }
                }
            });
        }
    }
}


