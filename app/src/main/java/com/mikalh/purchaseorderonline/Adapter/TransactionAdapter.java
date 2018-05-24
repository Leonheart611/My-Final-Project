package com.mikalh.purchaseorderonline.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Model.Transaction;
import com.mikalh.purchaseorderonline.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        TextView date_transaction, tanggalPengiriman_transaction, quantitas_transaction,statusPemsanan_transaction, total_transaction, noPo_transaction;
        ImageView statusComplete_transaction;

        public TransactionHolder(View itemView) {
            super(itemView);
            date_transaction = itemView.findViewById(R.id.date_transaction);
            tanggalPengiriman_transaction = itemView.findViewById(R.id.tanggalPengiriman_transaction);
            quantitas_transaction = itemView.findViewById(R.id.quantitas_transaction);
            total_transaction = itemView.findViewById(R.id.total_transaction);
            noPo_transaction = itemView.findViewById(R.id.noPo_transaction);
            statusComplete_transaction = itemView.findViewById(R.id.statusComplete_transaction);
            statusPemsanan_transaction = itemView.findViewById(R.id.statusPemesanan_transaction);
        }

        public void bind(final DocumentSnapshot snapshot
                , final OnTransactionSelectedListener listener) {
            String tanggalPembuatanPO = snapshot.get("tanggalPembuatanPO").toString();
            String tanggalPermintaanKirim = snapshot.get("tanggalPermintaanKirim").toString();
            String NomorPO = snapshot.get("NomorPO").toString();
            int Total = Integer.parseInt(snapshot.get("GrandTotal").toString());
            String Status = snapshot.get("StatusPO").toString();
            int banyakBarang = Integer.parseInt(snapshot.get("BanyakData").toString());
            String GrandTotal = formatRP(Total);
            date_transaction.setText(tanggalPembuatanPO);
            noPo_transaction.setText(NomorPO);
            statusPemsanan_transaction.setText(Status);
            total_transaction.setText(GrandTotal);
            quantitas_transaction.setText(banyakBarang+"");
            tanggalPengiriman_transaction.setText(tanggalPermintaanKirim);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onTransactionSelectedListener(snapshot);
                    }
                }
            });


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
