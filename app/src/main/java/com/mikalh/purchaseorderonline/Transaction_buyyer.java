package com.mikalh.purchaseorderonline;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.TransactionAdapter;


public class Transaction_buyyer extends Fragment implements TransactionAdapter.OnTransactionSelectedListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    Query query;
    TransactionAdapter adapter;
    RecyclerView transactionList_RV;
    public static String KEY_UID = "id";
    private OnFragmentInteractionListener mListener;
    //Deklarasi Variable

    public Transaction_buyyer() {
        // Required empty public constructor
    }


    public static Transaction_buyyer newInstance(String param1, String param2) {
        Transaction_buyyer fragment = new Transaction_buyyer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("Cart")
                .whereEqualTo("MakePO",true).whereEqualTo("IDPembeli",user.getUid()).orderBy("tanggalPembuatanPO", Query.Direction.ASCENDING);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transaction_buyyer, container, false);
        transactionList_RV = v.findViewById(R.id.transactionList_RV);
        adapter = new TransactionAdapter(query,this){
            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public void onBindViewHolder(TransactionHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }
        };
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        transactionList_RV.setLayoutManager(llm);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(transactionList_RV.getContext(), llm.getOrientation());
        transactionList_RV.addItemDecoration(itemDecoration);
        transactionList_RV.setAdapter(adapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onTransactionSelectedListener(DocumentSnapshot transaction) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }else {
            String ID = transaction.getId();
            Intent i =  new Intent(getActivity(),POBuyer.class);
            i.putExtra(KEY_UID,ID);
            startActivity(i);
        }

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
