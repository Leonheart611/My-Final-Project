package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.R;

public class BlockRequestAdapter extends FirestoreAdapter<BlockRequestAdapter.BlockRequestAdapterHolder> {
    public interface OnBlockRequestListener{
        void onBlockRequestListener(DocumentSnapshot data);
    }
    private OnBlockRequestListener mlistener;
    public BlockRequestAdapter(Query query,OnBlockRequestListener listener) {
        super(query);
        mlistener = listener;
    }

    @Override
    public BlockRequestAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_request_list,parent,false);
        return new BlockRequestAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(BlockRequestAdapterHolder holder, int position) {
        holder.bind(getSnapshot(position),mlistener);
    }




    public static class BlockRequestAdapterHolder extends RecyclerView.ViewHolder{
        TextView namaPerusahaan_blockList, alasanBlock_blockList;
        public BlockRequestAdapterHolder(View itemView) {
            super(itemView);
            namaPerusahaan_blockList = itemView.findViewById(R.id.namaPerusahaan_blockList);
            alasanBlock_blockList = itemView.findViewById(R.id.alasanBlock_blockList);
        }
        public void bind(final DocumentSnapshot snapshot,final OnBlockRequestListener listener){
            String NamaPerusahaan = snapshot.get("NamaPerusahaan").toString();
            String Alasan = snapshot.get("Alasan").toString();
            namaPerusahaan_blockList.setText(NamaPerusahaan);
            alasanBlock_blockList.setText(Alasan);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        listener.onBlockRequestListener(snapshot);
                    }
                }
            });
        }
    }
}
