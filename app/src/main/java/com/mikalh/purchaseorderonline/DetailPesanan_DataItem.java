package com.mikalh.purchaseorderonline;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.CreatePOAdapter;
import com.mikalh.purchaseorderonline.Adapter.TransactionAdapter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class DetailPesanan_DataItem extends Fragment implements CreatePOAdapter.OnCreatePOSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DetailPesanan_DataItem() {
        // Required empty public constructor
    }
    public static DetailPesanan_DataItem newInstance(String param1, String param2) {
        DetailPesanan_DataItem fragment = new DetailPesanan_DataItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    CreatePOAdapter adapter;
    Query query;
    String ID;
    ViewPager paggerDetailPesanan;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ID = getActivity().getIntent().getExtras().getString(Transaction_buyyer.KEY_UID);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("Cart").document(ID).collection("ItemList").orderBy("nama_barang");
        paggerDetailPesanan = getActivity().findViewById(R.id.paggerDetailPesanan);
    }
    TextView noPO_detail,tanggalPO_detail,penerimaPO_detail
            ,alamatPO_detail,propinsiPO_detail,kotaPO_detail,noTelpnPO_detail
            ,faxPO_detail,totalHargaPO_detail;
    RecyclerView createPO_RV_detail;
    Button nextPO_detail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_pesanan__data_item, container, false);
        noPO_detail = v.findViewById(R.id.noPO_detail);
        tanggalPO_detail = v.findViewById(R.id.tanggalPO_detail);
        penerimaPO_detail = v.findViewById(R.id.penerimaPO_detail);
        alamatPO_detail = v.findViewById(R.id.alamatPO_detail);
        propinsiPO_detail = v.findViewById(R.id.propinsiPO_detail);
        kotaPO_detail = v.findViewById(R.id.kotaPO_detail);
        noTelpnPO_detail = v.findViewById(R.id.noTelpnPO_detail);
        faxPO_detail = v.findViewById(R.id.faxPO_detail);
        totalHargaPO_detail = v.findViewById(R.id.totalHargaPO_detail);
        createPO_RV_detail = v.findViewById(R.id.createPO_RV_detail);
        nextPO_detail = v.findViewById(R.id.nextPO_detail);
        nextPO_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paggerDetailPesanan.setCurrentItem(1,true);
            }
        });
        adapter = new CreatePOAdapter(query,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
            }
        };
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        createPO_RV_detail.setLayoutManager(llm);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(createPO_RV_detail.getContext(), llm.getOrientation());
        createPO_RV_detail.addItemDecoration(itemDecoration);
        createPO_RV_detail.setAdapter(adapter);
        firestore.collection("Cart").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    String  namaPIC = snapshot.get("NamaPIC").toString();
                    String Alamat = snapshot.get("Alamat").toString();
                    String Kota = snapshot.get("Kota").toString();
                    String Provinsi = snapshot.get("Propinsi").toString();
                    String Telp = snapshot.get("Telp").toString();
                    String Fax = snapshot.get("Fax").toString();
                    String noPO = snapshot.get("StatusPO").toString();
                    int TotalHarga = Integer.parseInt(snapshot.get("GrandTotal").toString());
                    String Grandtotal = formatRP(TotalHarga);

                    noPO_detail.setText(noPO);
                    faxPO_detail.setText(Fax);
                    noTelpnPO_detail.setText(Telp);
                    propinsiPO_detail.setText(Provinsi);
                    kotaPO_detail.setText(Kota);
                    alamatPO_detail.setText(Alamat);
                    penerimaPO_detail.setText(namaPIC);
                    totalHargaPO_detail.setText(Grandtotal);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Crashlytics.logException(e);
            }
        });


        return v;
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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
    public void onCreatePOSelectedListener(DocumentSnapshot cart) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
