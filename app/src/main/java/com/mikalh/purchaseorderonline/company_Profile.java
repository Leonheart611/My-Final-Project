package com.mikalh.purchaseorderonline;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;


public class company_Profile extends android.support.v4.app.Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    CollectionReference users;
    TextInputEditText companyName_profile, address_profile,
            province_profile,city_profile,fax_profile;
    Button btnSave;
    User profileUser;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String userID;
    CustomDialog customDialog;
    private OnFragmentInteractionListener mListener;

    public company_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment company_Profile.
     */
    // TODO: Rename and change types and number of parameters
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DocumentReference docRef = users.document(userID);

        View v = inflater.inflate(R.layout.fragment_company_profile, container, false);
        companyName_profile = v.findViewById(R.id.companyName_profile);
        address_profile = v.findViewById(R.id.address_profile);
        province_profile = v.findViewById(R.id.province_profile);
        city_profile = v.findViewById(R.id.city_profile);
        fax_profile = v.findViewById(R.id.fax_profile);
        btnSave = v.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

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
                    companyName_profile.setText(profileUser.getNama_perusahaan(), TextView.BufferType.EDITABLE);
                    address_profile.setText(profileUser.getAlamat_perusahaan(), TextView.BufferType.EDITABLE);
                    province_profile.setText(profileUser.getProvinsi(), TextView.BufferType.EDITABLE);
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

    // TODO: Rename method, update argument and hook method into UI event
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

        }
    }
    public void updateCompany(){
        customDialog.show();
        HashMap<String,Object> update = new HashMap<>();
        update.put("alamat_perusahaan",address_profile.getText().toString());
        update.put("nama_perusahaan",companyName_profile.getText().toString());
        update.put("no_fax",fax_profile.getText().toString());
        update.put("Provinsi",province_profile.getText().toString());
        update.put("historyUpdate", FieldValue.serverTimestamp());

        users.document(user.getUid()).update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    customDialog.dismiss();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}