package com.mikalh.purchaseorderonline;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class home extends android.support.v4.app.Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView profileImage,catalogImage,logoutImage,myOrderImage;
    TextView profileTxt, catalogTxt,logoutTxt,myOrderTxt;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String USER_ID = "userID";
    FirebaseAuth auth;
    FirebaseUser user;
    private OnFragmentInteractionListener mListener;
    ViewPager mainPagger;
    public home() {
        // Required empty public constructor
    }


    public static home newInstance(String param1, String param2) {
        home fragment = new home();
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
        mainPagger = getActivity().findViewById(R.id.mainPagger);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        profileImage = view.findViewById(R.id.profileImage);
        profileImage.setOnClickListener(this);
        profileTxt = view.findViewById(R.id.ProfileTxt);
        profileTxt.setOnClickListener(this);

        catalogImage = view.findViewById(R.id.catalogImage);
        catalogImage.setOnClickListener(this);
        catalogTxt = view.findViewById(R.id.catalogTxt);
        catalogTxt.setOnClickListener(this);

        myOrderImage = view.findViewById(R.id.myOrderImage);
        myOrderImage.setOnClickListener(this);
        myOrderTxt = view.findViewById(R.id.myOrderTxt);
        myOrderTxt.setOnClickListener(this);


        logoutImage = view.findViewById(R.id.logoutImage);
        logoutImage.setOnClickListener(this);
        logoutTxt = view.findViewById(R.id.logoutTxt);
        logoutTxt.setOnClickListener(this);

        return view;
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
        if (view == logoutImage || view == logoutTxt){
           logOut("Are You Sure Want Log Out?");
        }
        if (view == catalogImage || view == catalogTxt){
            Intent i = new Intent(getActivity(),myCatalogue.class);
            startActivity(i);
        }
        if (view == profileImage || view == profileTxt){
            Intent i = new Intent(getActivity(),Profile.class);
            i.putExtra(USER_ID,user.getUid());
            startActivity(i);
        }
        if(view == myOrderImage || view == myOrderTxt){
            mainPagger.setCurrentItem(1,true);
        }
    }
    void logOut(String judul){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.log_out_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView judulTxt = dialog.findViewById(R.id.judulTxt);
        judulTxt.setText(judul);
        Button ok_button = dialog.findViewById(R.id.ok_button);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(),MainActivity.class);
                Toast.makeText(getActivity(),"Success Sign Out",Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        });
        Button cancel_popUp = dialog.findViewById(R.id.cancel_popUp);
        cancel_popUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
