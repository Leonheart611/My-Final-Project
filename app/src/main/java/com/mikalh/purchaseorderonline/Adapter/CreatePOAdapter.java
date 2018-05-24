package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Model.Cart;
import com.mikalh.purchaseorderonline.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CreatePOAdapter  extends FirestoreAdapter<CreatePOAdapter.CreatePOAdapterHolder>{
    public interface OnCreatePOSelectedListener{
        void onCreatePOSelectedListener(DocumentSnapshot cart);
    }
    private OnCreatePOSelectedListener mlistener;


    public CreatePOAdapter(Query query, OnCreatePOSelectedListener listener) {
        super(query);
        mlistener = listener;
    }

    @Override
    public CreatePOAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.createpo_item_list,parent,false);
        return new CreatePOAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(CreatePOAdapterHolder holder, int position) {
        holder.bind(getSnapshot(position),mlistener);
    }



    public static class CreatePOAdapterHolder extends RecyclerView.ViewHolder{
        TextView namaItem_PO,banyakBarang_PO,totalHarga_PO,quantitas_PO,hargaSatuan_PO;
        public CreatePOAdapterHolder(View itemView) {
            super(itemView);
            namaItem_PO = itemView.findViewById(R.id.namaItem_PO);
            banyakBarang_PO = itemView.findViewById(R.id.banyakBarang_PO);
            totalHarga_PO = itemView.findViewById(R.id.totalHarga_PO);
            quantitas_PO = itemView.findViewById(R.id.quantitas_PO);
            hargaSatuan_PO = itemView.findViewById(R.id.hargaSatuan_PO);
        }
        public void bind(final DocumentSnapshot snapshot, final OnCreatePOSelectedListener listener){
            Cart cart = snapshot.toObject(Cart.class);
            String totalHarga = formatRP(cart.getTotalHargaBarang());
            namaItem_PO.setText(cart.getNama_barang());
            banyakBarang_PO.setText(cart.getQuantitas_banyakBarang()+"");
            totalHarga_PO.setText(totalHarga);
            quantitas_PO.setText(cart.getUnit());
            hargaSatuan_PO.setText("@ "+cart.getHarga_barang());
        }
        public String formatRP (Integer n){
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
