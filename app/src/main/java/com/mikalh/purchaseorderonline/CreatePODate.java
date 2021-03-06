package com.mikalh.purchaseorderonline;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.mikalh.purchaseorderonline.Adapter.CreatePOAdapter;
import com.mikalh.purchaseorderonline.Model.Cart;
import com.mikalh.purchaseorderonline.Model.Counter;
import com.mikalh.purchaseorderonline.Model.Shard;
import com.mikalh.purchaseorderonline.Model.User;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class CreatePODate extends Fragment implements CreatePOAdapter.OnCreatePOSelectedListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CreatePODate() {
        // Required empty public constructor
    }


    public static CreatePODate newInstance(String param1, String param2) {
        CreatePODate fragment = new CreatePODate();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    SharedPreferences mPrefs;
    private OnFragmentInteractionListener mListener;
    // Variable
    String ID,tanggal,tanggalbulan,tanggaltahun,tanggalDepan;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    TextView noPO,tanggalPO,penerimaPO,alamatPO,propinsiPO,kotaPO,noTelpnPO,faxPO,totalHargaPO;
    Button nextPO;
    RecyclerView createPO_RV;
    CreatePOAdapter adapter;
    Query query;
    ViewPager createPOPagger;
    String noPOS;
    Task<Integer> counter = null;
    User myUser;
    Random rand = new Random();
    int TotalHarga;
    int  n = rand.nextInt(100) + 1;
    CollectionReference cartCollection;
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
        cartCollection = firestore.collection("Cart");
        ID = getActivity().getIntent().getExtras().getString(CartBuyer.KEY_UID);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
        tanggal = df.format(c);
        SimpleDateFormat bulan = new SimpleDateFormat("MMMM");
        final SimpleDateFormat tahun = new SimpleDateFormat("yyyy");
        SimpleDateFormat tanggalDepanf = new SimpleDateFormat("dd");
        tanggalbulan = bulan.format(c);
        tanggaltahun = tahun.format(c);
        tanggalDepan = tanggalDepanf.format(c);
        query = cartCollection.document(ID).collection("ItemList").orderBy("nama_barang");
        createPOPagger = getActivity().findViewById(R.id.createPoPagger);
        mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);

    }
    public Task<Integer> getCount(final DocumentReference ref) {
        // Sum the count of each shard in the subcollection
        return ref.collection("shards").get()
                .continueWith(new Continuation<QuerySnapshot, Integer>() {
                    @Override
                    public Integer then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        int count = 0;
                        for (DocumentSnapshot snap : task.getResult()) {
                            Shard shard = snap.toObject(Shard.class);
                            count += shard.count;
                        }
                        return count;
                    }
                });
    }
    public Task<User> getUser(String ID){
        DocumentReference ref = firestore.collection("Users").document(ID);

        return ref.get().continueWith(new Continuation<DocumentSnapshot, User>() {
            @Override
            public User then(@NonNull Task<DocumentSnapshot> task) throws Exception{
                User user = task.getResult().toObject(User.class);
                return user;
            }
        });
    }
    public Task<Void> createCounter(final DocumentReference ref, final int numShards) {
        // Initialize the counter document, then initialize each shard.
        return ref.set(new Counter(numShards))
                .continueWithTask(new Continuation<Void, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        List<Task<Void>> tasks = new ArrayList<>();

                        // Initialize each shard with count=0
                        for (int i = 0; i < numShards; i++) {
                            Task<Void> makeShard = ref.collection("shards")
                                    .document(String.valueOf(i))
                                    .set(new Shard(0));

                            tasks.add(makeShard);
                        }

                        return Tasks.whenAll(tasks);
                    }
                });
    }
    public Task<Void> incrementCounter(final DocumentReference ref, final int numShards) {
        int shardId = (int) Math.floor(Math.random() * numShards);
        final DocumentReference shardRef = ref.collection("shards").document(String.valueOf(shardId));

        return firestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                Shard shard = transaction.get(shardRef).toObject(Shard.class);
                shard.count += 1;

                transaction.set(shardRef, shard);
                return null;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_podate, container, false);
        noPO = v.findViewById(R.id.noPO);
        tanggalPO = v.findViewById(R.id.tanggalPO);
        penerimaPO = v.findViewById(R.id.penerimaPO);
        alamatPO = v.findViewById(R.id.alamatPO);
        propinsiPO = v.findViewById(R.id.propinsiPO);
        kotaPO = v.findViewById(R.id.kotaPO);
        noTelpnPO = v.findViewById(R.id.noTelpnPO);
        faxPO = v.findViewById(R.id.faxPO);
        totalHargaPO = v.findViewById(R.id.totalHargaPO);
        createPO_RV = v.findViewById(R.id.createPO_RV_POBuyer);
        nextPO = v.findViewById(R.id.nextPO_POBuyer);
        adapter = new CreatePOAdapter(query,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }
            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Crashlytics.logException(e);
            }
            @Override
            public void onBindViewHolder(CreatePOAdapterHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            public CreatePOAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }
        };
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        createPO_RV.setLayoutManager(llm);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(createPO_RV.getContext(), llm.getOrientation());
        createPO_RV.addItemDecoration(itemDecoration);
        createPO_RV.setAdapter(adapter);
        firestore.collection("Cart").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                String  namaPIC = snapshot.get("NamaPIC").toString();
                String Alamat = snapshot.get("Alamat").toString();
                String Kota = snapshot.get("Kota").toString();
                String Provinsi = snapshot.get("Propinsi").toString();
                String Telp = snapshot.get("Telp").toString();
                String Fax = snapshot.get("Fax").toString();
                TotalHarga = Integer.parseInt(snapshot.get("GrandTotal").toString());
                String Grandtotal = formatRP(TotalHarga);
                noPOS = tanggalDepan + "/" + user.getDisplayName().substring(1, 3) + "/" + tanggalbulan + "/PO" +n+"/" + tanggaltahun;
                noPO.setText(": "+noPOS);
                tanggalPO.setText(": "+tanggal);
                penerimaPO.setText(": "+namaPIC);
                alamatPO.setText(": "+Alamat);
                kotaPO.setText(": "+Kota);
                noTelpnPO.setText(": "+Telp);
                propinsiPO.setText(": "+Provinsi);
                faxPO.setText(": "+Fax);
                totalHargaPO.setText(Grandtotal);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Crashlytics.logException(e);
            }
        });
        nextPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor prefEdit = mPrefs.edit();
                prefEdit.putString("NoPO",noPOS);
                prefEdit.commit();
                createPOPagger.setCurrentItem(1,true);
            }
        });



        return v;
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
    public void onDestroy() {
        super.onDestroy();
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
    public String formatRP (Integer n){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        return kursIndonesia.format(n);
    }

    @Override
    public void onCreatePOSelectedListener(DocumentSnapshot cart) {
        String id = cart.getId();
        popUpDetailItem(id);
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
        final ImageView detailDeleteItem;
        detailDeleteItem = dialog.findViewById(R.id.detailDeleteItem);
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
                            final int HasilTotal = TotalHarga - hargaKurang[0];
                            if (HasilTotal == 0){
                                firestore.collection("Cart").document(ID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            dialog.dismiss();
                                            getActivity().finish();
                                            Toast.makeText(getActivity(),"Cart Kosong",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else {
                                firestore.collection("Cart").document(ID).collection("ItemList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            QuerySnapshot snapshots = task.getResult();
                                            int BanyakData = snapshots.size();
                                            Map<String,Object> updateData = new HashMap<>();
                                            updateData.put("GrandTotal",HasilTotal);
                                            updateData.put("BanyakData",BanyakData);
                                            firestore.collection("Cart").document(ID).update(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        firestore.collection("Cart").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                DocumentSnapshot snapshot = task.getResult();
                                                                String  namaPIC = snapshot.get("NamaPIC").toString();
                                                                String Alamat = snapshot.get("Alamat").toString();
                                                                String Kota = snapshot.get("Kota").toString();
                                                                String Provinsi = snapshot.get("Propinsi").toString();
                                                                String Telp = snapshot.get("Telp").toString();
                                                                String Fax = snapshot.get("Fax").toString();
                                                                TotalHarga = Integer.parseInt(snapshot.get("GrandTotal").toString());
                                                                String Grandtotal = formatRP(TotalHarga);
                                                                noPOS = tanggalDepan + "/" + user.getDisplayName().substring(1, 3) + "/" + tanggalbulan + "/PO" +n+"/" + tanggaltahun;
                                                                noPO.setText(": "+noPOS);
                                                                tanggalPO.setText(": "+tanggal);
                                                                penerimaPO.setText(": "+namaPIC);
                                                                alamatPO.setText(": "+Alamat);
                                                                kotaPO.setText(": "+Kota);
                                                                noTelpnPO.setText(": "+Telp);
                                                                propinsiPO.setText(": "+Provinsi);
                                                                faxPO.setText(": "+Fax);
                                                                totalHargaPO.setText(Grandtotal);
                                                                dialog.dismiss();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Crashlytics.logException(e);
                                                            }
                                                        });
                                                    }

                                                }
                                            });
                                        }
                                    }
                                });


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
                    Glide.with(detailDeleteItem.getContext())
                            .load(cart.getImageItemUrl())
                            .into(detailDeleteItem);
                    dialog.show();
                }
            }
        });

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
