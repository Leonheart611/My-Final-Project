package com.mikalh.purchaseorderonline;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

import static android.app.Activity.RESULT_OK;


public class POBuyer_date extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public POBuyer_date() {
        // Required empty public constructor
    }

    public static POBuyer_date newInstance(String param1, String param2) {
        POBuyer_date fragment = new POBuyer_date();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String Key = "key=AAAAx1NMbj0:APA91bHv2Yky3eenD79mwmY1unL3bLEI57VLpDkFoxQ2rfowQXju2DkeRV4_SvOF-LCaO9IsZfAhFIliTTeo5RPs5EwBxlImuoeDlfBzKsTDEiHsGBqtJlp8fCNgHEjlOAx9UqU_mWaT";
    String NotificationTarget;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    String ID, tanggalPengiriman, nomorPO;
    int PICK_IMAGE_REQUEST = 1;
    String[] permissionsRequired = new String[]{android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Uri filePath, imageResult;
    StorageReference storageReference;
    FirebaseStorage storage;
    CustomDialog customDialog;
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
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        customDialog = new CustomDialog(getActivity());

    }
    TextView statusKirimPO;
    TextInputEditText tanggalPermintaanKirim_poBuyyer, AlamatPengiriman_poBuyer;
    Button uploadBuktiBayar, convertPdf_poBuyer, barangSudahDiterima;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pobuyer_date, container, false);
        statusKirimPO = v.findViewById(R.id.statusKirimPO_pobuyer);
        tanggalPermintaanKirim_poBuyyer = v.findViewById(R.id.tanggalPermintaanKirim_poBuyyer);
        AlamatPengiriman_poBuyer = v.findViewById(R.id.AlamatPengiriman_poBuyer);
        uploadBuktiBayar = v.findViewById(R.id.pesananSudahDikirim);
        convertPdf_poBuyer = v.findViewById(R.id.convertPdf_poBuyer);
        barangSudahDiterima = v.findViewById(R.id.barangSudahDiterima);
        firestore.collection("Cart").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    tanggalPengiriman = snapshot.get("tanggalPermintaanKirim").toString();
                    String Alamat = snapshot.get("alamatPengiriman").toString();
                    String Status = snapshot.get("StatusPO").toString();
                    nomorPO = snapshot.get("NomorPO").toString();
                    NotificationTarget = snapshot.getString("PenjualNotif");
                    tanggalPermintaanKirim_poBuyyer.setText(tanggalPengiriman, TextView.BufferType.EDITABLE);
                    AlamatPengiriman_poBuyer.setText(Alamat, TextView.BufferType.EDITABLE);
                    statusKirimPO.setText(Status);
                    if (!Status.equals("Pesanan Sedang Dikirim")) {
                        barangSudahDiterima.setEnabled(false);
                    }if (!Status.equals("Sudah Diterima")){
                        uploadBuktiBayar.setEnabled(false);
                    }
                }
            }
        });
        barangSudahDiterima.setOnClickListener(this);
        uploadBuktiBayar.setOnClickListener(this);
        convertPdf_poBuyer.setOnClickListener(this);
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /* @Override
     public void onAttach(Context context) {
         super.onAttach(context);
         if (context instanceof OnFragmentInteractionListener) {
             mListener = (OnFragmentInteractionListener) context;
         } else {
             throw new RuntimeException(context.toString()
                     + " must implement OnFragmentInteractionListener");
         }
     }
 */
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
        if (v == uploadBuktiBayar) {
            popUpAddFoto();
        }
        if (v == convertPdf_poBuyer) {
            firestore.collection("Cart").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        idPenjual[0] = snapshot.get("IDPenjual").toString();
                        idPembeli[0] = snapshot.get("IDPembeli").toString();
                    }
                    if (task.isComplete()) {
                        getUser(idPenjual[0]).addOnCompleteListener(new OnCompleteListener<User>() {
                            @Override
                            public void onComplete(@NonNull Task<User> task) {
                                if (task.isSuccessful())
                                    userPenjual[0] = task.getResult();
                                Log.e("Data Penjual", userPenjual[0].getNama_PIC());
                                if (task.isComplete()) {
                                    getUser(idPembeli[0]).addOnCompleteListener(new OnCompleteListener<User>() {
                                        @Override
                                        public void onComplete(@NonNull Task<User> task) {
                                            if (task.isSuccessful()) {
                                                userPenmebeli[0] = task.getResult();
                                                Log.e("Data Pembeli", userPenmebeli[0].getNama_PIC());
                                                firestore.collection("Cart").document(ID).collection("ItemList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            QuerySnapshot querySnapshot = task.getResult();
                                                            carts = querySnapshot.toObjects(Cart.class);
                                                            Log.e("Data salah 1 data", carts.get(0).getNama_barang());
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
        if (v == barangSudahDiterima) {
            firestore.collection("Cart").document(ID).update("StatusPO","Sudah Diterima").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        firestore.collection("Cart").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot snapshot = task.getResult();
                                String Status = snapshot.get("StatusPO").toString();
                                statusKirimPO.setText(Status);
                                uploadBuktiBayar.setEnabled(true);
                                new Sendnotif().execute();
                                Toast.makeText(getActivity(),"Berhasil merubah status Pesanan",Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            });
        }
    }

    class getPDFData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*  customDialog.show();*/
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Gson gson = new Gson();
                URL url = new URL("https://pdfgeneratorapi.com/api/v3/templates/33028/output");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("X-Auth-Key", "7901fc53f35f62b3e8690d39bf7ade12359c67b2a91f210498fb440757b2d232");
                conn.setRequestProperty("X-Auth-Workspace", "mika.leonheart6@gmail.com");
                conn.setRequestProperty("X-Auth-Signature", "adf8d5abfc33aa151ba7193affdd8000f7299b465efb5c17ef4c5d21cbb6084d");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                JSONObject postDataParam = new JSONObject();
                postDataParam.put("namaPTPengirim", userPenmebeli[0].getNama_perusahaan());
                postDataParam.put("alamatPTPengirim", userPenmebeli[0].getAlamat_perusahaan());
                postDataParam.put("kotaPengirim", userPenmebeli[0].getKota());
                postDataParam.put("namaPICPengirim", userPenmebeli[0].getNama_PIC());
                postDataParam.put("jabatanPICPengirim", userPenmebeli[0].getJabatan_PIC());
                postDataParam.put("namaPICPenerima", userPenjual[0].getNama_PIC());
                postDataParam.put("namaPTPenerima", userPenjual[0].getNama_perusahaan());
                postDataParam.put("Alamat_PT_Penerima", userPenjual[0].getAlamat_perusahaan());
                postDataParam.put("kotaPenerima", userPenjual[0].getKota());
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < carts.size(); i++) {
                    Cart cart = carts.get(i);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("nama_barang", cart.getNama_barang());
                    jsonObject.put("quantitas_banyakBarang", cart.getQuantitas_banyakBarang());
                    jsonObject.put("totalHargaBarang", cart.getTotalHargaBarang());
                    jsonObject.put("harga_barang", cart.getHarga_barang());
                    jsonObject.put("unit", cart.getUnit());
                    jsonArray.put(jsonObject);
                }
                postDataParam.put("itemList", jsonArray);
                postDataParam.put("TanggalPengiriman", tanggalPengiriman);

                Log.e("param", postDataParam.toString());

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
                writer.write(postDataParam.toString());
                writer.flush();
                writer.close();
                os.close();


                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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
                return new String("Exeception" + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String dataPDF = jsonObject.getString("response");
                ConvertToPDF(dataPDF);
                Log.e("Result", s);
                Toast.makeText(getActivity(), "PDF berhasil di buat mohon cek di bagian file Download", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Crashlytics.logException(e);
                /*    customDialog.dismiss();*/
            }
        }
    }

    public void ConvertToPDF(String data) throws Exception {
        String filesDirPath = Environment.getExternalStorageDirectory().toString() + "/Download/";
        nomorPO = nomorPO.replace("/", "-");
        final File dwldsPath = new File(filesDirPath +nomorPO+".pdf");
        byte[] pdfAsBytes = Base64.decode(data, 0);
        FileOutputStream os;
        os = new FileOutputStream(dwldsPath, false);
        os.write(pdfAsBytes);
        os.flush();
        os.close();
    }

    public void popUpAddFoto() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choose_add_picture);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final Button cameraPicture_add = dialog.findViewById(R.id.cameraPicture_add);
        final Button filePicture_add = dialog.findViewById(R.id.filePicture_add);
        cameraPicture_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(getActivity(), permissionsRequired)) {
                    requestPermissions(permissionsRequired, 1);
                } else {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "DCIM/Camera", "IMG_lwlwlwlwlwwl.jpg");
                    filePath = Uri.fromFile(file);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
                    startActivityForResult(i, 0);
                    dialog.dismiss();
                }

            }
        });
        filePicture_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(getActivity(), permissionsRequired)) {

                    requestPermissions(permissionsRequired, 1);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            customDialog.show();
            filePath = data.getData();
            String hasilPO = nomorPO.replace("/", "");
            final StorageReference ref = storageReference.child("BuktiBayar/" + hasilPO);
            UploadTask uploadTask = ref.putFile(filePath);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        imageResult = task.getResult();
                        firestore.collection("Cart").document(ID).update("LinkBuktiBayar",imageResult.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getActivity(),"Berhasil MengUpload Bukti Bayar",Toast.LENGTH_LONG).show();
                                    customDialog.dismiss();
                                    new buktiUploadNotif().execute();
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                    customDialog.dismiss();
                }
            });
        }
        if (requestCode == 0 && resultCode == RESULT_OK && filePath != null) {
            if (filePath != null) {
                customDialog.show();
                String hasilPO = nomorPO.replace("/", "-");
                final StorageReference ref = storageReference.child("BuktiBayar/" + hasilPO);
                UploadTask uploadTask = ref.putFile(filePath);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            imageResult = task.getResult();
                            firestore.collection("Cart").document(ID).update("LinkBuktiBayar",imageResult.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getActivity(),"Berhasil MengUpload Bukti Bayar",Toast.LENGTH_LONG).show();
                                        customDialog.dismiss();
                                        new buktiUploadNotif().execute();
                                    }
                                }
                            });
                        }
                    }
                });
            }else {
                customDialog.dismiss();

            }
            // letak if di sini coy

        }
    }
    public class buktiUploadNotif extends AsyncTask<String, Void, String> {
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
                notification.put("body", "Bukti Pembayaran untuk PO "+nomorPO+" Sudah di upload");

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

    public class Sendnotif extends AsyncTask<String, Void, String> {
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
                notification.put("body", "You Item was Recieved");

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
    public static boolean hasPermissions (Context context, String...permissions){
            if (context != null && permissions != null) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
            return true;
        }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}


