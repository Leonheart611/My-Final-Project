package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mikalh.purchaseorderonline.Adapter.CartAdapter;
import com.mikalh.purchaseorderonline.Model.Cart;
import com.mikalh.purchaseorderonline.Model.Company;
import com.mikalh.purchaseorderonline.Model.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class cartUI extends AppCompatActivity implements CartAdapter.OnCartSelectedListener,View.OnClickListener {
    RecyclerView cart_recylerView;
    CartAdapter adapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    Query query;
    Button MakePO_do;
    ArrayList<Cart> carts;
    Cart cart = new Cart();
    Transaction transactionModel;
    private DatabaseReference mDatabase;
    String Key = "key=AAAAx1NMbj0:APA91bHv2Yky3eenD79mwmY1unL3bLEI57VLpDkFoxQ2rfowQXju2DkeRV4_SvOF-LCaO9IsZfAhFIliTTeo5RPs5EwBxlImuoeDlfBzKsTDEiHsGBqtJlp8fCNgHEjlOAx9UqU_mWaT";
    String instanceId = FirebaseInstanceId.getInstance().getToken();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Cart");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_cart_ui);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        query = firestore.collection("Users").document(user.getUid()).collection("Cart");
        cart_recylerView = findViewById(R.id.cart_RV);
        MakePO_do = findViewById(R.id.MakePO_do);
        adapter = new CartAdapter(query, this) {
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public void onBindViewHolder(CartHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("Error Adapater", e.getMessage());
            }
        };

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cart_recylerView.setLayoutManager(llm);
        MakePO_do.setOnClickListener(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(cart_recylerView.getContext(), llm.getOrientation());
        cart_recylerView.addItemDecoration(itemDecoration);
        cart_recylerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onCartSelected(DocumentSnapshot cart) {

    }


    @Override
    public void onClick(View view) {
        if (view == MakePO_do){
            Date calendar = Calendar.getInstance().getTime();
            final String date = calendar.toString();
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){

                        for (final DocumentSnapshot documentSnapshot : task.getResult()){
                            cart = documentSnapshot.toObject(Cart.class);

                            BigDecimal harga = new BigDecimal(cart.getHarga_barang().replace(".",""));
                            int quantitas = cart.getQuantitas_banyakBarang();
                            BigDecimal total = totalCost(quantitas,harga);
                            String TotalCost = total.toString();
                            transactionModel = new Transaction(cart.getNama_barang(),cart.getUserId()
                                    ,cart.getUnit(),cart.getNamaPerusahaan(),cart.getHarga_barang(),cart.getImageItemUrl(),cart.getNotificationId(),cart.getQuantitas_banyakBarang(),user.getUid(),cart.getUserId(),"Masih Dalam Proses",date,instanceId,TotalCost);
                            firestore.collection("Transaction").document().set(transactionModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Log.d("Data has been Added",task.toString());
                                    }
                                    if (task.isComplete()){
                                        firestore.collection("Users").document(user.getUid()).collection("Cart")
                                                .document(documentSnapshot.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Log.d("Susscess Delete Data",task.toString());
                                                    new Sendnotif().execute();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Error delete data",e.getMessage());
                                            }
                                        });

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Error add data",e.getMessage());
                                }
                            });
                        }
                    }
                    if (task.isComplete()){

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
    public ArrayList<String> checkMultipleBuyer(ArrayList<Cart> carts){
        ArrayList<String> userSellerId = new ArrayList<>();
        for (int i = 0; i<carts.size(); i++){
            cart = carts.get(i);
            Cart cartNext = carts.get(i+1);
            if (cartNext != null){
                if (!cart.getUserId().equals(cartNext.getUserId())){
                    userSellerId.add(cart.getUserId());
                }
            }
        }
        return userSellerId;
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
                postDataParam.put("to", cart.getNotificationId());
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
    public BigDecimal totalCost(int itemQuantity, BigDecimal itemPrice){
        BigDecimal itemCost,totalCost = null;
        itemCost = itemPrice.multiply(new BigDecimal(itemQuantity));
        totalCost = itemCost;
        return totalCost;
    }

}

