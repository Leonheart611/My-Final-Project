package com.mikalh.purchaseorderonline.Adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Model.Item;
import com.mikalh.purchaseorderonline.R;

/**
 * Created by mika.frentzen on 13/03/2018.
 */

public class CatalogueAdapter extends FirestoreAdapter<CatalogueAdapter.CatalogueHolder> {
    public interface OnClickCatalogueListener{
        void onClickCatalogueListener(DocumentSnapshot item);
    }
    private OnClickCatalogueListener mlistener;

    public CatalogueAdapter(Query query, OnClickCatalogueListener listener) {
        super(query);
        mlistener = listener;
    }

    @Override
    public CatalogueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_my_catalogue,parent,false);
        return new CatalogueHolder(view);
    }

    @Override
    public void onBindViewHolder(CatalogueHolder holder, int position) {
        holder.bind(getSnapshot(position),mlistener);
    }

    public static class CatalogueHolder extends RecyclerView.ViewHolder {
        ImageView img_catalogue;
        TextView namaBarang_catalogue;
        public CatalogueHolder(View itemView) {
            super(itemView);
            img_catalogue = itemView.findViewById(R.id.img_catalogue);
            namaBarang_catalogue = itemView.findViewById(R.id.namaBarang_catalogue);
        }
        public void bind(final DocumentSnapshot snapshot, final OnClickCatalogueListener listener){
            Item item = snapshot.toObject(Item.class);
            if (!item.getImageItemUrl().isEmpty()) {
                Glide.with(img_catalogue.getContext())
                        .load(item.getImageItemUrl())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(img_catalogue);
            }
            namaBarang_catalogue.setText(item.getNama_barang());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        listener.onClickCatalogueListener(snapshot);
                    }
                }
            });
        }
    }
}
