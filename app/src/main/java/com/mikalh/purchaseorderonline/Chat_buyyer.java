package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.ChatAdapter;
import com.mikalh.purchaseorderonline.Adapter.ChatListAdapter;


public class Chat_buyyer extends Fragment implements ChatListAdapter.OnChatListListenerListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RecyclerView chatBuyer_RV;
    ChatListAdapter adapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    Query query;
    public Chat_buyyer() {
        // Required empty public constructor
    }

    public static Chat_buyyer newInstance(String param1, String param2) {
        Chat_buyyer fragment = new Chat_buyyer();
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
        query = firestore.collection("RoomChat").whereEqualTo("Users."+user.getUid(),true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat_buyyer, container, false);
        chatBuyer_RV = v.findViewById(R.id.chatBuyyer_RV);
        adapter = new ChatListAdapter(query,this,user){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public void onBindViewHolder(ChatListHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Crashlytics.logException(e);
            }
        };
        chatBuyer_RV.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setSmoothScrollbarEnabled(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        chatBuyer_RV.setLayoutManager(llm);
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
        adapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
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
    public void onChatListSelected(DocumentSnapshot chat) {
        Intent i = new Intent(getActivity(),DetailChat.class);
        i.putExtra(detailItem.ROOMID,chat.getId());
        i.putExtra(detailItem.SENDER_ID,user.getUid());
        startActivity(i);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
