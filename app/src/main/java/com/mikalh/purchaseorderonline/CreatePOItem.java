package com.mikalh.purchaseorderonline;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class CreatePOItem extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //Setting data view
    TextInputEditText tanggalPermintaanKirim,AlamatPnegiriman;
    Button kirimPO;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    String ID;
    Calendar myCalendar;
    String tanggalHariIni;
    SharedPreferences mPref;
    String NoPO;
    String Key = "key=AAAAx1NMbj0:APA91bHv2Yky3eenD79mwmY1unL3bLEI57VLpDkFoxQ2rfowQXju2DkeRV4_SvOF-LCaO9IsZfAhFIliTTeo5RPs5EwBxlImuoeDlfBzKsTDEiHsGBqtJlp8fCNgHEjlOAx9UqU_mWaT";
    String NotificationTarget;
    public CreatePOItem() {
        // Required empty public constructor
    }

    public static CreatePOItem newInstance(String param1, String param2) {
        CreatePOItem fragment = new CreatePOItem();
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
        ID = getActivity().getIntent().getExtras().getString(SendPO.KEY_UID);
        myCalendar = Calendar.getInstance();
        Date c = myCalendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
        tanggalHariIni = df.format(c);
        mPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        NoPO = mPref.getString("NoPO","");
        firestore.collection("Cart").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    NotificationTarget = snapshot.get("PenjualNotif").toString();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_poitem, container, false);

        tanggalPermintaanKirim = v.findViewById(R.id.tanggalPermintaanKirim);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tanggalPermintaanKirim);
            }
        };
        tanggalPermintaanKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(),date,myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        AlamatPnegiriman = v.findViewById(R.id.AlamatPengiriman);
        kirimPO = v.findViewById(R.id.kirimPO);
        kirimPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String alamat = AlamatPnegiriman.getText().toString();
                final String tanggal = tanggalPermintaanKirim.getText().toString();
                Map<String,Object> dataUpdate = new HashMap<>();
                dataUpdate.put("MakePO",true);
                dataUpdate.put("StatusPO","Sedang Di konfirmasi Kepenjual");
                dataUpdate.put("alamatPengiriman",alamat);
                dataUpdate.put("tanggalPermintaanKirim",tanggal);
                dataUpdate.put("tanggalPembuatanPO",tanggalHariIni);
                dataUpdate.put("NomorPO",NoPO);
                firestore.collection("Cart").document(ID).update(dataUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            new Sendnotif().execute();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Crashlytics.logException(e);
                    }
                });

            }
        });

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private void updateLabel(EditText textInputEditText){
        String myFormat = "dd-MMM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        textInputEditText.setText(sdf.format(myCalendar.getTime()));
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
                notification.put("body", "You Have New Purchase Order");

                JSONObject postDataParam = new JSONObject();
                postDataParam.put("to",NotificationTarget);
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

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                int Success = jsonObject.getInt("success");
                if (Success==1){
                    Intent i = new Intent(getActivity(),buyerActivity.class);
                    startActivity(i);
                    Toast.makeText(getActivity(),"PO Berhasil di kirim, Untuk update cek Bagian Transaksi",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context. toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
