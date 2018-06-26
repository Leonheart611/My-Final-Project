package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Model.Company;
import com.mikalh.purchaseorderonline.Model.User;
import com.mikalh.purchaseorderonline.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mika.frentzen on 16/03/2018.
 */

public class CompanyAdapter extends FirestoreAdapter<CompanyAdapter.CompanyHolder>{

    public CompanyAdapter(Query query, OnCompanySelectedListener listener) {
        super(query);
        mListener = listener;
    }

    public interface OnCompanySelectedListener {

        void onCompanySelected(DocumentSnapshot user);

    }
    private OnCompanySelectedListener mListener;

    @Override
    public CompanyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_company,parent,false);
        return new CompanyHolder(view);
    }

    @Override
    public void onBindViewHolder(CompanyHolder holder, int position) {
        holder.bind(getSnapshot(position),mListener);
    }

    public static class CompanyHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        CircleImageView imageCompany;
        public CompanyHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyName);
            imageCompany = itemView.findViewById(R.id.imageCompany);
        }
        public void bind(final DocumentSnapshot snapshot,
                         final OnCompanySelectedListener listener){
            Company company = snapshot.toObject(Company.class);
                Glide.with(imageCompany.getContext())
                        .load(company.getUrl_pictLogo())
                        .into(imageCompany);
            companyName.setText(company.getNama_perusahaan());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!= null){
                        listener.onCompanySelected(snapshot);
                    }
                }
            });
        }




    }
}
