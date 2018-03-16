package com.mikalh.purchaseorderonline;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home extends android.support.v4.app.Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView profileImage,catalogImage,searchImage,logoutImage;
    TextView profileTxt, catalogTxt,searchTxt,logoutTxt;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
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

        searchImage = view.findViewById(R.id.searchImage);
        searchImage.setOnClickListener(this);
        searchTxt = view.findViewById(R.id.searchTxt);
        searchTxt.setOnClickListener(this);

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
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getActivity(),MainActivity.class);
            Toast.makeText(getActivity(),"Success Sign Out",Toast.LENGTH_LONG).show();
            startActivity(i);
        }
        if (view == catalogImage || view == catalogTxt){
            Intent i = new Intent(getActivity(),myCatalogue.class);
            startActivity(i);
        }
        if (view == profileImage || view == profileTxt){
            Intent i = new Intent(getActivity(),Profile.class);
            startActivity(i);
        }
        if(view == searchImage || view == searchTxt){
            Intent i = new Intent(getActivity(),search.class);
            startActivity(i);
        }
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
