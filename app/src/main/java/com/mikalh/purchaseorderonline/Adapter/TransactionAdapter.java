package com.mikalh.purchaseorderonline.Adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.mikalh.purchaseorderonline.Model.Company;
import com.mikalh.purchaseorderonline.Model.Transaction;
import com.mikalh.purchaseorderonline.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class TransactionAdapter extends FirestoreAdapter<TransactionAdapter.TransactionHolder> {


    public interface OnTransactionSelectedListener {

        void onTransactionSelectedListener(DocumentSnapshot transaction);
    }
    private OnTransactionSelectedListener mListener;
    public TransactionAdapter(Query query, OnTransactionSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transaction,parent,false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {
        holder.bind(getSnapshot(position),mListener);
    }

    public static class TransactionHolder extends RecyclerView.ViewHolder {
        TextView date_transaction, namaBarang_transaction, quantitas_transaction, status_transaction, total_transaction, companyName_transaction;

        public TransactionHolder(View itemView) {
            super(itemView);
            date_transaction = itemView.findViewById(R.id.date_transaction);
            namaBarang_transaction = itemView.findViewById(R.id.namaBarang_transaction);
            quantitas_transaction = itemView.findViewById(R.id.quantitas_transaction);
            status_transaction = itemView.findViewById(R.id.status_transaction);
            total_transaction = itemView.findViewById(R.id.total_transaction);
            companyName_transaction = itemView.findViewById(R.id.companyName_transaction);
        }

        public void bind(final DocumentSnapshot snapshot
                , final OnTransactionSelectedListener listener) {
            Transaction transaction = new Transaction();
            String TotalCur = "";
            try {
                transaction = snapshot.toObject(Transaction.class);
                BigDecimal harga = new BigDecimal(transaction.getHarga_barang().replace(".",""));
                int quantitas = transaction.getQuantitas_banyakBarang();
                BigDecimal total = totalCost(quantitas,harga);
                TotalCur = formatRP(total);
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("error adapter Transac",e.getMessage());
            }



            date_transaction.setText(transaction.getTanggal());
            namaBarang_transaction.setText(transaction.getNama_barang());
            quantitas_transaction.setText(transaction.getQuantitas_banyakBarang()+"");
            status_transaction.setText(transaction.getStatus());
            total_transaction.setText(TotalCur);
            companyName_transaction.setText(transaction.getNamaPerusahaan());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onTransactionSelectedListener(snapshot);
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
