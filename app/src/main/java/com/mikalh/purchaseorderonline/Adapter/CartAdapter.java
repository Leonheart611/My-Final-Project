package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;


/**
 * Created by mika.frentzen on 05/03/2018.
 */

public class CartAdapter extends FirestoreAdapter<CartAdapter.CartHolder>{
    public interface OnCartSelectedListener{
        void onCartSelected(DocumentSnapshot cart);
    }

    private OnCartSelectedListener mListener;

    public CartAdapter(Query query, OnCartSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list,parent,false);;

        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(CartHolder holder, int position) {
        holder.bind(getSnapshot(position),mListener);
    }


    public static class CartHolder extends RecyclerView.ViewHolder{
        ImageView imageItem_cart;
        TextView nameItem_cart,hargaItem_cart,quantitasItem_cart,totalHarga;

        public CartHolder(View itemView) {
            super(itemView);
            nameItem_cart = itemView.findViewById(R.id.namaItem_cart);
            hargaItem_cart = itemView.findViewById(R.id.hargaItem_cart);
            quantitasItem_cart = itemView.findViewById(R.id.quantitasItem_cart);
            imageItem_cart = itemView.findViewById(R.id.imageItem_cart);
            totalHarga = itemView.findViewById(R.id.totalHarga);
        }
        public void bind(final DocumentSnapshot snapshot, final OnCartSelectedListener listener){
            Cart cart = snapshot.toObject(Cart.class);
            BigDecimal harga = new BigDecimal(cart.getHarga_barang().replace(".",""));
            int quantitas = cart.getQuantitas_banyakBarang();
            BigDecimal total = totalCost(quantitas,harga);
            String TotalCur = formatRP(total);
            /*if (!cart.getImageItemUrl().isEmpty()) {
                Glide.with(imageItem_cart.getContext())
                        .load(cart.getImageItemUrl())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageItem_cart);
            }*/
            nameItem_cart.setText(cart.getNama_barang());
            hargaItem_cart.setText(cart.getHarga_barang());
            quantitasItem_cart.setText(cart.getQuantitas_banyakBarang()+"");
            totalHarga.setText(TotalCur);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onCartSelected(snapshot);
                    }
                }
            });
        }

        public BigDecimal totalCost(int itemQuantity, BigDecimal itemPrice){
            BigDecimal itemCost,totalCost = null;
            itemCost = itemPrice.multiply(new BigDecimal(itemQuantity));
            totalCost = itemCost;
            return totalCost;
        }
        public String formatRP (BigDecimal n){
            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);

            return kursIndonesia.format(n);
        }
    }


}
