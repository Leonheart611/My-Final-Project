package com.mikalh.purchaseorderonline;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.CompanyAdapter;



public class companyNameSearch extends android.support.v4.app.Fragment implements CompanyAdapter.OnCompanySelectedListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String USER_ID = "userID";
    String queryBack="";
    FirebaseFirestore firestore;
    RecyclerView companyName_rv;
    Query query;
    CompanyAdapter adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public companyNameSearch() {
        // Required empty public constructor
    }

    public static companyNameSearch newInstance(String param1, String param2) {
        companyNameSearch fragment = new companyNameSearch();
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
        Intent intent = getActivity().getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            queryBack = intent.getStringExtra(SearchManager.QUERY);
        }
        firestore = FirebaseFirestore.getInstance();
        if (!queryBack.isEmpty()){
            query = firestore.collection("Users").whereGreaterThanOrEqualTo("nama_perusahaan",queryBack);
        }else {
            query = firestore.collection("Users");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_company_name, container, false);
        companyName_rv = v.findViewById(R.id.companyName_rv);
        adapter = new CompanyAdapter(query,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public void onBindViewHolder(CompanyHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            public CompanyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("Error Adapter Company",e.getMessage());
            }
        };
        LinearLayoutManager lim = new LinearLayoutManager(getActivity());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        companyName_rv.setLayoutManager(lim);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(companyName_rv.getContext(),lim.getOrientation());
        companyName_rv.addItemDecoration(itemDecoration);
        companyName_rv.setAdapter(adapter);


        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!= null){
            adapter.stopListening();
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
    public void onCompanySelected(DocumentSnapshot user) {
        Intent i = new Intent(getActivity(),Profile.class);
        i.putExtra(USER_ID,user.getId());
        startActivity(i);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
