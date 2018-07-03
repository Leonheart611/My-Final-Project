package com.mikalh.purchaseorderonline;

import android.app.Dialog;
import android.content.Intent;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.CreatePOAdapter;
import com.mikalh.purchaseorderonline.Model.Cart;

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
    int TotalHarga;
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
        createPO_RV_detail = v.findViewById(R.id.createPO_RV_POBuyer);
        nextPO_detail = v.findViewById(R.id.nextPO_POBuyer);
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
                    String noPO = snapshot.get("NomorPO").toString();
                    String tanggal = snapshot.get("tanggalPembuatanPO").toString();
                    TotalHarga = Integer.parseInt(snapshot.get("GrandTotal").toString());
                    String Grandtotal = formatRP(TotalHarga);

                    noPO_detail.setText(noPO);
                    faxPO_detail.setText(Fax);
                    noTelpnPO_detail.setText(Telp);
                    propinsiPO_detail.setText(Provinsi);
                    kotaPO_detail.setText(Kota);
                    alamatPO_detail.setText(Alamat);
                    penerimaPO_detail.setText(namaPIC);
                    totalHargaPO_detail.setText(Grandtotal);
                    tanggalPO_detail.setText(tanggal);
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
       /* String id = cart.getId();
        popUpDetailItem(id);*/
    }
    public void popUpDetailItem(final String id){
        final Dialog dialog = new Dialog(getActivity());
        final int[] hargaKurang = new int[1];
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.create_po_item_delete);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        final TextView namaBarang_createPO, banyakBarang_createPO,totalBarang_createPO,satuanBarang_createPO;
        final Button deleteDo_createPO;
        namaBarang_createPO = dialog.findViewById(R.id.namaBarang_createPO);
        banyakBarang_createPO = dialog.findViewById(R.id.banyakBarang_createPO);
        totalBarang_createPO = dialog.findViewById(R.id.totalBarang_createPO);
        satuanBarang_createPO = dialog.findViewById(R.id.satuanBarang_createPO);
        deleteDo_createPO = dialog.findViewById(R.id.deleteDo_createPO);
        deleteDo_createPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("Cart").document(ID).collection("ItemList").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            int HasilTotal = TotalHarga - hargaKurang[0];
                            if (HasilTotal==0){
                                firestore.collection("Cart").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            dialog.dismiss();
                                            Intent i = new Intent(getActivity(),CartBuyer.class);
                                            startActivity(i);
                                            Toast.makeText(getActivity(),"Cart Kosong",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else {
                                firestore.collection("Cart").document(id).update("GrandTotal",HasilTotal);
                                dialog.dismiss();
                            }
                        }
                    }
                });
            }
        });
        firestore.collection("Cart").document(ID).collection("ItemList").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    Cart cart = snapshot.toObject(Cart.class);
                    namaBarang_createPO.setText(cart.getNama_barang());
                    banyakBarang_createPO.setText(cart.getQuantitas_banyakBarang()+"");
                    totalBarang_createPO.setText(cart.getTotalHargaBarang()+"");
                    satuanBarang_createPO.setText(cart.getUnit());
                    hargaKurang[0] = cart.getTotalHargaBarang();
                    dialog.show();
                }
            }
        });

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
