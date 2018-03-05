package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Model.Cart;
import com.mikalh.purchaseorderonline.R;


/**
 * Created by mika.frentzen on 05/03/2018.
 */

public class CartAdapter extends FirestoreAdapter<CartAdapter.CartHolder>{
    public interface OnCartSelectedListener{
        void onItemSelected(DocumentSnapshot cart);
    }

    private OnCartSelectedListener mListener;

    public CartAdapter(Query query, OnCartSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        return null;
    }

    @Override
    public void onBindViewHolder(CartHolder holder, int position) {

    }


    public static class CartHolder extends RecyclerView.ViewHolder{
        ImageView imageItem_cart;
        TextView nameItem_cart,hargaItem_cart,quantitasItem_cart;

        public CartHolder(View itemView) {
            super(itemView);
            nameItem_cart = itemView.findViewById(R.id.namaItem_cart);
            hargaItem_cart = itemView.findViewById(R.id.hargaItem_cart);
            quantitasItem_cart = itemView.findViewById(R.id.quantitasItem_cart);
            imageItem_cart = itemView.findViewById(R.id.imageItem_cart);
        }
        public void bind(final DocumentSnapshot snapshot, final OnCartSelectedListener listener){
            Cart cart = snapshot.toObject(Cart.class);
            if (!cart.getImageItemUrl().isEmpty()) {
                Glide.with(imageItem_cart.getContext())
                        .load(cart.getImageItemUrl())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageItem_cart);
            }
            nameItem_cart.setText(cart.getNama_barang());
            hargaItem_cart.setText(cart.getHarga_barang());

        }
    }

}
