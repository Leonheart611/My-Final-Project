package com.mikalh.purchaseorderonline;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link home_buyyer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link home_buyyer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_buyyer extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageButton myProfile_buyyer, search_buyyer,send_po,logout_buyyer;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView search_buyyerTxt,sent_buyyerTxt,logOut_buyyerTxt,profileBuyyer_txt;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String USER_ID = "userID";
    private OnFragmentInteractionListener mListener;

    public home_buyyer() {
        // Required empty public constructor
    }
    public static home_buyyer newInstance(String param1, String param2) {
        home_buyyer fragment = new home_buyyer();
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
        user = auth.getCurrentUser();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_buyyer, container, false);
        myProfile_buyyer = v.findViewById(R.id.myProfile_buyyer);
        myProfile_buyyer.setOnClickListener(this);
        search_buyyer = v.findViewById(R.id.search_buyyer);
        search_buyyer.setOnClickListener(this);
        send_po = v.findViewById(R.id.send_po);
        send_po.setOnClickListener(this);
        logout_buyyer = v.findViewById(R.id.logout_buyyer);
        logout_buyyer.setOnClickListener(this);
        search_buyyerTxt = v.findViewById(R.id.search_buyyerTxt);
        search_buyyerTxt.setOnClickListener(this);
        sent_buyyerTxt = v.findViewById(R.id.sent_buyyerTxt);
        sent_buyyerTxt.setOnClickListener(this);
        logOut_buyyerTxt = v.findViewById(R.id.logOut_buyyerTxt);
        logOut_buyyerTxt.setOnClickListener(this);
        profileBuyyer_txt = v.findViewById(R.id.profileBuyyer_txt);
        profileBuyyer_txt.setOnClickListener(this);
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
    public void onClick(View v) {
        if (v == logout_buyyer || v == logOut_buyyerTxt){
            logOut("Are You Sure Want Log Out?");
        }
        if (v == myProfile_buyyer || v == profileBuyyer_txt){
            Intent i = new Intent(getActivity(),Profile.class);
            i.putExtra(USER_ID,user.getUid());
            startActivity(i);
        }
        if(v == search_buyyer || v == search_buyyerTxt){
            Intent i = new Intent(getActivity(),Search.class);
            startActivity(i);
        }if (v == send_po || v == sent_buyyerTxt){
            Intent i = new Intent(getActivity(),CartBuyer.class);
            startActivity(i);
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
