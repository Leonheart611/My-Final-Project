package com.mikalh.purchaseorderonline;

import android.app.Dialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.mikalh.purchaseorderonline.Model.Cart;
import com.mikalh.purchaseorderonline.Model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class DetailPesanan_Status extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DetailPesanan_Status() {
        // Required empty public constructor
    }


    public static DetailPesanan_Status newInstance(String param1, String param2) {
        DetailPesanan_Status fragment = new DetailPesanan_Status();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    TextInputEditText tanggalPermintaanKirim_detail,AlamatPengiriman_detail;
    TextView statusKirimPO, perusahaan_detailPesan;
    Button pesananSudahDikirim,lihatBuktiBayar,transaksiSelesai,convertPdf_detail;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    String ID,tanggalPengiriman,nomorPO;
    String buktiPembayaranLink;
    String Key = "key=AAAAx1NMbj0:APA91bHv2Yky3eenD79mwmY1unL3bLEI57VLpDkFoxQ2rfowQXju2DkeRV4_SvOF-LCaO9IsZfAhFIliTTeo5RPs5EwBxlImuoeDlfBzKsTDEiHsGBqtJlp8fCNgHEjlOAx9UqU_mWaT";
    String NotificationTarget;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_pesanan__status, container, false);
        tanggalPermintaanKirim_detail = v.findViewById(R.id.tanggalPermintaanKirim_detail);
        AlamatPengiriman_detail = v.findViewById(R.id.AlamatPengiriman_detail);
        pesananSudahDikirim = v.findViewById(R.id.pesananSudahDikirim);
        pesananSudahDikirim.setOnClickListener(this);
        lihatBuktiBayar = v.findViewById(R.id.lihatBuktiBayar);
        lihatBuktiBayar.setOnClickListener(this);
        transaksiSelesai = v.findViewById(R.id.transaksiSelesai);
        transaksiSelesai.setOnClickListener(this);
        convertPdf_detail = v.findViewById(R.id.convertPdf_detail);
        convertPdf_detail.setOnClickListener(this);
        statusKirimPO = v.findViewById(R.id.statusKirimPO);
        perusahaan_detailPesan = v.findViewById(R.id.perusahaan_detailPesanan);
        firestore.collection("Cart").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                tanggalPengiriman = snapshot.get("tanggalPermintaanKirim").toString();
                String Alamat = snapshot.get("alamatPengiriman").toString();
                String Status = snapshot.get("StatusPO").toString();
                String namaPerusahaanPemesan = snapshot.get("namaPerusahaanPembeli").toString();
                nomorPO = snapshot.get("NomorPO").toString();
                buktiPembayaranLink = snapshot.get("LinkBuktiBayar").toString();
                NotificationTarget = snapshot.get("PembeliNotif").toString();
                tanggalPermintaanKirim_detail.setText(tanggalPengiriman, TextView.BufferType.EDITABLE);
                AlamatPengiriman_detail.setText(Alamat, TextView.BufferType.EDITABLE);
                statusKirimPO.setText(Status);
                if (Status.equals("Sedang Di konfirmasi Kepenjual")){
                    transaksiSelesai.setEnabled(false);
                }if (Status.equals("Sudah Diterima")&& !buktiPembayaranLink.isEmpty()){
                    pesananSudahDikirim.setEnabled(false);
                    transaksiSelesai.setEnabled(false);
                }if (buktiPembayaranLink.isEmpty()){
                    lihatBuktiBayar.setEnabled(false);
                }
                perusahaan_detailPesan.setText(namaPerusahaanPemesan);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Crashlytics.logException(e);
            }
        });

        return v;
    }
    public void BuktiBayar(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_bukti_bayar);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        final ImageView gambarBuktiBayar = dialog.findViewById(R.id.gambarBuktiBayar);
        Glide.with(gambarBuktiBayar.getContext())
                .load(buktiPembayaranLink)
                .into(gambarBuktiBayar);
        final Button ok_buktiBayar = dialog.findViewById(R.id.ok_buktiBayar);
        ok_buktiBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public Task<User> getUser(String ID) {
        DocumentReference ref = firestore.collection("Users").document(ID);

        return ref.get().continueWith(new Continuation<DocumentSnapshot, User>() {
            @Override
            public User then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                User user = task.getResult().toObject(User.class);
                return user;
            }
        });
    }
    final String[] idPenjual = new String[1];
    final String[] idPembeli = new String[1];
    final User[] userPenjual = new User[1];
    final User[] userPenmebeli = new User[1];
    List<Cart> carts = new ArrayList<>();
    @Override
    public void onClick(View v) {
        if (v == convertPdf_detail){
            firestore.collection("Cart").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        idPenjual[0] = snapshot.get("IDPenjual").toString();
                        idPembeli[0] = snapshot.get("IDPembeli").toString();
                    }if (task.isComplete()){
                        getUser(idPenjual[0]).addOnCompleteListener(new OnCompleteListener<User>() {
                            @Override
                            public void onComplete(@NonNull Task<User> task) {
                                if (task.isSuccessful())
                                userPenjual[0] = task.getResult();
                                Log.e("Data Penjual",userPenjual[0].getNama_PIC());
                                if (task.isComplete()){
                                    getUser(idPembeli[0]).addOnCompleteListener(new OnCompleteListener<User>() {
                                        @Override
                                        public void onComplete(@NonNull Task<User> task) {
                                            if (task.isSuccessful()) {
                                                userPenmebeli[0] = task.getResult();
                                                Log.e("Data Pembeli",userPenmebeli[0].getNama_PIC());
                                                firestore.collection("Cart").document(ID).collection("ItemList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()){
                                                            QuerySnapshot querySnapshot = task.getResult();
                                                            carts = querySnapshot.toObjects(Cart.class);
                                                            Log.e("Data salah 1 data",carts.get(0).getNama_barang());
                                                            new getPDFData().execute();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Crashlytics.logException(e);
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Crashlytics.logException(e);
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Crashlytics.logException(e);
                }
            });
        }
        if(v == pesananSudahDikirim){
            firestore.collection("Cart").document(ID).update("StatusPO","Pesanan Sedang Dikirim").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        firestore.collection("Cart").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot snapshot = task.getResult();
                                String Status = snapshot.get("StatusPO").toString();
                                statusKirimPO.setText(Status);
                                Toast.makeText(getActivity(),"Berhasil ubah status Pesanan",Toast.LENGTH_LONG).show();
                                new pengirimanBarangNotif().execute();
                            }
                        });

                    }
                }
            });
        }
        if (v== lihatBuktiBayar){
            BuktiBayar();
        }
        if (v == transaksiSelesai){
            firestore.collection("Cart").document(ID).update("StatusPO","Selesai").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())  {
                        firestore.collection("Cart").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot snapshot = task.getResult();
                                String Status = snapshot.get("StatusPO").toString();
                                statusKirimPO.setText(Status);
                                Toast.makeText(getActivity(),"Berhasil ubah status Pesanan",Toast.LENGTH_LONG).show();
                                new transaksiSelesai().execute();
                            }
                        });
                    }
                }
            });
        }

    }
    public class transaksiSelesai extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Authorization", Key);
                conn.setRequestProperty("Content-Type", "application/json");


                JSONObject notification = new JSONObject();
                notification.put("body", "Transaksi Sudah Selesai Terima kasih");

                JSONObject postDataParam = new JSONObject();
                postDataParam.put("to", NotificationTarget);
                postDataParam.put("notification", notification);
                Log.e("param", postDataParam.toString());

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
                writer.write(postDataParam.toString());
                writer.flush();
                writer.close();
                os.close();


                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer();
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                } else {
                    return new String("False: " + responseCode);

                }

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
    }
    public class pengirimanBarangNotif extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Authorization", Key);
                conn.setRequestProperty("Content-Type", "application/json");


                JSONObject notification = new JSONObject();
                notification.put("body", "Barang yang dipesan telah dikirm");

                JSONObject postDataParam = new JSONObject();
                postDataParam.put("to", NotificationTarget);
                postDataParam.put("notification", notification);
                Log.e("param", postDataParam.toString());

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
                writer.write(postDataParam.toString());
                writer.flush();
                writer.close();
                os.close();


                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer();
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                } else {
                    return new String("False: " + responseCode);

                }

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
    }


    class getPDFData extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*  customDialog.show();*/
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                Gson gson = new Gson();
                URL url = new URL("https://pdfgeneratorapi.com/api/v3/templates/33028/output");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("X-Auth-Key", "7901fc53f35f62b3e8690d39bf7ade12359c67b2a91f210498fb440757b2d232");
                conn.setRequestProperty("X-Auth-Workspace","mika.leonheart6@gmail.com");
                conn.setRequestProperty("X-Auth-Signature","adf8d5abfc33aa151ba7193affdd8000f7299b465efb5c17ef4c5d21cbb6084d");
                conn.setRequestProperty("Content-Type","application/json");
                conn.setRequestProperty("Accept","application/json");

                JSONObject postDataParam = new JSONObject();
                postDataParam.put("namaPTPengirim",userPenmebeli[0].getNama_perusahaan());
                postDataParam.put("alamatPTPengirim",userPenmebeli[0].getAlamat_perusahaan());
                postDataParam.put("kotaPengirim",userPenmebeli[0].getKota());
                postDataParam.put("namaPICPengirim",userPenmebeli[0].getNama_PIC());
                postDataParam.put("jabatanPICPengirim",userPenmebeli[0].getJabatan_PIC());
                postDataParam.put("namaPICPenerima",userPenjual[0].getNama_PIC());
                postDataParam.put("namaPTPenerima",userPenjual[0].getNama_perusahaan());
                postDataParam.put("Alamat_PT_Penerima",userPenjual[0].getAlamat_perusahaan());
                postDataParam.put("kotaPenerima",userPenjual[0].getKota());
                JSONArray jsonArray = new JSONArray();
                for (int i = 0 ;i<carts.size();i++){
                    Cart cart = carts.get(i);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("nama_barang",cart.getNama_barang());
                    jsonObject.put("quantitas_banyakBarang",cart.getQuantitas_banyakBarang());
                    jsonObject.put("totalHargaBarang",cart.getTotalHargaBarang());
                    jsonObject.put("harga_barang",cart.getHarga_barang());
                    jsonObject.put("unit",cart.getUnit());
                    jsonArray.put(jsonObject);
                }
                postDataParam.put("itemList",jsonArray);
                postDataParam.put("TanggalPengiriman",tanggalPengiriman);

                Log.e("param", postDataParam.toString());

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
                writer.write(postDataParam.toString());
                writer.flush();
                writer.close();
                os.close();


                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK){
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer();
                    String line = "";
                    while ((line = in.readLine())!= null){
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }else {
                    return new String("False: "+responseCode);
                }
            }catch (Exception e){
                return new String("Exeception"+e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);
                String dataPDF = jsonObject.getString("response");
                ConvertToPDF(dataPDF);
                Log.e("Result",s);
                Toast.makeText(getActivity(),"PDF berhasil di buat mohon cek di bagian file Download",Toast.LENGTH_LONG).show();
            }catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
                /*    customDialog.dismiss();*/
            }
        }
    }
    public void ConvertToPDF(String data) throws Exception{
        String filesDirPath = Environment.getExternalStorageDirectory().toString() +"/Download/";
        nomorPO = nomorPO.replace("/","");
        final File dwldsPath = new File(filesDirPath+nomorPO+".pdf");
        byte[] pdfAsBytes = Base64.decode(data, 0);
        FileOutputStream os;
        os = new FileOutputStream(dwldsPath, false);
        os.write(pdfAsBytes);
        os.flush();
        os.close();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
