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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.CatalogueAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link catalogueItemSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link catalogueItemSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class catalogueItemSearch extends android.support.v4.app.Fragment  implements CatalogueAdapter.OnClickCatalogueListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String KEY_ITEM_ID = "keyItemID";
    private String mParam1;
    private String mParam2;



    private OnFragmentInteractionListener mListener;




    public catalogueItemSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment catalogueItemSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static catalogueItemSearch newInstance(String param1, String param2) {
        catalogueItemSearch fragment = new catalogueItemSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String queryBack="";
    RecyclerView catalogueSearch_rv;
    CatalogueAdapter adapter;
    FirebaseFirestore firestore;
    Query query;
    FirebaseAuth auth;
    FirebaseUser user;
    CollectionReference itemCollection;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        firestore = FirebaseFirestore.getInstance();
        itemCollection = firestore.collection("Items");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent intent = getActivity().getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            queryBack = intent.getStringExtra(SearchManager.QUERY);
        }
        if (!queryBack.isEmpty()){
            query = itemCollection.whereGreaterThanOrEqualTo("nama_barang",queryBack).whereLessThan("userId",user.getUid());
        }else {
            query = itemCollection;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalogue_item_search, container, false);
        catalogueSearch_rv = view.findViewById(R.id.catalogueSearch_rv);
        adapter = new CatalogueAdapter(query,this){
            @Override
            public void onBindViewHolder(CatalogueHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            public CatalogueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("Error Catalogue",e.getMessage());
            }
        };
        LinearLayoutManager lim = new LinearLayoutManager(getActivity());
        lim.setOrientation(LinearLayoutManager.VERTICAL);
        catalogueSearch_rv.setLayoutManager(lim);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(catalogueSearch_rv.getContext(),lim.getOrientation());
        catalogueSearch_rv.addItemDecoration(itemDecoration);
        catalogueSearch_rv.setAdapter(adapter);


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
    public void onClickCatalogueListener(DocumentSnapshot item) {
        Intent i = new Intent(getActivity(),detailItem.class);
        i.putExtra(KEY_ITEM_ID,item.getId());
        startActivity(i);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null){
            adapter.startListening();
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
