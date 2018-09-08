package com.mikalh.purchaseorderonline;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Model.User;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.HashMap;


public class company_Profile extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    CollectionReference users;
    TextInputEditText companyName_profile, address_profile,
            city_profile,fax_profile;
    Button btnSave;
    User profileUser;
    SearchableSpinner province_profile;
    String[] listProvince = new String[]{
            "Aceh" ,
            "Bali" ,
            "Banten" ,
            "Bengkulu" ,
            "Gorontalo" ,
            "DKI Jakarta" ,
            "Jambi" ,
            "Jawa Barat" ,
            "Jawa Tengah" ,
            "Jawa Timur" ,
            "Kalimantan Barat" ,
            "Kalimantan Selatan" ,
            "Kalimantan Tengah" ,
            "Kalimantan Timur" ,
            "Kalimantan Utara" ,
            "Kepulauan Bangka Belitung" ,
            "Kepulauan Riau" ,
            "Lampung" ,
            "Maluku" ,
            "Maluku Utara" ,
            "Nusa Tenggara Barat" ,
            "Nusa Tenggara Timur" ,
            "Papua" ,
            "Papua Barat" ,
            "Riau" ,
            "Sulawesi Barat" ,
            "Sulawesi Selatan" ,
            "Sulawesi Tengah" ,
            "Sulawesi Tenggara" ,
            "Sulawesi Utara" ,
            "Sumatera Barat" ,
            "Sumatera Selatan" ,
            "Sumatera Utara" ,
            "Yogyakarta"

    };
    private String mParam1;
    private String mParam2;
    String userID;
    CustomDialog customDialog;
    private OnFragmentInteractionListener mListener;

    public company_Profile() {
        // Required empty public constructor
    }


    public static company_Profile newInstance(String param1, String param2) {
        company_Profile fragment = new company_Profile();
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
        userID = getActivity().getIntent().getExtras().getString(companyNameSearch.USER_ID);
        users = firestore.collection("Users");
        customDialog = new CustomDialog(getActivity());
    }
    TextInputLayout TIL_CompanyName,TIL_address,TIL_city,TIL_fax;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DocumentReference docRef = users.document(userID);

        View v = inflater.inflate(R.layout.fragment_company_profile, container, false);
        companyName_profile = v.findViewById(R.id.companyName_profile);
        address_profile = v.findViewById(R.id.address_profile);
        province_profile = v.findViewById(R.id.province_profile);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,listProvince);
        province_profile.setAdapter(adapter);
        city_profile = v.findViewById(R.id.city_profile);
        fax_profile = v.findViewById(R.id.fax_profile);
        btnSave = v.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        TIL_CompanyName = v.findViewById(R.id.TIL_CompanyName);
        TIL_fax = v.findViewById(R.id.TIL_fax);
        TIL_city = v.findViewById(R.id.TIL_city);
        TIL_address = v.findViewById(R.id.TIL_address);
        if (!userID.equals(user.getUid())){
            companyName_profile.setEnabled(false);
            companyName_profile.setFocusable(false);
            address_profile.setEnabled(false);
            address_profile.setFocusable(false);
            province_profile.setEnabled(false);
            province_profile.setFocusable(false);
            city_profile.setEnabled(false);
            city_profile.setFocusable(false);
            fax_profile.setEnabled(false);
            fax_profile.setEnabled(false);

            btnSave.setVisibility(View.INVISIBLE);
        }

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    profileUser = documentSnapshot.toObject(User.class);
                    int provincePos = 0;
                    for (int i = 0 ; i < listProvince.length ; i++){
                        if (listProvince[i].equals(profileUser.getProvinsi())){
                            provincePos = i;
                            break;
                        }
                    }
                    companyName_profile.setText(profileUser.getNama_perusahaan(), TextView.BufferType.EDITABLE);
                    address_profile.setText(profileUser.getAlamat_perusahaan(), TextView.BufferType.EDITABLE);
                    province_profile.setSelection(provincePos);
                    city_profile.setText(profileUser.getKota(), TextView.BufferType.EDITABLE);
                    fax_profile.setText(profileUser.getNo_fax(), TextView.BufferType.EDITABLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error get Data",e.getMessage());
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


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
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view == btnSave){
            boolean lanjutkan = true;
            TIL_CompanyName.setError(null);
            TIL_CompanyName.setErrorEnabled(false);
            TIL_address.setError(null);
            TIL_address.setErrorEnabled(false);
            /*TIL_province.setError(null);
            TIL_province.setErrorEnabled(false);*/
            TIL_city.setError(null);
            TIL_city.setErrorEnabled(false);
            TIL_fax.setError(null);
            TIL_fax.setErrorEnabled(false);
            if (companyName_profile.getText().toString().isEmpty()){
                TIL_CompanyName.setErrorEnabled(true);
                TIL_CompanyName.setError("Harus Diisi");
                lanjutkan = false;
            }if (address_profile.getText().toString().isEmpty()){
                TIL_address.setErrorEnabled(true);
                TIL_address.setError("Harus Diisi");
                lanjutkan = false;
            }/*if (province_profile.getText().toString().isEmpty()){
                TIL_province.setErrorEnabled(true);
                TIL_province.setError("Harus Diisi");
                lanjutkan = false;
            }*/if (city_profile.getText().toString().isEmpty()){
                TIL_city.setErrorEnabled(true);
                TIL_city.setError("Harus Diisi");
                lanjutkan = false;
            }if (fax_profile.getText().toString().isEmpty()){
                TIL_fax.setErrorEnabled(true);
                TIL_fax.setError("Harus Diisi");
                lanjutkan = false;
            }
            if (lanjutkan) {
                updateCompany();
            }
        }
    }
    public void updateCompany(){
        customDialog.show();
        HashMap<String,Object> update = new HashMap<>();
        update.put("alamat_perusahaan",address_profile.getText().toString());
        update.put("nama_perusahaan",companyName_profile.getText().toString());
        update.put("no_fax",fax_profile.getText().toString());
        update.put("Provinsi","");
        update.put("historyUpdate", FieldValue.serverTimestamp());

        users.document(user.getUid()).update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    customDialog.dismiss();
                    Toast.makeText(getActivity(),"Berhasil Update data Profile",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Crashlytics.logException(e);
                customDialog.dismiss();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
