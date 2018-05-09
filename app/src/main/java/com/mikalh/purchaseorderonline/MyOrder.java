package com.mikalh.purchaseorderonline;

import android.app.Dialog;
import android.app.DownloadManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.TransactionAdapter;
import com.mikalh.purchaseorderonline.Model.Transaction;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MyOrder extends AppCompatActivity implements TransactionAdapter.OnTransactionSelectedListener{
    FirebaseAuth auth;
    FirebaseUser user;
    Query query;
    FirebaseFirestore firestore;
    TransactionAdapter adapter;
    RecyclerView transasction_myOrder;
    CustomDialog cd;
    Transaction detailTransaction;
    String Key = "key=AAAAx1NMbj0:APA91bHv2Yky3eenD79mwmY1unL3bLEI57VLpDkFoxQ2rfowQXju2DkeRV4_SvOF-LCaO9IsZfAhFIliTTeo5RPs5EwBxlImuoeDlfBzKsTDEiHsGBqtJlp8fCNgHEjlOAx9UqU_mWaT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        query = FirebaseFirestore.getInstance().collection("Transaction").whereEqualTo("penerima_id",user.getUid());
        firestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_my_order);
        cd = new CustomDialog(this);
        transasction_myOrder = findViewById(R.id.transaction_myOrder);
        adapter = new TransactionAdapter(query,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            public void onBindViewHolder(TransactionHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                FirebaseCrash.logcat(Log.ERROR, "Error Adapter", "NPE caught");
                FirebaseCrash.report(e);
            }
        };
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        transasction_myOrder.setLayoutManager(llm);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(transasction_myOrder.getContext(), llm.getOrientation());
        transasction_myOrder.addItemDecoration(itemDecoration);
        transasction_myOrder.setAdapter(adapter);
    }

    @Override
    public void onTransactionSelectedListener(final DocumentSnapshot transaction) {
        cd.show();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_my_order_detail);

        final TextInputEditText namaBarang_detailOrder = dialog.findViewById(R.id.namaBarang_detailOrder);
        final TextInputEditText banyakBarang_detailOrder = dialog.findViewById(R.id.banyakBarang_detailOrder);
        final TextInputEditText totalHarga_detailOrder = dialog.findViewById(R.id.totalHarga_detailOrder);
        final TextInputEditText status_detailOrder = dialog.findViewById(R.id.status_detailOrder);
        final Button update_statusDo = dialog.findViewById(R.id.update_statusDo);
        final ImageView exit_image = dialog.findViewById(R.id.exit_image);

        firestore.collection("Transaction").document(transaction.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    detailTransaction = doc.toObject(Transaction.class);
                    namaBarang_detailOrder.setText(detailTransaction.getNama_barang());
                    banyakBarang_detailOrder.setText(detailTransaction.getQuantitas_banyakBarang());
                    totalHarga_detailOrder.setText(detailTransaction.getTotalHarga()+"");
                    status_detailOrder.setText(detailTransaction.getStatus());

                    cd.dismiss();
                    dialog.show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.logcat(Log.ERROR, "Error Get Data myOrder", "NPE caught");
                FirebaseCrash.report(e);
            }
        });
        update_statusDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> updates = new HashMap<>();
                updates.put("status",status_detailOrder.getText().toString());
                firestore.collection("Transaction").document(transaction.getId())
                        .update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            new Sendnotif().execute();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.logcat(Log.ERROR, "Error update My Order", "NPE caught");
                        FirebaseCrash.report(e);
                    }
                });
            }
        });

        exit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public class Sendnotif extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("https://fcm.googleapis.com/fcm/send"); //http://api.simasjiwa.com/rest/json/epolicylogin http://192.168.22.54/RestApp/rest/json/epolicylogin
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
                /*JSONArray notificationArr = new JSONArray();
                notification.put("notification", notification);*/

                JSONObject postDataParam = new JSONObject();
                postDataParam.put("to", detailTransaction.getNotificationId());
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

        }
    }
}
