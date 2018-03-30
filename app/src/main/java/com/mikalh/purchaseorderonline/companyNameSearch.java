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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link companyNameSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link companyNameSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class companyNameSearch extends android.support.v4.app.Fragment implements CompanyAdapter.OnCompanySelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment companyNameSearch.
     */
    // TODO: Rename and change types and number of parameters
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
