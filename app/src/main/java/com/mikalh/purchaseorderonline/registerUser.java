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
import android.widget.Toast;

import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Model.Company;
import com.mikalh.purchaseorderonline.Model.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link registerUser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link registerUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class registerUser extends android.support.v4.app.Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextInputEditText email_register, name_register, position_register, username_register,password_register;
    Button registerBtn;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    registerCompanyProfile previous;
    private OnFragmentInteractionListener mListener;

    public registerUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment registerUser.
     */
    // TODO: Rename and change types and number of parameters
    public static registerUser newInstance(String param1, String param2) {
        registerUser fragment = new registerUser();
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
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        previous = new registerCompanyProfile();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_user, container, false);
        email_register = v.findViewById(R.id.email_register);
        name_register = v.findViewById(R.id.name_register);
        position_register = v.findViewById(R.id.position_register);
        username_register = v.findViewById(R.id.username_register);
        password_register = v.findViewById(R.id.password_register);
        registerBtn = v.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        if (view == registerBtn){
                RegisterDo();
        }
    }

    void RegisterDo(){
        Company mcompany = CachePot.getInstance().pop(Company.class);

        final String CompanyName = mcompany.getNama_perusahaan();
        final String Address = mcompany.getAlamat_perusahaan();
        final String Province = mcompany.getProvinsi();
        final String City = mcompany.getKota();
        final String Telephone = mcompany.getNomorTelphone();
        final String FAX = mcompany.getNo_fax();
        final String Email = email_register.getText().toString();
        final String PICName = name_register.getText().toString();
        final String PICPossition = position_register.getText().toString();
        String Username = username_register.getText().toString();
        String Password = password_register.getText().toString();

        auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user = auth.getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(PICName).build();
                    user.updateProfile(profileChangeRequest).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e==null){
                                Log.e("Error :",e.getMessage());
                            }
                        }
                    });
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(),"Please Check Your email for verification",Toast.LENGTH_LONG).show();
                            }
                            else {

                            }
                        }
                    });
                    User userAdd = new User(PICName,Email,user.getUid(),PICPossition,Address,CompanyName,Telephone,FAX,City,Province,"");
                    firestore.collection("Users").document(user.getUid()).set(userAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(),"Successfuly Create User",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.e("Error save to firestore",e.getMessage());
                        }
                    });



                }else{
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error Create User",e.getMessage());
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
