package com.mikalh.purchaseorderonline;

import android.content.Context;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Model.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PICContact.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PICContact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PICContact extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    String userID;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    CollectionReference users;
    TextInputEditText picName_profile,picPosition_profile
            ,telephone_profile,username_profile,email_profile;
    User userProfile;
    Button saveButton_profile,changePassword_profile;
    public PICContact() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PICContact.
     */
    // TODO: Rename and change types and number of parameters
    public static PICContact newInstance(String param1, String param2) {
        PICContact fragment = new PICContact();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_piccontact, container, false);
        picName_profile = v.findViewById(R.id.picName_profile);
        picPosition_profile = v.findViewById(R.id.picPosition_profile);
        telephone_profile = v.findViewById(R.id.telephone_profile);
        saveButton_profile = v.findViewById(R.id.saveButton_profile);
        changePassword_profile = v.findViewById(R.id.changePassword_profile);
        email_profile = v.findViewById(R.id.email_profile);
        username_profile = v.findViewById(R.id.username_profile);

        if (!userID.equals(user.getUid())){
            picName_profile.setEnabled(false);
            picName_profile.setFocusable(false);
            picPosition_profile.setEnabled(false);
            picPosition_profile.setFocusable(false);
            telephone_profile.setEnabled(false);
            telephone_profile.setFocusable(false);
            email_profile.setEnabled(false);
            email_profile.setFocusable(false);
            username_profile.setEnabled(false);
            username_profile.setFocusable(false);

            saveButton_profile.setVisibility(View.INVISIBLE);
            changePassword_profile.setVisibility(View.INVISIBLE);

        }

        DocumentReference docRef = users.document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    userProfile = documentSnapshot.toObject(User.class);
                    picName_profile.setText(userProfile.getNama_PIC(), TextView.BufferType.EDITABLE);
                    picPosition_profile.setText(userProfile.getJabatan_PIC(), TextView.BufferType.EDITABLE);
                    telephone_profile.setText(userProfile.getNomorTelphone(), TextView.BufferType.EDITABLE);
                    email_profile.setText(userProfile.getEmail(), TextView.BufferType.EDITABLE);
                    username_profile.setText(userProfile.getUsername(), TextView.BufferType.EDITABLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Gagal mendapatkan data",e.getMessage());
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
