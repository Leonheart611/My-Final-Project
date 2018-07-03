package com.mikalh.purchaseorderonline;

import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ThrowOnExtraProperties;
import com.mikalh.purchaseorderonline.Model.User;

import java.util.HashMap;


public class PIC_Profile extends android.support.v4.app.Fragment {
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
            ,telephone_profile,email_profile;
    User userProfile;
    CustomDialog customDialog;
    Button saveButton_profile,changePassword_profile;
    public PIC_Profile() {
        // Required empty public constructor
    }
    public static PIC_Profile newInstance(String param1, String param2) {
        PIC_Profile fragment = new PIC_Profile();
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
    TextInputLayout TIL_picName, TIL_picPos,TIL_telephone,TIL_email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_piccontact, container, false);
        customDialog.show();
        picName_profile = v.findViewById(R.id.picName_profile);
        picPosition_profile = v.findViewById(R.id.picPosition_profile);
        telephone_profile = v.findViewById(R.id.telephone_profile);
        saveButton_profile = v.findViewById(R.id.saveButton_profile);
        changePassword_profile = v.findViewById(R.id.changePassword_profile);
        email_profile = v.findViewById(R.id.email_profile);
        TIL_email = v.findViewById(R.id.TIL_email);
        TIL_picName = v.findViewById(R.id.TIL_picName);
        TIL_picPos = v.findViewById(R.id.TIL_picPosition);
        TIL_telephone = v.findViewById(R.id.TIL_telephone);
        saveButton_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean lanjut = true;
                TIL_picName.setErrorEnabled(false);
                TIL_picName.setError(null);
                TIL_picPos.setErrorEnabled(false);
                TIL_picPos.setError(null);
                TIL_telephone.setErrorEnabled(false);
                TIL_telephone.setError(null);
                TIL_email.setErrorEnabled(false);
                TIL_email.setError(null);
                if (picName_profile.getText().toString().isEmpty()){
                    TIL_picName.setErrorEnabled(true);
                    TIL_picName.setError("Harus Diisi");
                    lanjut = false;
                }if (picPosition_profile.getText().toString().isEmpty()){
                    TIL_picPos.setErrorEnabled(true);
                    TIL_picPos.setError("Harus Diisi");
                    lanjut = false;
                }if (telephone_profile.getText().toString().isEmpty()){
                    TIL_telephone.setErrorEnabled(true);
                    TIL_telephone.setError("Harus Diisi");
                    lanjut = false;
                }if (email_profile.getText().toString().isEmpty()){
                    TIL_email.setErrorEnabled(true);
                    TIL_email.setError("Harus Diisi");
                    lanjut = false;
                }
                if (lanjut) {
                    updateProfile();
                }
            }
        });
        changePassword_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePassword();
            }
        });

        if (!userID.equals(user.getUid())){
            picName_profile.setEnabled(false);
            picName_profile.setFocusable(false);
            picPosition_profile.setEnabled(false);
            picPosition_profile.setFocusable(false);
            telephone_profile.setEnabled(false);
            telephone_profile.setFocusable(false);
            email_profile.setEnabled(false);
            email_profile.setFocusable(false);
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
                    customDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Crashlytics.logException(e);
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                customDialog.dismiss();
            }
        });



        return v;
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void updateProfile(){
        customDialog.show();
        String email = email_profile.getText().toString();
        HashMap<String, Object> update = new HashMap<>();
        update.put("nama_PIC",picName_profile.getText().toString());
        update.put("jabatan_PIC",picPosition_profile.getText().toString());
        update.put("nomorTelphone",telephone_profile.getText().toString());
        update.put("email",email);
        update.put("username","");
        users.document(user.getUid()).update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    customDialog.dismiss();
                    Toast.makeText(getActivity(),"Berhasil Update Profile",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_LONG).show();
                Crashlytics.logException(e);
                customDialog.dismiss();
            }
        });
        if (!user.getEmail().equals(email)){
            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        Crashlytics.log("Success Change Email");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Crashlytics.logException(e);
                }
            });
        }
    }

    public void UpdatePassword(){
        final Dialog dialog = new Dialog(getActivity());
        /* dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        dialog.setContentView(R.layout.update_password);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final TextInputEditText updatePassword = dialog.findViewById(R.id.updatePassword);
        final Button updateDo = dialog.findViewById(R.id.updatePassword_do);
        final TextInputEditText oldPassword = dialog.findViewById(R.id.oldPassword);
        final TextInputEditText retypeNewPassword = dialog.findViewById(R.id.retypeNewPassword);
        final TextInputLayout TIL_notifUpdate = dialog.findViewById(R.id.TIL_notifUpdate);
        updateDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TIL_notifUpdate.setError(null);
                final String password = updatePassword.getText().toString();
                if (password.isEmpty() || oldPassword.getText().toString().isEmpty() || retypeNewPassword.getText().toString().isEmpty()){
                    TIL_notifUpdate.setError("Please Fill All Form");
                }else {
                    dialog.dismiss();
                    customDialog.show();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), oldPassword.getText().toString());
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getActivity(),"Berhasil Ganti Password",Toast.LENGTH_LONG).show();
                                        customDialog.dismiss();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Crashlytics.logException(e);
                                    Toast.makeText(getActivity(),"Gagal Merubah password,Silahkan coba kembali",Toast.LENGTH_LONG).show();
                                    customDialog.dismiss();
                                    dialog.show();
                                }
                            });
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
        dialog.show();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
